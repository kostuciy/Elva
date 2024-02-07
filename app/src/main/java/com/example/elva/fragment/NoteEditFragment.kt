package com.example.elva.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.elva.R
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

        binding.apply {
            viewModel.noteState.value?.lastEdited?.let { clickedNote ->
                if (clickedNote == Note.EMPTY_NOTE) return@let
                titleEditText.setText(clickedNote.title)
                contentEditText.setText(clickedNote.content)
            }

            toolbar.apply {
                inflateMenu(R.menu.note_creation_bar)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.action_save -> {
                            viewModel.toEdited(
                                Note(
                                    0,
                                    titleEditText.text.toString().ifEmpty { "Unnamed Note" },
                                    contentEditText.text.toString()
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