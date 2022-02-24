package com.example.mynotes.ui.main

import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.databinding.NoteSelectionViewHolderBinding
import com.example.mynotes.models.NoteDetail

class NoteSelectionRecyclerViewAdapter(private val notes: MutableList<NoteDetail>,
                                       val clickListener: NoteSelectionRecyclerViewClickListener, val holdClickListener: NoteSelectionRecyclerViewClickListener) : RecyclerView.Adapter<NoteSelectionViewHolder>() {

    interface NoteSelectionRecyclerViewClickListener {
        fun clickNoteTitle(note: NoteDetail)
        fun HoldNameTitle(note: NoteDetail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteSelectionViewHolder {
        val binding = NoteSelectionViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteSelectionViewHolder(binding);
    }

    override fun onBindViewHolder(holder: NoteSelectionViewHolder, position: Int) {
        holder.binding.itemNum.text = (position + 1).toString()
        holder.binding.itemName.text = notes[position].name
        holder.itemView.setOnClickListener {
            clickListener.clickNoteTitle(notes[position])
        }
        holder.itemView.setOnLongClickListener{
            holdClickListener.HoldNameTitle(notes[position])
            true
        }
    }

    override fun getItemCount() = notes.size

    fun notesUpdated() {
        Log.d(ContentValues.TAG, notes.size.toString())
        notifyItemInserted(notes.size-1)
    }
    fun notesRemove(posRemove : Int){
        Log.d(ContentValues.TAG, notes.size.toString())
        notifyItemRemoved(posRemove)

    }
}
