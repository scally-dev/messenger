package com.example.mymessenger.ui.fragments.single_chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mymessenger.R
import com.example.mymessenger.databinding.MessageItemBinding
import com.example.mymessenger.models.CommonModel
import com.example.mymessenger.utilits.CURRENT_UID
import com.example.mymessenger.utilits.DiffUtilCallback
import com.example.mymessenger.utilits.asTime

class SingleChatAdapter : RecyclerView.Adapter<SingleChatAdapter.SingleChatHolder>() {

    private var mListMessagesCache = mutableListOf<CommonModel>()
    private lateinit var mDiffResult: DiffUtil.DiffResult

    inner class SingleChatHolder(private val binding: MessageItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val blocUserMessage: ConstraintLayout = binding.blocUserMessage
        val chatUserMessage: TextView = binding.chatUserMessage
        val chatUserMessageTime: TextView = binding.chatUserMessageTime

        val blocReceivedMessage: ConstraintLayout = binding.blocReceivedMessage
        val chatReceivedMessage: TextView = binding.chatReceivedMessage
        val chatReceivedMeessageTime: TextView = binding.chatReceivedMessageTime
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleChatHolder {
        val binding = MessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SingleChatHolder(binding)
    }

    override fun getItemCount(): Int = mListMessagesCache.size

    override fun onBindViewHolder(holder: SingleChatHolder, position: Int) {
        val currentMessage = mListMessagesCache[position]
        if (currentMessage.from == CURRENT_UID) {
            holder.blocUserMessage.visibility = View.VISIBLE
            holder.blocReceivedMessage.visibility = View.GONE
            holder.chatUserMessage.text = currentMessage.text
            holder.chatUserMessageTime.text = currentMessage.timeStamp.toString().asTime()
        } else {
            holder.blocUserMessage.visibility = View.GONE
            holder.blocReceivedMessage.visibility = View.VISIBLE
            holder.chatReceivedMessage.text = currentMessage.text
            holder.chatReceivedMeessageTime.text = currentMessage.timeStamp.toString().asTime()
        }
    }

    fun addItemToBottom(item: CommonModel,
                        onSuccess: () -> Unit){
        if (!mListMessagesCache.contains(item)) {
            mListMessagesCache.add(item)
            notifyItemInserted(mListMessagesCache.size)
        }
        onSuccess()
    }

    fun addItemToTop(item: CommonModel,
                     onSuccess: () -> Unit) {
        if (!mListMessagesCache.contains(item)) {
            mListMessagesCache.add(item)
            mListMessagesCache.sortBy { it.timeStamp.toString() }
            notifyItemInserted(0)
        }
        onSuccess()
    }
}