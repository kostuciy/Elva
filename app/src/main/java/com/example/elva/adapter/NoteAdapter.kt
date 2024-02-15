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
//    fun noteChanged(id: Long, newNote: Note): Note
    fun noteClicked(note: Note)
    fun noteDeleted(id: Long): Note



    // TODO: implement post interaction
}

class NoteAdapter(private val noteInteractionListener: NoteInteractionListener)
    : ListAdapter<Note, NoteAdapter.NoteViewHolder>(NoteDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NoteListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)

        return NoteViewHolder(binding.root, binding, noteInteractionListener)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getItem(position)
        holder.bind(note)
    }

    class NoteViewHolder(
        itemView: View, private val binding: NoteListItemBinding,
        private val noteInteractionListener: NoteInteractionListener
    ) : ViewHolder(itemView) {
        fun bind(note: Note) {
            binding.apply {
                contentTextView.text = note[0]?.text.orEmpty()
                titleTextView.text = note.title
                deleteButton.setOnClickListener {
                    noteInteractionListener.noteDeleted(note.id)
                }
                root.setOnClickListener {
                    noteInteractionListener.noteClicked(note)
                }
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