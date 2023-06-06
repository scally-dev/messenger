package com.example.mymessenger.ui.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.example.mymessenger.R
import com.example.mymessenger.activities.RegisterActivity
import com.example.mymessenger.databinding.FragmentSettingsBinding
import com.example.mymessenger.utilits.*
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView


class SettingsFragment : BaseFragment(R.layout.fragment_settings) {
    private lateinit var mBinding: FragmentSettingsBinding

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        initFields()
    }

    private fun initFields() {
        binding.settingsBio.text = USER.bio
        binding.settingsUsername.text = USER.username
        binding.settingsStatus.text = USER.status
        binding.settingsLogin.text = "@${USER.login}"
        binding.settingsBtnChangeLogin.setOnClickListener { replaceFragment(ChangeLoginFragment()) }
        binding.settingsBtnChangeBio.setOnClickListener { replaceFragment(ChangeBioFragment()) }
        binding.settingsChangePhoto.setOnClickListener { (ChangePhotoUser()) }
    }

    private fun ChangePhotoUser(){
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(600, 600)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(APP_ACTIVITY, this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.settings_action_menu, menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.settings_menu_exit -> {
                AUTH.signOut()
                APP_ACTIVITY.replaceActivity(RegisterActivity())
            }
            R.id.settings_menu_change_name -> replaceFragment(ChangeNameFragment())
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode == RESULT_OK && data != null) {
            val uri = CropImage.getActivityResult(data).uri
            val path = REF_STORAGE_ROOT.child(FOLDER_PROFILE_IMAGE)
                .child(CURRENT_UID)
            path.putFile(uri).addOnCompleteListener { task1 ->
                if (task1.isSuccessful) {
                    path.downloadUrl.addOnCompleteListener { task2 ->
                        if (task2.isSuccessful) {
                            val photoUrl = task2.result.toString()
                            REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
                                .child(CHILD_PHOTO_URL).setValue(photoUrl)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        binding.settingsUserPhoto.downloadAndSetImage(photoUrl)
                                        showToast(getString(R.string.toast_data_update))
                                        USER.photoUrl = photoUrl
                                    }
                                }
                        }
                    }
                }
            }
        }
    }
}
