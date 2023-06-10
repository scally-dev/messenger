package com.example.mymessenger.ui.screens.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mymessenger.R
import com.example.mymessenger.database.NODE_LOGINS
import com.example.mymessenger.database.REF_DATABASE_ROOT

import com.example.mymessenger.databinding.FragmentEnterLoginBinding
import com.example.mymessenger.utilits.AppValueEventListener
import com.example.mymessenger.utilits.replaceFragment
import com.example.mymessenger.utilits.showToast
import java.util.*


class EnterLoginFragment : Fragment(R.layout.fragment_enter_login) {

    private lateinit var mLogin:String
    //private lateinit var mCallback: EmailAuthProvider.


    private var _binding: FragmentEnterLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentEnterLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.registerBtnNext.setOnClickListener { checkLogin() }
    }

    private fun checkLogin() {
        mLogin = binding.registerInputLogin.text.toString().lowercase(Locale.getDefault())
        if (mLogin.isEmpty()) {
            showToast(getString(R.string.register_toast_enter_login))
        } else {
            REF_DATABASE_ROOT.child(NODE_LOGINS)
                .addListenerForSingleValueEvent(AppValueEventListener {
                    if (it.hasChild(mLogin)) {
                        replaceFragment(EnterPasswordFragment(mLogin, true))
                    } else {
                        replaceFragment(EnterPasswordFragment(mLogin, false))
                    }
                })

        }
    }
    fun change() {

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}