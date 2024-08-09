package com.deskree.shoppinglist.db

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.deskree.shoppinglist.R
import com.deskree.shoppinglist.databinding.NoteListItemBinding
import com.deskree.shoppinglist.entities.NoteItem
import com.deskree.shoppinglist.utils.HtmlManager
import com.deskree.shoppinglist.utils.TimeManager

class NoteAdapter(private val listener: Listener, private val defPref: SharedPreferences): ListAdapter<NoteItem, NoteAdapter.ItemHolder>(ItemComparator()) {

    class ItemHolder(view: View): RecyclerView.ViewHolder(view){

        private val binding = NoteListItemBinding.bind(view)

        @SuppressLint("SetTextI18n")
        fun setData(note: NoteItem, listener: Listener, defPref: SharedPreferences) = with(binding){
            tvTitle.text = note.title
            if(defPref.getString("note_style_key", "Grid") == "Grid")
                tvDesc.maxLines = 10
            tvDesc.text = HtmlManager.getFromHtml(note.content).trim()
            tvDateTime.text = TimeManager.getTimeFormat(note.dataTime, defPref)
            if(note.changeDateTime != null) {
                tvChangeDateTime.visibility = View.VISIBLE
                tvChangeDateTime.text =
                    "Змінено: \n${TimeManager.getTimeFormat(note.changeDateTime, defPref)}"
            }
            itemView.setOnClickListener{
                listener.onClickItem(note)
            }
            imbDelete.setOnClickListener{
                listener.deleteItem(note.id!!)
            }
        }
        companion object{
            fun create(parent: ViewGroup): ItemHolder{
                return ItemHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.note_list_item, parent, false))
            }
        }
    }

    class ItemComparator: DiffUtil.ItemCallback<NoteItem>(){
        override fun areItemsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position), listener, defPref)
    }
    interface Listener{
        fun deleteItem(id: Int)
        fun onClickItem(note: NoteItem)
    }
}