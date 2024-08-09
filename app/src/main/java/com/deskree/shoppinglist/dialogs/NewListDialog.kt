package com.deskree.shoppinglist.dialogs

import android.annotation.SuppressLint
import android.app.Activity
import androidx.appcompat.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.deskree.shoppinglist.R
import com.deskree.shoppinglist.databinding.NewListDialogBinding
import com.deskree.shoppinglist.utils.KeyboardControl

object NewListDialog {
    @SuppressLint("StaticFieldLeak")
    private lateinit var binding: NewListDialogBinding
    private var dialog: AlertDialog? = null


    fun showDialog(context: Context, listener: Listener, name: String = ""){

        val builder = AlertDialog.Builder(context, R.style.AlertDialogCustom)
        binding = NewListDialogBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)

        listener.onStartDi()

        binding.apply {
            edtListName.setText(name)
            edtListName.requestFocus()
            if(name.isNotEmpty()) {
                btnCreateOrSave.text = context.getString(R.string.btnEdit)
                tvTitle.text = context.getString(R.string.newListName)
            }
            // ЗБЕРЕЖЕННЯ ДАННИХ
            edtListName.setOnEditorActionListener { textView, i, keyEvent ->
                saveItem(context, listener)
                true
            }
            btnCreateOrSave.setOnClickListener{
                saveItem(context, listener)
            }

        }
        dialog = builder.create()
        dialog?.window?.setBackgroundDrawable(null)
        dialog?.show()
        dialog?.setOnDismissListener {
            listener.onDismiss()
        }
    }

    private fun saveItem(context: Context, listener: Listener) = with(binding){
        if(edtListName.text.isNullOrEmpty()){
            edtListName.error = context.getString(R.string.edtError)
        } else {
            listener.onClick(edtListName.text.toString().trim())
            dialog?.dismiss()
        }
    }

    interface Listener{
        fun onStartDi()
        fun onClick(name: String)
        fun onDismiss()
    }
}