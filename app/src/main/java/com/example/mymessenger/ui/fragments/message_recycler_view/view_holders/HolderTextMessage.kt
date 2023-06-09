package com.example.mymessenger.ui.fragments.message_recycler_view.view_holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.mymessenger.R
import com.example.mymessenger.database.CURRENT_UID
import com.example.mymessenger.ui.fragments.message_recycler_view.view.MessageView
import com.example.mymessenger.utilits.asTime

class HolderTextMessage(view:View):RecyclerView.ViewHolder(view) {
    val blocUserMessage: ConstraintLayout = view.findViewById(
        R.id.bloc_user_message)
    val chatUserMessage: TextView = view.findViewById(R.id.chat_user_message)
    val chatUserMessageTime: TextView = view.findViewById(R.id.chat_user_message_time)
    val blocReceivedMessage: ConstraintLayout = view.findViewById(R.id.bloc_received_message)
    val chatReceivedMessage: TextView = view.findViewById(R.id.chat_received_message)
    val chatReceivedMessageTime: TextView = view.findViewById(R.id.chat_received_message_time)

    fun drawMessageText(holder: HolderTextMessage, view: MessageView) {

        if (view.from == CURRENT_UID) {
            holder.blocUserMessage.visibility = View.VISIBLE
            holder.blocReceivedMessage.visibility = View.GONE
            holder.chatUserMessage.text = view.text
            holder.chatUserMessageTime.text =
                view.timeStamp.asTime()
        } else {
            holder.blocUserMessage.visibility = View.GONE
            holder.blocReceivedMessage.visibility = View.VISIBLE
            holder.chatReceivedMessage.text = view.text
            holder.chatReceivedMessageTime.text =
                view.timeStamp.asTime()
        }
    }
}