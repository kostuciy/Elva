package com.example.elva.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.example.elva.R
import com.example.elva.adapter.BlockAdapter
import com.example.elva.adapter.BlockInteractionListener
import com.example.elva.databinding.FragmentNoteEditBinding
import com.example.elva.dto.Note
import com.example.elva.viewmodel.NoteViewModel

class NoteEditFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding =
            FragmentNoteEditBinding.inflate(layoutInflater, container, false)

        val viewModel: NoteViewModel by activityViewModels()
        val navController = findNavController()
        val blockAdapter = BlockAdapter(object : BlockInteractionListener { // TODO: use somewhere or delete

            override fun blocksUpdateState(finished: Boolean) {
            }
        })



        binding.apply {
            blockRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = blockAdapter
                addOnScrollListener(object : OnScrollListener() {

                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        if (dy > 0) {
                            titleEditText.visibility = View.GONE
                        } else {
                            if (!titleEditText.isVisible) titleEditText.visibility = View.VISIBLE
                        }
                    }
                })
            }

            constraintLayout.setOnClickListener {
                blockAdapter.updateBlocksLocal(Note.Block.EMPTY_BLOCK)
                blockAdapter.submitToSync()
            }

            viewModel.noteState.value?.lastEdited?.let { clickedNote ->
                if (clickedNote == Note.EMPTY_NOTE) return@let
                titleEditText.setText(clickedNote.title)
                blockAdapter.submitList(clickedNote.blocks)

            }

            toolbar.apply {
                inflateMenu(R.menu.note_creation_bar)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.action_save -> {
                            blockAdapter.submitToSync()
                            viewModel.toEdited(
                                Note(
                                    viewModel.noteState.value?.lastEdited?.id ?: 0L,
                                    titleEditText.text.toString().ifEmpty { "Unnamed Note" },
                                    blockAdapter.currentList
                                )
                            )
                            viewModel.save()
                            navController.navigateUp()
                            true
                        }
                        else -> false
                    }
                }
            }
        }

        return binding.root
    }
}