package com.example.mymessenger.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.mymessenger.R
import com.example.mymessenger.databinding.ActivityRegisterBinding
import com.example.mymessenger.ui.fragments.EnterLoginFragment
import com.example.mymessenger.utilits.initFirebase
import com.example.mymessenger.utilits.replaceFragment

class RegisterActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityRegisterBinding
    private lateinit var mToolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initFirebase()
    }
    override fun onStart() {
        super.onStart()
        mToolbar = mBinding.registerToolbar
        setSupportActionBar(mToolbar)
        title = getString(R.string.register_title_your_login)
        replaceFragment(EnterLoginFragment(), false)
    }
}