package com.deskree.shoppinglist.dialogs

import android.annotation.SuppressLint
import android.app.Activity
import androidx.appcompat.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.deskree.shoppinglist.R
import com.deskree.shoppinglist.databinding.EditListItemDialogBinding
import com.deskree.shoppinglist.entities.ShopListItem
import com.deskree.shoppinglist.utils.KeyboardControl

object EditListItemDialog {
    @SuppressLint("StaticFieldLeak")
    private lateinit var binding: EditListItemDialogBinding
    private var dialog: AlertDialog? = null

    fun showDialog(context: Context, listener: Listener, item: ShopListItem){
        val builder = AlertDialog.Builder(context, R.style.AlertDialogCustom)
        binding = EditListItemDialogBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)

        listener.onStartDialog()

        binding.apply {
            edtNameItem.setText(item.name)
            edtNameItem.clearFocus()
            edtNameItem.requestFocus()
            edtDescItem.setText(item.itemInfo)

            // ЗБЕРЕЖЕННЯ РЕДАГУВАННЯ ПРЕДМЕТА
            edtDescItem.setOnEditorActionListener { textView, i, keyEvent ->
                saveItem(context, listener, item)
                true
            }
            if(item.itemType == 1) {
                edtDescItem.visibility = View.GONE
                labelDesc.visibility = View.GONE
            }

            // ЗБЕРЕЖЕННЯ РЕДАГУВАННЯ ПРЕДМЕТА
            btnSaveEdit.setOnClickListener{
                saveItem(context, listener, item)
            }
            btnCancel.setOnClickListener {
                dialog?.dismiss()
            }
        }
        dialog = builder.create()
        dialog?.window?.setBackgroundDrawable(null)
        dialog?.show()
        dialog?.setOnDismissListener {
            listener.onDismiss()
        }
    }

    private fun saveItem(context: Context, listener: Listener, item: ShopListItem) = with(binding){
        if(edtNameItem.text.isNullOrEmpty()){
            edtNameItem.error = context.getString(R.string.edtError)
        }else{
            listener.onClick(item.copy(
                name = edtNameItem.text.toString().trim(),
                itemInfo = edtDescItem.text.toString().trim()))
            dialog?.dismiss()
        }
    }

    interface Listener{
        fun onClick(item: ShopListItem)
        fun onDismiss()
        fun onStartDialog()
    }
}