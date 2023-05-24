package com.example.mymessenger.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mymessenger.MainActivity
import com.example.mymessenger.activities.RegisterActivity
import com.example.mymessenger.utilits.AUTH
import com.example.mymessenger.databinding.FragmentEnterPasswordBinding
import com.example.mymessenger.utilits.AppTextWatcher
import com.example.mymessenger.utilits.replaceActivity
import com.example.mymessenger.utilits.showToast

class EnterPasswordFragment(val mEmail: String) : Fragment() {
    private var _binding: FragmentEnterPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentEnterPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as RegisterActivity).title = mEmail
        binding.registerInputPassword.addTextChangedListener(AppTextWatcher {
                val password = binding.registerInputPassword.text.toString()
                if (password.length == 6) {
                    verifiCode(password)
                }
        })
    }

    private fun verifiCode(password: String) {

        /*val credential = PhoneAuthProvider.getCredential(id, code)
        AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful){
                showToast("Добро пожаловать")
                (activity as RegisterActivity).replaceActivity(MainActivity())
            } else showToast(task.exception?.message.toString())*/
        AUTH.signInWithEmailAndPassword(mEmail, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showToast("Добро пожаловать")
                    (activity as RegisterActivity).replaceActivity(MainActivity())
                } else {
                    showToast(task.exception?.message.toString())
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}