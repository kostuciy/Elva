package com.example.elva.dto

data class Note(
    val id: Long,
    val title: String,
    val blocks: List<Block>,
    val timestamp: Long = System.currentTimeMillis(),
    val color: NoteColor = NoteColor.DEFAULT) {

    operator fun get(blockId: Long): Block? =
        blocks.find { it.id == blockId }

    companion object {
        val EMPTY_NOTE = Note(
            0, "", listOf(Block.EMPTY_BLOCK), 0
        )
    }

    data class Block(
        val id: Long,
        val text: String,
        val attachment: BlockAttachment? = null
    ) {

        companion object {
            val EMPTY_BLOCK = Block(
                0, "", null
            )
        }

        data class BlockAttachment(
            val type: AttachmentType
        )

        enum class AttachmentType {
            IMAGE, VIDEO, FILE, DRAWING
        }
    }

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

