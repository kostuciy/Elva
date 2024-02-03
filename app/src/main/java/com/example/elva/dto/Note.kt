package com.example.elva.dto

data class Note(
    val id: Long,
    val title: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val color: NoteColor = NoteColor.DEFAULT) {

    enum class NoteColor() {
        RED,
        ORANGE,
        YELLOW,
        GREEN,
        BLUE,
        INDIGO,
        VIOLET,
        DEFAULT
    }
}

