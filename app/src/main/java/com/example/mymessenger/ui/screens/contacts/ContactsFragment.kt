package com.example.mymessenger.ui.screens.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.mymessenger.R
import com.example.mymessenger.database.*
import com.example.mymessenger.databinding.ContactItemBinding
import com.example.mymessenger.databinding.FragmentContactsBinding
import com.example.mymessenger.models.CommonModel
import com.example.mymessenger.ui.screens.base.BaseFragment
import com.example.mymessenger.ui.screens.single_chat.SingleChatFragment
import com.example.mymessenger.utilits.*
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import de.hdodenhof.circleimageview.CircleImageView

class ContactsFragment : BaseFragment(R.layout.fragment_contacts) {
    private lateinit var binding: FragmentContactsBinding
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: FirebaseRecyclerAdapter<CommonModel, ContactsHolder>
    private lateinit var mRefContacts: DatabaseReference
    private lateinit var mRefUsers: DatabaseReference
    private lateinit var mRefUsersListener:AppValueEventListener
    private lateinit var mToolbarContacts:View
    private  var mapListeners = hashMapOf<DatabaseReference,AppValueEventListener>()

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "Контакты"
        initRecycleView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initContactsToolbar()
        binding = FragmentContactsBinding.bind(view)
    }

    private fun initContactsToolbar() {
        mToolbarContacts = APP_ACTIVITY.mToolbar.findViewById<ConstraintLayout>(R.id.toolbar_contact)
        mToolbarContacts.visibility = View.VISIBLE

        val toolbarContactFind = mToolbarContacts.findViewById<EditText>(R.id.toolbar_conatct_find)

        mToolbarContacts.findViewById<CircleImageView>(R.id.toolbar_contact_image).setOnClickListener {
            toolbarContactFind.visibility = View.VISIBLE
            mToolbarContacts.findViewById<CircleImageView>(R.id.toolbar_contact_image).visibility = View.INVISIBLE
            toolbarContactFind.addTextChangedListener(AppTextWatcher {
                val string = toolbarContactFind.text.toString()
                REF_DATABASE_ROOT.child(NODE_LOGINS).addListenerForSingleValueEvent(
                    AppValueEventListener {
                        it.children.forEach { snapshot ->
                            if (string == snapshot.key) {
                                showToast(string)
                            }
                        }
                    })
            })
            APP_ACTIVITY.title = "Контакты"

        }
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

                mRefUsersListener = AppValueEventListener {
                    val contact = it.getCommonModel()
                    holder.binding.contactUsername.text = contact.username
                    holder.binding.contactStatus.text = contact.state
                    holder.binding.contactPhoto.downloadAndSetImage(contact.photoUrl)
                    holder.itemView.setOnClickListener { replaceFragment(SingleChatFragment(contact)) }
                }
                mRefUsers.addValueEventListener(mRefUsersListener)
                mapListeners[mRefUsers] = mRefUsersListener
            }
        }

        mRecyclerView.adapter = mAdapter
        mAdapter.startListening()
    }

    inner class ContactsHolder(val binding: ContactItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onPause() {
        super.onPause()
        mAdapter.stopListening()
        mapListeners.forEach {
            it.key.removeEventListener(it.value)
        }
    }
}