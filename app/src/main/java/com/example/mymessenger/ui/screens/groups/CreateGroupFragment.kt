package com.example.mymessenger.ui.screens.groups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mymessenger.R
import com.example.mymessenger.databinding.FragmentAddContactsBinding
import com.example.mymessenger.databinding.FragmentCreateGroupBinding
import com.example.mymessenger.models.CommonModel
import com.example.mymessenger.ui.screens.base.BaseFragment
import com.example.mymessenger.utilits.APP_ACTIVITY
import com.example.mymessenger.utilits.getPlurals
import com.example.mymessenger.utilits.hideKeyboard
import com.example.mymessenger.utilits.showToast

class CreateGroupFragment(private var listContacts:List<CommonModel>): BaseFragment(R.layout.fragment_create_group) {

    private var _binding: FragmentCreateGroupBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCreateGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: AddContactsAdapter

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = getString(R.string.create_group)
        hideKeyboard()
        initRecyclerView()
        binding.createGroupBtnComplete.setOnClickListener { showToast("Click") }
        binding.createGroupInputName.requestFocus()
        binding.createGroupCounts.text = getPlurals(listContacts.size)
    }

    private fun initRecyclerView() {
        mRecyclerView = binding.createGroupRecycleView
        mAdapter = AddContactsAdapter()
        mRecyclerView.adapter = mAdapter
        listContacts.forEach {  mAdapter.updateListItems(it) }
    }
}