package com.example.mymessenger.ui.screens.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mymessenger.database.*

import com.example.mymessenger.databinding.FragmentEnterPasswordBinding
import com.example.mymessenger.utilits.*
import com.example.mymessenger.utilits.AppStates.Companion.updateState

class EnterPasswordFragment(val login: String, val userExist: Boolean) : Fragment() {
    private var _binding: FragmentEnterPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentEnterPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.enterBtnNext.setOnClickListener {
                val password = binding.registerInputPassword.text.toString()
                if (userExist) {
                    APP_ACTIVITY.title = "$login auth"
                    authUser(password)
                } else {
                    APP_ACTIVITY.title = "$login register"
                    registerUser(password)
                }
        }
    }

    private fun registerUser(password: String) {

        AUTH.createUserWithEmailAndPassword("$login@gmail.com", password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = AUTH.currentUser?.uid.toString()
                    val dateMap = mutableMapOf<String, Any>()
                    dateMap[CHILD_ID] = uid
                    dateMap[CHILD_LOGIN] = login
                    dateMap[CHILD_USERNAME] = login
                    //перенос в регистрацию
                    REF_DATABASE_ROOT.child(NODE_USERNAMES).child(login).setValue(uid)
                    REF_DATABASE_ROOT.child(NODE_LOGINS).child(login).setValue(uid)
                        .addOnFailureListener { showToast(it.message.toString()) }
                        .addOnSuccessListener {

                            REF_DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(dateMap)
                                .addOnSuccessListener {
                                    showToast("Добро пожаловать")
                                    //updateState(AppStates.ONLINE)
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

    private fun authUser(password: String) {

        AUTH.signInWithEmailAndPassword("$login@gmail.com", password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showToast("Добро пожаловать")
                    restartActivity()

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