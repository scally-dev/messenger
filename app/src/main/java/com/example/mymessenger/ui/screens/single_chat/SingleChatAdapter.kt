package com.example.mymessenger.ui.screens.single_chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.mymessenger.database.CURRENT_UID

import com.example.mymessenger.models.CommonModel
import com.example.mymessenger.ui.message_recycler_view.view.MessageView
import com.example.mymessenger.ui.message_recycler_view.view_holders.*
import com.example.mymessenger.utilits.*

class SingleChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mListMessagesCache = mutableListOf<MessageView>()
    private var mListHolders = mutableListOf<MessageHolder>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AppHolderFactory.getHolder(parent, viewType)
    }
    override fun getItemViewType(position: Int): Int {
        return mListMessagesCache[position].getTypeView()
    }

    override fun getItemCount(): Int = mListMessagesCache.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MessageHolder).drawMessage(mListMessagesCache[position])
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        (holder as MessageHolder).onAttach(mListMessagesCache[holder.adapterPosition])
        mListHolders.add((holder as MessageHolder))
        super.onViewAttachedToWindow(holder)

    }
    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        (holder as MessageHolder).onDetach()
        mListHolders.remove((holder as MessageHolder))
        super.onViewDetachedFromWindow(holder)
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
    fun onDestroy() {
        mListHolders.forEach {
            it.onDetach()
        }
    }
}