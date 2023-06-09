package com.example.mymessenger.ui.fragments

import androidx.fragment.app.Fragment
import com.example.mymessenger.R
import com.example.mymessenger.databinding.FragmentChatsBinding
import com.example.mymessenger.utilits.APP_ACTIVITY


class MainFragment : Fragment(R.layout.fragment_chats) {
    // TODO: Rename and change types of parameters
    private lateinit var mBinding: FragmentChatsBinding

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "myMessenger"
        APP_ACTIVITY.mAppDrawer.enableDrawer()

    }


}