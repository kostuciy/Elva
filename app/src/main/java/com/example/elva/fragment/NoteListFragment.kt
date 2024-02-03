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
import com.example.elva.viewmodel.NoteViewModel

class NoteListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding =
            FragmentNoteListBinding.inflate(layoutInflater, container, false)

        val viewModel: NoteViewModel by viewModels()

//        recyclerView = findViewById(R.id.recyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.adapter = ItemAdapter(items)
        val recyclerView = binding.noteRecyclerView
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = NoteAdapter(object : NoteInteractionListener {
//                TODO: implement LiveData,
            }).apply { submitList(viewModel.getAll()) }
        }

        return binding.root
    }

}