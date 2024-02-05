package com.example.elva.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.elva.databinding.FragmentNoteCreationBinding
import com.example.elva.dto.Note
import com.example.elva.viewmodel.NoteViewModel

class NoteCreationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding =
            FragmentNoteCreationBinding.inflate(layoutInflater, container, false)

        val viewModel: NoteViewModel by activityViewModels()
        val navController = findNavController()

        binding.apply {
            saveButton.setOnClickListener {
                viewModel.toEdited(
                    Note(0, titleEditText.text.toString(), contentEditText.text.toString())
                )
                viewModel.save()

                navController.navigateUp()
            }
        }

        return binding.root
    }

}