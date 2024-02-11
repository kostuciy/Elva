package com.example.elva.repository

import com.example.elva.dto.Note
import java.lang.IllegalArgumentException

class NoteRepository : Repository<Note> {

    private var testList : List<Note> =
        emptyList()

    override fun save(note: Note): Note {

        testList = if (note.id == 0L) testList + note.copy(
            id = if (testList.isEmpty()) 1 else testList.maxOf { it.id } + 1
        )
        else testList.map { if (it.id == note.id) note else it }

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

//    fun editBlock(block: Note.Block, note: Note): Note {
//        val updatedBlocks = if (block.id in testList.map { it.id })
//            note.blocks.map { if (it.id == block.id) block else it }
//        else listOf(block.copy(id = note.blocks.maxOf { it.id } + 1)) + note.blocks
//
//        testList = testList.map { if (it.id == note.id) note.copy (
//                blocks = updatedBlocks
//            ) else it
//        }
//
//        return testList.find { it.id == note.id } ?: throw IllegalArgumentException("incorrect note id")
//    }
}