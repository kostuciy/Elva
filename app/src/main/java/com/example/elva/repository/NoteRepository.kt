package com.example.elva.repository

import com.example.elva.dto.Note
import java.lang.IllegalArgumentException

class NoteRepository : Repository<Note> {

    private var testList : List<Note> =
//        emptyList()
        listOf(
            Note(1L, "To do list", "Bebebaba", 123),
            Note(2L, "To do list", "Bebebaba", 123),
            Note(3L, "To do list", "Bebebaba", 123)
        )

    override fun save(note: Note): Note {
        testList = testList + note
        return note
    }

    override fun getAll(): List<Note> = testList

    override fun delete(note: Note): Note {
        testList = testList.filter { it.id != note.id }
        return note
    }

    override fun edit(oldNote: Note, newNote: Note): Note {
        testList = testList.map {
            if (it.id == oldNote.id) newNote.copy (id = oldNote.id)
            else it
        }

        return testList.find { it.id == oldNote.id } ?: throw IllegalArgumentException("incorrect note id")
    }
}