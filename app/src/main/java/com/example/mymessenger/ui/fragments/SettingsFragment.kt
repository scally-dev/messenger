package com.example.mymessenger.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Menu
import android.view.MenuInflater
import com.example.mymessenger.R
import com.example.mymessenger.databinding.FragmentSettingsBinding


class SettingsFragment : BaseFragment(R.layout.fragment_settings) {
    private lateinit var mBinding: FragmentSettingsBinding

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)

    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.settings_action_menu, menu)
    }

}