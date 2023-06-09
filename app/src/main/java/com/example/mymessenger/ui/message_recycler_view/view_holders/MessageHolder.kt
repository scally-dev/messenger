package com.example.mymessenger.ui.message_recycler_view.view_holders

import com.example.mymessenger.ui.message_recycler_view.view.MessageView

interface MessageHolder {
    fun drawMessage(view: MessageView)
    fun onAttach(view: MessageView)
    fun onDetach()
}