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

class HolderVoiceMessage(view: View):RecyclerView.ViewHolder(view) {
    val blocReceivedVoiceMessage: ConstraintLayout = view.findViewById(R.id.bloc_received_voice_message)
    val blocUserVoiceMessage: ConstraintLayout = view.findViewById(R.id.bloc_user_voice_message)
    val chatUserVoiceMessageTime: TextView = view.findViewById(R.id.chat_user_voice_message_time)
    val chatReceivedVoiceMessageTime: TextView = view.findViewById(R.id.chat_received_voice_message_time)


    val chatReceivedBtnPlay:ImageView = view.findViewById(R.id.chat_received_btn_play)
    val chatReceivedBtnStop:ImageView = view.findViewById(R.id.chat_received_btn_stop)

    val chatUserBtnPlay:ImageView = view.findViewById(R.id.chat_user_btn_play)
    val chatUserBtnStop:ImageView = view.findViewById(R.id.chat_user_btn_stop)


    fun drawMessageVoice(holder: HolderVoiceMessage, view: MessageView) {
        if (view.from == CURRENT_UID) {
            holder.blocReceivedVoiceMessage.visibility = View.GONE
            holder.blocUserVoiceMessage.visibility = View.VISIBLE
            holder.chatUserVoiceMessageTime.text =
                view.timeStamp.asTime()
        } else {
            holder.blocReceivedVoiceMessage.visibility = View.VISIBLE
            holder.blocUserVoiceMessage.visibility = View.GONE
            holder.chatReceivedVoiceMessageTime.text =
                view.timeStamp.asTime()
        }
    }

}