package com.example.elva.model

import com.example.elva.dto.Note


data class NoteState(
    val noteList: List<Note> = emptyList(),
    val lastEdited: Note = Note.EMPTY_NOTE,
    val empty: Boolean = true,
    override val loading: Boolean = false,
    override val error: Boolean = false
) : State
