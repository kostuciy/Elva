package com.example.elva.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.elva.adapter.NoteAdapter
import com.example.elva.adapter.NoteInteractionListener
import com.example.elva.databinding.FragmentNoteListBinding
import com.example.elva.dto.Note
import com.example.elva.viewmodel.NoteViewModel

class NoteListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding =
            FragmentNoteListBinding.inflate(layoutInflater, container, false)

        val viewModel: NoteViewModel by viewModels()

        val recyclerView = binding.noteRecyclerView
        val noteAdapter = NoteAdapter(object : NoteInteractionListener {

            override fun noteChanged(id: Long, newNote: Note): Note {
                TODO("Not yet implemented")
            }

            override fun noteDeleted(id: Long): Note {
                return viewModel.delete(id)
            }
        }).apply { submitList(viewModel.getAll()) }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = noteAdapter
        }

        viewModel.apply {
            noteData.observe(viewLifecycleOwner) { noteList ->
                noteAdapter.submitList(noteList)
            }
        }

        return binding.root
    }

}