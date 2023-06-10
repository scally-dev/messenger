package com.example.mymessenger.ui.screens.settings

import android.os.Bundle
import android.view.*
import com.example.mymessenger.R
import com.example.mymessenger.database.*
import com.example.mymessenger.databinding.FragmentChangeLoginBinding
import com.example.mymessenger.ui.screens.base.BaseChangeFragment
import com.example.mymessenger.utilits.*
import java.util.*


class ChangeLoginFragment : BaseChangeFragment(R.layout.fragment_change_login) {
    lateinit var mNewLogin: String

    private var _binding: FragmentChangeLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentChangeLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.settingsInputLogin.setText(USER.login)
    }

    override fun change() {
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

        REF_DATABASE_ROOT.child(NODE_LOGINS).child(mNewLogin).setValue(CURRENT_UID)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    updateCurrentLogin(mNewLogin)
                }
            }
    }




}