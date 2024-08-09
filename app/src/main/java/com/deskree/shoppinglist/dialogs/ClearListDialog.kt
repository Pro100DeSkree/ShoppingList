package com.deskree.shoppinglist.dialogs

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.deskree.shoppinglist.R
import com.deskree.shoppinglist.databinding.ClearListDialogBinding
import com.deskree.shoppinglist.databinding.DeleteDialogBinding


object ClearListDialog {
    fun showDialog(context: Context, listener: Listener){
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context, R.style.AlertDialogCustom)
        val binding = ClearListDialogBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)

        binding.apply {
            btnDeleteOK.setOnClickListener{
                    listener.onClick()
                    dialog?.dismiss()
            }
            btnDeleteCancel.setOnClickListener {
                dialog?.dismiss()
            }
        }
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(null)
        dialog.show()
    }
    interface Listener{
        fun onClick()
    }
}