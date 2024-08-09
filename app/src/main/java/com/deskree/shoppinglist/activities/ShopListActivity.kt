package com.deskree.shoppinglist.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.deskree.shoppinglist.R
import com.deskree.shoppinglist.billing.BillingManager
import com.deskree.shoppinglist.databinding.ActivityShopListBinding
import com.deskree.shoppinglist.db.MainViewModel
import com.deskree.shoppinglist.db.ShopListItemAdapter
import com.deskree.shoppinglist.dialogs.ClearListDialog
import com.deskree.shoppinglist.dialogs.EditListItemDialog
import com.deskree.shoppinglist.entities.LibraryItem
import com.deskree.shoppinglist.entities.ShopListItem
import com.deskree.shoppinglist.entities.ShopListNameItem
import com.deskree.shoppinglist.utils.KeyboardControl
import com.deskree.shoppinglist.utils.ShareHelper
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds


class ShopListActivity : AppCompatActivity(), ShopListItemAdapter.Listener {
    private lateinit var binding: ActivityShopListBinding
    private var shopListNameItem: ShopListNameItem? = null
    private lateinit var saveItem: MenuItem
    private var edItem: EditText? = null
    private lateinit var clearList: MenuItem
    private var clearListIsVisible = false
    private var adapter: ShopListItemAdapter? = null
    private lateinit var textWatcher: TextWatcher
    private var menuGlob: Menu? = null
    private lateinit var mAdView: AdView
    private lateinit var pref: SharedPreferences
    private val mainViewModel: MainViewModel by viewModels{
        MainViewModel.MainViewModelFactory((applicationContext as MainApp).database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = getSharedPreferences(BillingManager.MAIN_PREF, MODE_PRIVATE)
        // Ініціалізація реклами
        // TODO: Ініціалізація реклами ВИМКНЕНА
//        if(!pref.getBoolean(BillingManager.REMOVE_ADS_KEY, false)) {
//            mAdView = binding.adView
//            mAdView.visibility = View.VISIBLE
//            MobileAds.initialize(this) {}
//            val adRequest = AdRequest.Builder().build()
//            mAdView.loadAd(adRequest)
//        }

        initShopListNameItem()
        listItemObserver()
        actionBarSettings()
        initRcView()
    }


    override fun onPause() {
        super.onPause()
        KeyboardControl.showOrHideKeyboard(KeyboardControl.STATE_HIDE, this@ShopListActivity)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuGlob = menu

        menuInflater.inflate(R.menu.shop_list_menu, menu)
        saveItem = menu?.findItem(R.id.save_item)!!
        clearList = menu.findItem(R.id.clear_list)
        val newItem = menu.findItem(R.id.new_item)

        clearList.isVisible = clearListIsVisible

        edItem = newItem.actionView?.findViewById(R.id.edtNewShopItem) as EditText

        edItem?.setOnEditorActionListener { _, _, _ ->

            addNewShopItem(edItem?.text.toString())
            menuGlob?.findItem(R.id.new_item)?.collapseActionView()
            true
        }


        newItem.setOnActionExpandListener(expandActionView())
        saveItem.isVisible = false
        textWatcher = textWatcher()
        return true
    }

    private fun textWatcher(): TextWatcher{
        return object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mainViewModel.getAllLibraryItemsM("%$s%")
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
    }

    private fun actionBarSettings(){
        val ab = supportActionBar
        ab?.setDisplayHomeAsUpEnabled(true)
        ab?.title = "${shopListNameItem?.name}"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.new_item -> {
                KeyboardControl.showOrHideKeyboard(KeyboardControl.STATE_SHOW, this@ShopListActivity)
            }
            R.id.save_item -> {
                addNewShopItem(edItem?.text.toString())
                menuGlob?.findItem(R.id.new_item)?.collapseActionView()
            }
            android.R.id.home -> {
                finish()
            }
            R.id.clear_list -> ClearListDialog.showDialog(this, object : ClearListDialog.Listener{
                override fun onClick() {
                    mainViewModel.clearShopListItems(shopListNameItem?.id!!)
                    clearList.isVisible = false
                }

            })
            R.id.share_list -> startActivity(Intent.createChooser(
                ShareHelper.shareShopList(
                    adapter?.currentList!!,
                    shopListNameItem?.name!!),
                getString(R.string.shareBy)))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addNewShopItem(name: String){
        if(name.isEmpty()) return
        val item = ShopListItem(
            null,
            name,
            "",
            false,
            shopListNameItem?.id!!,
            0
        )
        edItem?.setText("")
        mainViewModel.insertShopItem(item)
    }

    private fun listItemObserver(){
        mainViewModel.getAllItemsFromList(shopListNameItem?.id!!).observe(this) {
            adapter?.submitList(it)
            saveItemCount(it)
            binding.tvLabelEmpty.visibility = if(it.isEmpty()){
                View.VISIBLE
            }else{
                clearListIsVisible = true
                View.GONE
            }
        }
    }

    private fun libraryItemObserver(){
        mainViewModel.libraryItems.observe(this) {
            val tempShopList = ArrayList<ShopListItem>()
            it.forEach { item ->
                val shopItem = ShopListItem(
                    item.id,
                    item.name,
                    "",
                    false,
                    0,
                    1
                )
                tempShopList.add(shopItem)
            }
            adapter?.submitList(tempShopList)
            binding.tvLabelEmpty.visibility = if(it.isEmpty()){
                View.VISIBLE
            }else{
                View.GONE
            }
        }
    }

    private fun initRcView() = with(binding){
        adapter = ShopListItemAdapter(this@ShopListActivity)
        rcViewItems.layoutManager = LinearLayoutManager(this@ShopListActivity)
        rcViewItems.adapter = adapter
    }

    private fun expandActionView(): MenuItem.OnActionExpandListener{
        return object : MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                saveItem.isVisible = true
                clearList.isVisible = false
                edItem?.addTextChangedListener(textWatcher)
                libraryItemObserver()
                mainViewModel.getAllItemsFromList(shopListNameItem?.id!!).removeObservers(this@ShopListActivity)
                mainViewModel.getAllLibraryItemsM("%%")
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                edItem?.clearFocus()
                KeyboardControl.showOrHideKeyboard(KeyboardControl.STATE_HIDE, this@ShopListActivity)
                saveItem.isVisible = false
                invalidateOptionsMenu()
                edItem?.removeTextChangedListener(textWatcher)
                mainViewModel.libraryItems.removeObservers(this@ShopListActivity)
                edItem?.setText("")
                listItemObserver()
                return true
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
//        if (ev.action == MotionEvent.ACTION_DOWN)
//            KeyboardControl.showOrHideKeyboard(KeyboardControl.STATE_HIDE, this@ShopListActivity)
        return super.dispatchTouchEvent(ev)
    }

    private fun initShopListNameItem(){
        shopListNameItem = intent.getSerializableExtra(SHOP_LIST_NAME) as ShopListNameItem
    }

    override fun deleteItem(itemID: Int) {
        mainViewModel.deleteShopListItem(itemID)
    }

    override fun onClickItem(shopListItem: ShopListItem, state: Int) {
        when(state){
            ShopListItemAdapter.CHECK_BOX -> mainViewModel.updateListItem(shopListItem)
            ShopListItemAdapter.EDIT -> editListItem(shopListItem)
            ShopListItemAdapter.EDIT_LIBRARY_ITEM -> editLibraryItem(shopListItem)
            ShopListItemAdapter.DELETE_LIBRARY_ITEM -> {
                mainViewModel.deleteLibraryItem(shopListItem.id!!)
                mainViewModel.getAllLibraryItemsM("%${edItem?.text.toString()}%")
            }
            ShopListItemAdapter.ADD -> {
                addNewShopItem(shopListItem.name)
                menuGlob?.findItem(R.id.new_item)?.collapseActionView()
            }
        }
    }

    private fun editListItem(shopListItem: ShopListItem) {
        EditListItemDialog.showDialog(this, object : EditListItemDialog.Listener{
            override fun onClick(item: ShopListItem) {
                mainViewModel.updateListItem(item)
            }

            override fun onDismiss() {
                KeyboardControl.showOrHideKeyboard(KeyboardControl.STATE_HIDE, this@ShopListActivity)
            }

            override fun onStartDialog() {
                KeyboardControl.showOrHideKeyboard(KeyboardControl.STATE_SHOW, this@ShopListActivity)
            }
        }, shopListItem)
    }

    private fun editLibraryItem(shopListItem: ShopListItem) {
        EditListItemDialog.showDialog(this, object : EditListItemDialog.Listener{

            override fun onClick(item: ShopListItem) {
                mainViewModel.updateLibraryItem(LibraryItem(item.id, item.name))
                mainViewModel.getAllLibraryItemsM("%${edItem?.text.toString()}%")
            }

            override fun onDismiss() {}
            override fun onStartDialog() {}
        }, shopListItem)
    }

    private fun saveItemCount(shopListItems: List<ShopListItem>){
        var checkedItemCounter = 0
        var allItemCount = 0

        shopListItems.forEach {
            if(shopListNameItem?.id == it.listID) {
                if (it.itemChecked) {
                    checkedItemCounter++
                }
                allItemCount++
            }
        }
        val tempShopListNameItem = shopListNameItem?.copy(
            allItemCounter = allItemCount,
            checkedItemsCounter = checkedItemCounter)
        mainViewModel.updateListName(tempShopListNameItem!!)
    }

    companion object{
        const val SHOP_LIST_NAME = "shop_list_name"
    }
}