package com.example.elva.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.elva.dto.Note
import com.example.elva.model.NoteState
import com.example.elva.repository.NoteRepository

class NoteViewModel : ViewModel() {

    private val repository = NoteRepository()
    private val _noteState: MutableLiveData<NoteState> by lazy {
        MutableLiveData(NoteState())
    }
    val noteState: LiveData<NoteState>
        get() = _noteState

    init {
        val noteList = getAll()
        _noteState.value = NoteState(
            noteList = noteList,
            empty = noteList.isEmpty()
        )
    }

    private fun sync() {
        val noteList = getAll()
        _noteState.value = _noteState.value?.copy(
            noteList = noteList, empty = noteList.isEmpty()
        ) ?: NoteState(error = true)
    }

    fun toEdited(note: Note) {
        _noteState.value = _noteState.value?.copy(lastEdited = note)
    }
    private fun getAll(): List<Note> = repository.getAll()

    private fun findById(id: Long): Note {
        _noteState.value?.noteList?.find { it.id == id }.let { note ->
            if (note != null) return note
            _noteState.value = _noteState.value?.copy(error = true)
            return Note.EMPTY_NOTE
        }
    }

//    saves note in "lastEdited" field and sets it empty
    fun save() {
        repository.save(noteState.value?.lastEdited ?: Note.EMPTY_NOTE)
        toEdited(Note.EMPTY_NOTE)
        sync()
    }


    fun delete(id: Long): Note {
        val note = repository.delete(findById(id))
        sync()

        return note
    }


//    Todo: delete? (check is save fun does the same)
    fun edit(id: Long, newNote: Note): Note {
        val note = repository.edit(findById(id), newNote)
        sync()

        return note
    }

//    does not update note list, provides updated list of blocks locally
//    (to be used in block recycler view only)
    fun updateBlocksLocal(block: Note.Block, blocks: List<Note.Block>): List<Note.Block> =
        if (blocks.isEmpty() || block.id !in blocks.map { it.id }) blocks + block.copy(
            id = if (blocks.isEmpty()) 1 else blocks.maxOf { it.id } + 1
        )
        else blocks.map { if (it.id == block.id) block else it }

    fun deleteBlockLocal(blockId: Long, blocks: List<Note.Block>): List<Note.Block> =
        blocks.filter { it.id != blockId }

}






