package com.deskree.shoppinglist.utils

import android.content.Intent
import com.deskree.shoppinglist.entities.NoteItem
import com.deskree.shoppinglist.entities.ShopListItem

object ShareHelper {
    fun shareShopList(shopList: List<ShopListItem>, listName: String): Intent{
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plane"
        intent.apply {
            putExtra(Intent.EXTRA_TEXT, makeShareTextList(shopList, listName))
        }
        return intent
    }

    fun shareNote(note: NoteItem): Intent{
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plane"
        intent.apply {
            putExtra(Intent.EXTRA_TEXT, makeShareTextNote(note))
        }
        return intent
    }

    private fun makeShareTextList(shopList: List<ShopListItem>, listName: String): String{
        val sBuilder = StringBuilder()
        sBuilder.append("<<$listName>>")
        sBuilder.append("\n")
        var counter = 0
        shopList.forEach {
            sBuilder.append("${++counter} -- ${it.name}")
            if(it.itemInfo.isNotEmpty()) {
                sBuilder.append(" --> [${it.itemInfo}]")
            }
            sBuilder.append("\n")
        }
        return sBuilder.toString()
    }

    private fun makeShareTextNote(note: NoteItem): String{
        val sBuilder = StringBuilder()
        sBuilder.append("<<${note.title}>>")
        if(note.content.isNotEmpty()) {
            sBuilder.append("\n\n")
            sBuilder.append(HtmlManager.getFromHtml(note.content).trim())
        }

        return sBuilder.toString()
    }
}