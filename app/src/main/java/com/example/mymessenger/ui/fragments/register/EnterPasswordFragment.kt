package com.example.mymessenger.ui.fragments.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mymessenger.MainActivity

import com.example.mymessenger.databinding.FragmentEnterPasswordBinding
import com.example.mymessenger.utilits.*

class EnterPasswordFragment(val login: String) : Fragment() {
    private var _binding: FragmentEnterPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentEnterPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        APP_ACTIVITY.title = login
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
        AUTH.signInWithEmailAndPassword("$login@gmail.com", password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = AUTH.currentUser?.uid.toString()
                    val dateMap = mutableMapOf<String, Any>()
                    dateMap[CHILD_ID] = uid
                    dateMap[CHILD_LOGIN] = login
                    dateMap[CHILD_USERNAME] = uid
                    //перенос в регистрацию
                    REF_DATABASE_ROOT.child(NODE_LOGINS).child(login).setValue(uid)
                        .addOnFailureListener { showToast(it.message.toString()) }
                        .addOnSuccessListener {

                            REF_DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(dateMap)
                                .addOnSuccessListener {
                                        showToast("Добро пожаловать")
                                        restartActivity()
                                    }
                                .addOnFailureListener{
                                    showToast(it.message.toString())
                                }
                        }

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