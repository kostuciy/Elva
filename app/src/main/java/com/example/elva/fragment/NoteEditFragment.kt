package com.example.elva.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
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
        var currentBlockList = viewModel.noteState.value?.lastEdited?.blocks ?: emptyList()
        val blockAdapter = BlockAdapter(object : BlockInteractionListener {

            override fun blockDeleted(block: Note.Block): List<Note.Block> {
                currentBlockList = viewModel.deleteBlockLocal(block.id, currentBlockList)
                return currentBlockList
            }

            override fun blockChanged(block: Note.Block) {
                currentBlockList =
                    viewModel.updateBlocksLocal(block, currentBlockList)
            }

        })


        binding.apply {
            blockRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = blockAdapter
            }

            constraintLayout.setOnClickListener {
                val newList =
                    viewModel.updateBlocksLocal(Note.Block.EMPTY_BLOCK, blockAdapter.currentList)
                blockAdapter.submitList(newList)
                currentBlockList = blockAdapter.currentList
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
                            viewModel.toEdited(
                                Note(
                                    viewModel.noteState.value?.lastEdited?.id ?: 0L,
                                    titleEditText.text.toString().ifEmpty { "Unnamed Note" },
                                    currentBlockList
                                )
                            )
                            viewModel.save()
                            navController.navigateUp()
                        }
                        else -> false
                    }
                }
            }
        }

        return binding.root
    }
}