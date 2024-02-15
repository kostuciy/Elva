package com.example.elva.adapter

import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.elva.R
import com.example.elva.databinding.NoteBlockBinding
import com.example.elva.dto.Note
import com.example.elva.dto.Note.Block

interface BlockInteractionListener {

    fun blocksUpdateState(finished: Boolean)

}
class BlockAdapter(private val blockInteractionListener: BlockInteractionListener)
    : ListAdapter<Block, BlockAdapter.BlockViewHolder>(BlockDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlockViewHolder {
        val binding = NoteBlockBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)

        return BlockViewHolder(binding.root, binding, blockInteractionListener, this)
    }

    override fun onBindViewHolder(holder: BlockViewHolder, position: Int) {
        val note = getItem(position)
        holder.bind(note)
    }

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position

    override fun onViewDetachedFromWindow(holder: BlockViewHolder) {
        holder.disableTextWatcher()
    }

    override fun submitList(list: List<Block>?) {
        super.submitList(null)
        super.submitList(list)
        currentEditedList = currentList
    }

    fun submitToSync() {
        submitList(currentEditedList)
    }


    private var currentEditedList: List<Block> = currentList

    fun updateBlocksLocal(block: Block) {
//        val blockIndex =
//            currentEditedList.ifEmpty { null }?.withIndex()?.find { it.value.id == block.id }?.index
//
//        currentEditedList =
//            if (blockIndex != null) currentEditedList.map { if (it.id == block.id) block else it }
//            else currentEditedList + block.copy(
//                id = if (currentEditedList.isEmpty()) 1 else currentEditedList.maxOf { it.id } + 1
//            )
        Log.d("GUG", "Adding block: $block\nto cur list: ${currentEditedList}")
        currentEditedList =
            if (currentEditedList.isEmpty() || block.id !in currentEditedList.map { it.id })
                currentEditedList + block.copy(
                    id = if (currentEditedList.isEmpty()) 1 else currentEditedList.maxOf { it.id } + 1
                )
        else currentEditedList.map { if (it.id == block.id) block else it }
        Log.d("GUG", "Added, current list: ${currentEditedList}")
    }

    fun deleteBlockLocal(blockId: Long) {
        currentEditedList = currentEditedList.filter { it.id != blockId }
    }


    class BlockViewHolder(
        itemView: View, private val binding: NoteBlockBinding,
        private val blockInteractionListener: BlockInteractionListener, // TODO: add funs or remove
        private val adapter: BlockAdapter,
    ) : RecyclerView.ViewHolder(itemView) {

        private var textWatcher: TextWatcher? = null

        fun disableTextWatcher() {
            binding.editText.removeTextChangedListener(textWatcher)
        }

        fun bind(block: Note.Block) {
            binding.apply {

                editText.apply {
                    requestFocus()
                    setText(block.text)
                    doAfterTextChanged {
                        adapter.updateBlocksLocal(block.copy(text = it.toString()))
                    }.let { textWatcher = it }

                }

                root.setOnLongClickListener {
                    PopupMenu(binding.root.context, binding.root).apply {
//                    creating popping block's options
                        menuInflater.inflate(R.menu.block_popup_menu, this.menu)
                        setOnMenuItemClickListener { menuItem ->
                            when (menuItem.itemId) {
                                R.id.action_delete_block -> {
                                    adapter.deleteBlockLocal(block.id)
                                    adapter.submitToSync()
                                    true
                                }

                                else -> TODO()
                            }
                        }
                        show()
                    }
                    true
                }

                if (block.attachment != null) {
                    attachmentEditText.setText("unknown attachment")
                    attachmentGroup.visibility = View.VISIBLE
//                    TODO: add listener to attachment changer
                } else attachmentGroup.visibility = View.GONE
            }
        }
    }
}


object BlockDiffCallback : DiffUtil.ItemCallback<Block>() {
    override fun areItemsTheSame(oldItem: Block, newItem: Block): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Block, newItem: Block): Boolean {
        return oldItem == newItem
    }
}