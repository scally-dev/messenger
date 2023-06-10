package com.example.mymessenger.ui.screens.groups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.mymessenger.R
import com.example.mymessenger.database.*
import com.example.mymessenger.databinding.FragmentAddContactsBinding
import com.example.mymessenger.databinding.FragmentSettingsBinding
import com.example.mymessenger.models.CommonModel
import com.example.mymessenger.ui.screens.base.BaseFragment
import com.example.mymessenger.utilits.*

class AddContactsFragment : BaseFragment(R.layout.fragment_add_contacts) {
    private var _binding: FragmentAddContactsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAddContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: AddContactsAdapter
    private val mRefMainList = REF_DATABASE_ROOT.child(NODE_MAIN_LIST).child(CURRENT_UID)
    private val mRefUsers = REF_DATABASE_ROOT.child(NODE_USERS)
    private val mRefMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(CURRENT_UID)
    private var mListItems = listOf<CommonModel>()

    override fun onResume() {
        listContacts.clear()
        super.onResume()
        APP_ACTIVITY.title = "Добавить участника"
        hideKeyboard()
        initRecyclerView()
        binding.addContactsBtnNext.setOnClickListener {
            if (listContacts.isEmpty()) showToast("Добавьте участника")
            else replaceFragment(CreateGroupFragment(listContacts))
            }
        }

    private fun initRecyclerView() {
        mRecyclerView = binding.addContactsRecycleView
        mAdapter = AddContactsAdapter()

        // 1 запрос
        mRefMainList.addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot ->
            mListItems = dataSnapshot.children.map { it.getCommonModel() }
            mListItems.forEach { model ->

                // 2 запрос
                mRefUsers.child(model.id)
                    .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot1 ->
                        val newModel = dataSnapshot1.getCommonModel()

                        // 3 запрос
                        mRefMessages.child(model.id).limitToLast(1)
                            .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot2 ->
                                val tempList = dataSnapshot2.children.map { it.getCommonModel() }

                                if (tempList.isEmpty()){
                                    newModel.lastMessage = "Чат очищен"
                                } else {
                                    newModel.lastMessage = tempList[0].text
                                }
                                if (newModel.username.isEmpty()) {
                                    newModel.username = newModel.login
                                }
                                mAdapter.updateListItems(newModel)
                            })
                    })
            }
        })

        mRecyclerView.adapter = mAdapter
    }

    companion object{
        val listContacts = mutableListOf<CommonModel>()
    }
}