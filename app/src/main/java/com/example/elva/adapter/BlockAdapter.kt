package com.example.elva.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.elva.R
import com.example.elva.databinding.NoteBlockBinding
import com.example.elva.dto.Note
import com.example.elva.dto.Note.Block
import com.jakewharton.rxbinding4.widget.textChangeEvents
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import java.util.concurrent.TimeUnit

interface BlockInteractionListener {

    fun blockDeleted(block: Block): List<Block>

    fun blockChanged(block: Block)

}

class BlockAdapter(private val blockInteractionListener: BlockInteractionListener)
    : ListAdapter<Block, BlockAdapter.BlockViewHolder>(BlockDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlockViewHolder {
        val binding = NoteBlockBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)

        return BlockViewHolder(binding.root, binding, blockInteractionListener, ::submitList)
    }

    override fun onBindViewHolder(holder: BlockViewHolder, position: Int) {
        val note = getItem(position)
        holder.bind(note)
    }

    class BlockViewHolder(
        itemView: View, private val binding: NoteBlockBinding,
        private val blockInteractionListener: BlockInteractionListener,
        private val updateCallback: (blockList: List<Block>) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        fun bind(block: Note.Block) {
            binding.apply {
//                when changing block's text in edit view nothing saves to recycler view list
//                (changes apply only to edit view) so after every block change blockList of note
//                saves to currentBlocklist in fragment (updates by interaction listener) and
//                then saves to VM and rep only when save button pressed

                editText.apply {
                    setText(block.text)
                    textChanges().debounce(350, TimeUnit.MILLISECONDS).subscribe { textChanged ->
                        blockInteractionListener.blockChanged(
                            block.copy(text = textChanged.toString())
                        )
                    }

                }

                root.setOnLongClickListener {
                    PopupMenu(binding.root.context, binding.root).apply {
//                    creating popping block options
                        menuInflater.inflate(R.menu.block_popup_menu, this.menu)
                        setOnMenuItemClickListener { menuItem ->
                            when (menuItem.itemId) {
                                R.id.action_delete_block -> {
                                    val newList = blockInteractionListener.blockDeleted(block)
                                    updateCallback(newList)
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