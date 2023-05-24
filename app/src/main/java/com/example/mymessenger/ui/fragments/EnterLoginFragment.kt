package com.example.mymessenger.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mymessenger.MainActivity
import com.example.mymessenger.R
import com.example.mymessenger.activities.RegisterActivity
import com.example.mymessenger.utilits.AUTH
import com.example.mymessenger.utilits.replaceActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.concurrent.TimeUnit

import com.example.mymessenger.databinding.FragmentEnterLoginBinding
import com.example.mymessenger.utilits.replaceFragment
import com.example.mymessenger.utilits.showToast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.FirebaseAuthEmailException



class EnterLoginFragment : Fragment(R.layout.fragment_enter_login) {

    private lateinit var mEmail:String
    //private lateinit var mCallback: EmailAuthProvider.


    private var _binding: FragmentEnterLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentEnterLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerBtnNext.setOnClickListener { sendCode() }
    }

    private fun sendCode() {
        if (binding.registerInputLogin.text.toString().isEmpty()) {
            showToast(getString(R.string.register_toast_enter_login))
        } else {
            mEmail = binding.registerInputLogin.text.toString()
            replaceFragment(EnterPasswordFragment(mEmail))
        }
    }
    private fun authUser() {


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}