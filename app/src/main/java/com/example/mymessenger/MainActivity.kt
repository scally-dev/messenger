package com.example.mymessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mymessenger.databinding.ActivityMainBinding
import androidx.appcompat.widget.Toolbar
import com.example.mymessenger.activities.RegisterActivity
import com.example.mymessenger.models.User
import com.example.mymessenger.ui.fragments.ChatsFragment
import com.example.mymessenger.ui.objects.AppDrawer
import com.example.mymessenger.utilits.*
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mToolbar: Toolbar
    lateinit var mAppDrawer: AppDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }

    override fun onStart() {
        super.onStart()
        initFields()
        initFunc()
    }

    private fun initFunc() {
        if (AUTH.currentUser!=null){
            setSupportActionBar(mToolbar)
            mAppDrawer.create()
            replaceFragment(ChatsFragment(), false)
        } else {
            replaceActivity(RegisterActivity())
        }
    }

    private fun initFields() {
        mToolbar = mBinding.mainToolbar
        mAppDrawer = AppDrawer(this, mToolbar)
        initFirebase()
        initUser()
    }

    private fun initUser() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID)
            .addListenerForSingleValueEvent(AppValueEventListener{
                USER = it.getValue(User::class.java) ?: User()
            })
    }
}