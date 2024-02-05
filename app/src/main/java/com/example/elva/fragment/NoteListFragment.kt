package com.example.elva.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.elva.R
import com.example.elva.adapter.NoteAdapter
import com.example.elva.adapter.NoteInteractionListener
import com.example.elva.databinding.FragmentNoteListBinding
import com.example.elva.dto.Note
import com.example.elva.viewmodel.NoteViewModel
import java.lang.Exception
import java.lang.IllegalArgumentException

class NoteListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding =
            FragmentNoteListBinding.inflate(layoutInflater, container, false)

        val viewModel: NoteViewModel by activityViewModels()
        val navController = findNavController()
        val noteAdapter = NoteAdapter(object : NoteInteractionListener {

            override fun noteChanged(id: Long, newNote: Note): Note {
                TODO("Not yet implemented")
            }

            override fun noteDeleted(id: Long): Note {
                return viewModel.delete(id)
            }
        }).apply {
            submitList(viewModel.noteState.value?.noteList ?: throw Exception("no value in LD"))
        }

        binding.apply {
            noteRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = noteAdapter
            }
            createButton.setOnClickListener {
                navController.navigate(R.id.action_mainFragment_to_noteCreationFragment)
            }
        }

        viewModel.apply {
            noteState.observe(viewLifecycleOwner) { noteState ->
//                TODO: redo
                if (noteState.error) throw IllegalArgumentException("state error") // TODO: text instead of ex.
                    //noteState.empty -> TODO: text on screen
                    //noteState.loading -> TODO: text on screen
                noteAdapter.submitList(noteState.noteList)
            }
        }

        return binding.root
    }

}