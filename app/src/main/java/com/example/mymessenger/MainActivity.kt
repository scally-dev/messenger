package com.example.mymessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mymessenger.databinding.ActivityMainBinding
import androidx.appcompat.widget.Toolbar
import com.example.mymessenger.database.AUTH
import com.example.mymessenger.database.initFirebase
import com.example.mymessenger.database.initUser
import com.example.mymessenger.ui.screens.main_list.MainListFragment
import com.example.mymessenger.ui.screens.register.EnterLoginFragment
import com.example.mymessenger.ui.objects.AppDrawer
import com.example.mymessenger.utilits.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    lateinit var mToolbar: Toolbar
    lateinit var mAppDrawer: AppDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        APP_ACTIVITY = this
        initFirebase()
        initUser{
            CoroutineScope(Dispatchers.IO).launch {
                initContacts()
            }
            initFields()
            initFunc()
        }

    }



    override fun onStart() {
        super.onStart()
        AppStates.updateState(AppStates.ONLINE)
    }

    override fun onStop() {
        super.onStop()
        AppStates.updateState(AppStates.OFFLINE)
    }

    private fun initFunc() {
        setSupportActionBar(mToolbar)
        if (AUTH.currentUser!=null){
            mAppDrawer.create()
            replaceFragment(MainListFragment(), false)
        } else {
            replaceFragment(EnterLoginFragment(),false)        }
    }

    private fun initFields() {
        mToolbar = mBinding.mainToolbar
        mAppDrawer = AppDrawer()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        /*if (ContextCompat.checkSelfPermission(APP_ACTIVITY, READ_CONTACTS)== PackageManager.PERMISSION_GRANTED){
            initContacts()
        }*/
    }



}