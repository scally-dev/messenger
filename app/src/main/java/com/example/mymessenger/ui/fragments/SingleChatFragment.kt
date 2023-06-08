package com.example.mymessenger.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.mymessenger.R
import com.example.mymessenger.databinding.FragmentSettingsBinding
import com.example.mymessenger.databinding.FragmentSingleChatBinding
import com.example.mymessenger.models.CommonModel
import com.example.mymessenger.utilits.APP_ACTIVITY


class SingleChatFragment(contact: CommonModel) : BaseFragment(R.layout.fragment_single_chat) {

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.mToolbar.findViewById<ConstraintLayout>(R.id.toolbar_info).visibility = View.VISIBLE
    }

    override fun onPause() {
        super.onPause()
        APP_ACTIVITY.mToolbar.findViewById<ConstraintLayout>(R.id.toolbar_info).visibility = View.GONE

    }
}