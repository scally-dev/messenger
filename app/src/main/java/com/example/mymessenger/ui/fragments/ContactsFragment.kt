package com.example.mymessenger.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mymessenger.R
import com.example.mymessenger.databinding.ContactItemBinding
import com.example.mymessenger.databinding.FragmentContactsBinding
import com.example.mymessenger.models.CommonModel
import com.example.mymessenger.ui.fragments.BaseFragment
import com.example.mymessenger.utilits.*
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference

class ContactsFragment : BaseFragment(R.layout.fragment_contacts) {
    private lateinit var binding: FragmentContactsBinding
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: FirebaseRecyclerAdapter<CommonModel, ContactsHolder>
    private lateinit var mRefContacts: DatabaseReference
    private lateinit var mRefUsers: DatabaseReference

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "Контакты"
        initRecycleView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentContactsBinding.bind(view)
    }

    private fun initRecycleView() {
        mRecyclerView = binding.contactsRecycleView
        mRefContacts = REF_DATABASE_ROOT.child(NODE_CONTACTS).child(CURRENT_UID)

        val options = FirebaseRecyclerOptions.Builder<CommonModel>()
            .setQuery(mRefContacts, CommonModel::class.java)
            .build()

        mAdapter = object : FirebaseRecyclerAdapter<CommonModel, ContactsHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsHolder {
                val contactItemBinding = ContactItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ContactsHolder(contactItemBinding)
            }

            override fun onBindViewHolder(holder: ContactsHolder, position: Int, model: CommonModel) {
                mRefUsers = REF_DATABASE_ROOT.child(NODE_USERS).child(model.id)

                mRefUsers.addValueEventListener(AppValueEventListener {
                    val contact = it.getCommonModel()
                    holder.binding.contactUsername.text = contact.username
                    holder.binding.contactStatus.text = contact.state
                    holder.binding.contactPhoto.downloadAndSetImage(contact.photoUrl)
                })
            }
        }

        mRecyclerView.adapter = mAdapter
        mAdapter.startListening()
    }

    inner class ContactsHolder(val binding: ContactItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onPause() {
        super.onPause()
        mAdapter.stopListening()
    }
}