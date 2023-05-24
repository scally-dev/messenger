package com.example.mymessenger.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.mymessenger.R
import com.example.mymessenger.databinding.FragmentEnterPasswordBinding
import com.example.mymessenger.utilits.AppTextWatcher
import com.example.mymessenger.utilits.showToast

class EnterPasswordFragment : Fragment() {
    private var _binding: FragmentEnterPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentEnterPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.registerInputPassword.addTextChangedListener(AppTextWatcher {
                val string = binding.registerInputPassword.text.toString()
                if (string.length == 6) {
                    verifiCode()
                }
        })
    }

    private fun verifiCode() {
        showToast("Ok")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}