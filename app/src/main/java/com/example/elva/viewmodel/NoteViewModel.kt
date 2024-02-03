package com.example.elva.viewmodel

import androidx.lifecycle.ViewModel
import com.example.elva.dto.Note
import com.example.elva.repository.NoteRepository
import java.lang.IllegalArgumentException

class NoteViewModel : ViewModel() {

    private val repository = NoteRepository()
    private var noteData: List<Note> = emptyList()

    init {
        noteData = getAll()
    }

    fun getAll(): List<Note> = repository.getAll()

    private fun findById(id: Long): Note =
        noteData.find { it.id == id } ?: throw IllegalArgumentException("no post found")

    fun save(id: Long) = repository.save(
        findById(id)
    )

    fun delete(id: Long): Note = repository.delete(
        findById(id)
    )

    fun edit(id: Long, newNote: Note): Note = repository.edit(
        findById(id), newNote
    )
}