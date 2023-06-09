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
import com.example.mymessenger.utilits.downloadAndSetImage

class HolderImageMessage(view: View): RecyclerView.ViewHolder(view) {
    val blocReceivedImageMessage: ConstraintLayout = view.findViewById(R.id.bloc_received_image_message)
    val blocUserImageMessage: ConstraintLayout = view.findViewById(R.id.bloc_user_image_message)
    val chatUserImage: ImageView = view.findViewById(R.id.chat_user_image)
    val chatReceivedImage: ImageView = view.findViewById(R.id.chat_received_image)
    val chatUserImageMessageTime: TextView = view.findViewById(R.id.chat_user_image_message_time)
    val chatReceivedImageMessageTime: TextView = view.findViewById(R.id.chat_received_image_message_time)

    fun drawMessageImage(holder: HolderImageMessage, view: MessageView) {

        if (view.from == CURRENT_UID) {
            holder.blocReceivedImageMessage.visibility = View.GONE
            holder.blocUserImageMessage.visibility = View.VISIBLE
            holder.chatUserImage.downloadAndSetImage(view.fileUrl)
            holder.chatUserImageMessageTime.text =
                view.timeStamp.asTime()
        } else {
            holder.blocReceivedImageMessage.visibility = View.VISIBLE
            holder.blocUserImageMessage.visibility = View.GONE
            holder.chatReceivedImage.downloadAndSetImage(view.fileUrl)
            holder.chatReceivedImageMessageTime.text =
                view.timeStamp.asTime()
        }
    }
}