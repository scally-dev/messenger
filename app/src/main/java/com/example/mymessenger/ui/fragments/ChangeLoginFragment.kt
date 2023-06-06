package com.example.mymessenger.ui.fragments

import android.os.Bundle
import android.view.*
import com.example.mymessenger.MainActivity
import com.example.mymessenger.R
import com.example.mymessenger.databinding.FragmentChangeLoginBinding
import com.example.mymessenger.utilits.*
import java.util.*


class ChangeLoginFragment : BaseFragment(R.layout.fragment_change_login) {
    lateinit var mNewLogin: String

    private var _binding: FragmentChangeLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentChangeLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        binding.settingsInputLogin.setText(USER.login)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        (activity as MainActivity).menuInflater.inflate(R.menu.settings_menu_confirm, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_confirm_change -> change()
        }
        return true
    }

    private fun change() {
        mNewLogin = binding.settingsInputLogin.text.toString().lowercase(Locale.getDefault())
        if (mNewLogin.isEmpty()){
            showToast("Поле пустое")
        } else {
            REF_DATABASE_ROOT.child(NODE_LOGINS)
                .addListenerForSingleValueEvent(AppValueEventListener{
                    if (it.hasChild(mNewLogin)){
                        showToast("Такой пользователь уже существует")
                    } else{
                        changeLogin()
                    }
                })

        }
    }

    private fun changeLogin() {

        REF_DATABASE_ROOT.child(NODE_LOGINS).child(mNewLogin).setValue(UID)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    updateCurrentLogin()
                }
            }
    }

    private fun updateCurrentLogin() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_LOGIN)
            .setValue(mNewLogin)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    showToast(getString(R.string.toast_data_update))
                    deleteOldLogin()
                } else {
                    showToast(it.exception?.message.toString())
                }
            }
    }

    private fun deleteOldLogin() {
        REF_DATABASE_ROOT.child(NODE_LOGINS).child(USER.login).removeValue()
            .addOnCompleteListener {
                if (it.isSuccessful){
                    showToast(getString(R.string.toast_data_update))
                    fragmentManager?.popBackStack()
                    USER.login = mNewLogin
                } else {
                    showToast(it.exception?.message.toString())
                }
            }
    }
}