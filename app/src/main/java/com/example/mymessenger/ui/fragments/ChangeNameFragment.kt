package com.example.mymessenger.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.mymessenger.MainActivity
import com.example.mymessenger.R
import com.example.mymessenger.databinding.FragmentChangeNameBinding
import com.example.mymessenger.databinding.FragmentEnterLoginBinding
import com.example.mymessenger.utilits.*



class ChangeNameFragment : BaseChangeFragment(R.layout.fragment_change_name) {

    private var _binding: FragmentChangeNameBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentChangeNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val fullnameList = USER.username.split(" ")
        if (fullnameList.size>1) {
            binding.settingsInputName.setText(fullnameList[0])
            binding.settingsInputSurname.setText(fullnameList[1])
        } else binding.settingsInputName.setText(fullnameList[0])
    }



    override fun change() {
        val name = binding.settingsInputName.text.toString()
        val surname = binding.settingsInputSurname.text.toString()
        if (name.isEmpty()){
            showToast(getString(R.string.settings_toast_name_is_empty))
        } else {
            val fullname = "$name $surname"
            REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_USERNAME)
                .setValue(fullname).addOnCompleteListener {
                    if (it.isSuccessful){
                        showToast(getString(R.string.toast_data_update))
                        USER.username = fullname
                        fragmentManager?.popBackStack()
                    }
                }
        }
    }
}