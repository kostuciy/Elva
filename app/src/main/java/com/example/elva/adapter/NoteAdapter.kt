package com.example.elva.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.elva.databinding.NoteListItemBinding
import com.example.elva.dto.Note

interface NoteInteractionListener {

}

class NoteAdapter(noteInteractionListener: NoteInteractionListener)
    : ListAdapter<Note, NoteAdapter.NoteViewHolder>(NoteDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NoteListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)

        return NoteViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getItem(position)
        holder.bind(note)
    }

    class NoteViewHolder(itemView: View, private val binding: NoteListItemBinding) : ViewHolder(itemView) {
        fun bind(note: Note) {
            binding.apply {
                contentTextView.text = note.content
                titleTextView.text = note.title
            }
        }
    }
}

object NoteDiffCallback : ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
}