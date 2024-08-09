package com.deskree.shoppinglist.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.deskree.shoppinglist.R
import com.deskree.shoppinglist.activities.MainApp
import com.deskree.shoppinglist.activities.ShopListActivity
import com.deskree.shoppinglist.databinding.FragmentShopListNamesBinding
import com.deskree.shoppinglist.db.MainViewModel
import com.deskree.shoppinglist.db.ShopListNameAdapter
import com.deskree.shoppinglist.dialogs.DeleteDialog
import com.deskree.shoppinglist.dialogs.NewListDialog
import com.deskree.shoppinglist.entities.ShopListNameItem
import com.deskree.shoppinglist.utils.KeyboardControl
import com.deskree.shoppinglist.utils.TimeManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class ShopListNamesFragment : BaseFragment(), ShopListNameAdapter.Listener {
    private lateinit var binding: FragmentShopListNamesBinding
    private lateinit var adapter: ShopListNameAdapter
    private lateinit var defPref: SharedPreferences

    private val mainViewModel: MainViewModel by activityViewModels{
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).database)
    }

    override fun onClickNew() {
        NewListDialog.showDialog(activity as AppCompatActivity, object : NewListDialog.Listener{
            override fun onStartDi() {
                KeyboardControl.showOrHideKeyboard(KeyboardControl.STATE_SHOW, activity as AppCompatActivity)
            }

            override fun onClick(name: String) {
                val shopListNameItem = ShopListNameItem(
                    null,
                    name,
                    TimeManager.getCurrentTime(),
                    0,
                    0,
                    ""
                )
                mainViewModel.insertShoppingListName(shopListNameItem)
            }

            override fun onDismiss() {
                val bNav = activity?.findViewById<BottomNavigationView>(R.id.bNav)
                bNav?.selectedItemId = R.id.shop_list
                KeyboardControl.showOrHideKeyboard(KeyboardControl.STATE_HIDE, activity as AppCompatActivity)
            }

        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding = FragmentShopListNamesBinding.inflate(inflater, container, false)

        defPref = PreferenceManager.getDefaultSharedPreferences(requireActivity())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        observer()
    }

    private fun initRcView() = with(binding){
        rcView.layoutManager = LinearLayoutManager(activity)
        adapter = ShopListNameAdapter(this@ShopListNamesFragment, defPref)
        rcView.adapter = adapter
    }

    private fun observer(){
        mainViewModel.allShopListNamesItem.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.tvLabelEmpty.visibility = if(it.isEmpty()){
                View.VISIBLE
            }else{
                View.GONE
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ShopListNamesFragment()
    }

    override fun deleteItem(id: Int) {
        DeleteDialog.showDialog(context as AppCompatActivity, object : DeleteDialog.Listener{
            override fun onClick() {
                mainViewModel.deleteShopList(id)
            }
        })
    }

    override fun editItem(shopListNameItem: ShopListNameItem) {
        NewListDialog.showDialog(activity as AppCompatActivity, object : NewListDialog.Listener{
            override fun onStartDi() {
                KeyboardControl.showOrHideKeyboard(KeyboardControl.STATE_SHOW, activity as AppCompatActivity)
            }

            override fun onClick(name: String) {
                mainViewModel.updateListName(shopListNameItem.copy(name = name))
            }

            override fun onDismiss() {
                KeyboardControl.showOrHideKeyboard(KeyboardControl.STATE_HIDE, activity as AppCompatActivity)
            }
        }, shopListNameItem.name)
    }

    override fun onClickItem(shopListNameItem: ShopListNameItem) {
        val i = Intent(activity, ShopListActivity::class.java).apply {
            putExtra(ShopListActivity.SHOP_LIST_NAME, shopListNameItem)
        }
        startActivity(i)
    }
}