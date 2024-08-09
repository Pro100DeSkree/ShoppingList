package com.deskree.shoppinglist.dialogs

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.deskree.shoppinglist.R
import com.deskree.shoppinglist.databinding.DeleteDialogBinding
import com.deskree.shoppinglist.databinding.ExitDialogBinding


object ExitDialog {
    fun showDialog(context: Context, listener: Listener){
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context, R.style.AlertDialogCustom)
        val binding = ExitDialogBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)

        binding.apply {
            btnExitOK.setOnClickListener{
                listener.exit()
                dialog?.dismiss()
            }
            btnExitCancel.setOnClickListener {
                dialog?.dismiss()
            }
            btnExitAndSave.setOnClickListener {
                listener.save()
                listener.exit()
                dialog?.dismiss()
            }
        }
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(null)
        dialog.show()
    }
    interface Listener{
        fun exit()
        fun save()
    }
}