package com.example.mymessenger.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mymessenger.R
import com.example.mymessenger.database.USER
import com.example.mymessenger.database.setBioToDatabase
import com.example.mymessenger.databinding.FragmentChangeBioBinding
import com.example.mymessenger.ui.screens.BaseChangeFragment


class ChangeBioFragment : BaseChangeFragment(R.layout.fragment_change_bio) {

    private var _binding: FragmentChangeBioBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentChangeBioBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onResume() {
        super.onResume()
        binding.settingsInputBio.setText(USER.bio)
    }

    override fun change() {
        super.change()
        val newBio = binding.settingsInputBio.text.toString()
        setBioToDatabase(newBio)
    }
}