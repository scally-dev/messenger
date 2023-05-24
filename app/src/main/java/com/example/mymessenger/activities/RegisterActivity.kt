package com.example.mymessenger.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.mymessenger.R
import com.example.mymessenger.databinding.ActivityRegisterBinding
import com.example.mymessenger.ui.fragments.EnterLoginFragment

class RegisterActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityRegisterBinding
    private lateinit var mToolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }
    override fun onStart() {
        super.onStart()
        mToolbar = mBinding.registerToolbar
        setSupportActionBar(mToolbar)
        title = getString(R.string.register_title_your_login)
        supportFragmentManager.beginTransaction()
            .add(R.id.registerDataContainer, EnterLoginFragment ())
            .commit()
    }
}