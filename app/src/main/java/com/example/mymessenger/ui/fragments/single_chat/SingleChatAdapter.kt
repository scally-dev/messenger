package com.example.mymessenger.ui.fragments.single_chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.mymessenger.database.CURRENT_UID

import com.example.mymessenger.models.CommonModel
import com.example.mymessenger.ui.fragments.message_recycler_view.view.MessageView
import com.example.mymessenger.ui.fragments.message_recycler_view.view_holders.AppHolderFactory
import com.example.mymessenger.ui.fragments.message_recycler_view.view_holders.HolderImageMessage
import com.example.mymessenger.ui.fragments.message_recycler_view.view_holders.HolderTextMessage
import com.example.mymessenger.ui.fragments.message_recycler_view.view_holders.HolderVoiceMessage
import com.example.mymessenger.utilits.*

class SingleChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mListMessagesCache = mutableListOf<MessageView>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AppHolderFactory.getHolder(parent, viewType)
    }
    override fun getItemViewType(position: Int): Int {
        return mListMessagesCache[position].getTypeView()
    }

    override fun getItemCount(): Int = mListMessagesCache.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HolderImageMessage -> holder.drawMessageImage(holder, mListMessagesCache[position])
            is HolderTextMessage -> holder.drawMessageText(holder, mListMessagesCache[position])
            is HolderVoiceMessage -> holder.drawMessageVoice(holder, mListMessagesCache[position])
            else -> {
            }
        }
    }

    fun addItemToBottom(item: MessageView,
                        onSuccess: () -> Unit){
        if (!mListMessagesCache.contains(item)) {
            mListMessagesCache.add(item)
            notifyItemInserted(mListMessagesCache.size)
        }
        onSuccess()
    }

    fun addItemToTop(item: MessageView,
                     onSuccess: () -> Unit) {
        if (!mListMessagesCache.contains(item)) {
            mListMessagesCache.add(item)
            mListMessagesCache.sortBy { it.timeStamp.toString() }
            notifyItemInserted(0)
        }
        onSuccess()
    }
}