package com.example.elva.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.elva.dto.Note
import com.example.elva.repository.NoteRepository
import java.lang.IllegalArgumentException

class NoteViewModel : ViewModel() {

    private val repository = NoteRepository()
    private val _noteData: MutableLiveData<List<Note>> by lazy {
        MutableLiveData(emptyList())
    }
    val noteData: LiveData<List<Note>>
        get() = _noteData

    init {
        _noteData.value = getAll()
    }

    fun getAll(): List<Note> = repository.getAll()

    private fun findById(id: Long): Note =
        _noteData.value?.find { it.id == id } ?: throw IllegalArgumentException("no post found")


    fun save(id: Long) {
        val note = repository.save(findById(id))
        _noteData.value = getAll()
    }


    fun delete(id: Long): Note {
        val note = repository.delete(findById(id))
        _noteData.value = getAll()

        return note
    }


    fun edit(id: Long, newNote: Note): Note {
        val note = repository.edit(findById(id), newNote)
        _noteData.value = getAll()

        return note
    }

}