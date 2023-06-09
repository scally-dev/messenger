package com.example.mymessenger.ui.fragments.single_chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.mymessenger.R
import com.example.mymessenger.databinding.FragmentSingleChatBinding
import com.example.mymessenger.models.CommonModel
import com.example.mymessenger.models.UserModel
import com.example.mymessenger.ui.fragments.BaseFragment
import com.example.mymessenger.utilits.*
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import de.hdodenhof.circleimageview.CircleImageView


class SingleChatFragment(private val contact: CommonModel) : BaseFragment(R.layout.fragment_single_chat) {

    private var _binding: FragmentSingleChatBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSingleChatBinding.inflate(inflater, container, false)
        return binding.root
    }


    private lateinit var mListenerInfoToolbar: AppValueEventListener
    private lateinit var mReceivingUser: UserModel
    private lateinit var mToolbarInfo:View
    private lateinit var mRefUser: DatabaseReference

    private lateinit var mRefMessages: DatabaseReference
    private lateinit var mAdapter: SingleChatAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mMessagesListener: AppChildEventListener
    private var mCountMessages = 10
    private var mIsScrolling = false
    private var mSmoothScrollToPosition = true
    private var mListListeners = mutableListOf<AppChildEventListener>()

    override fun onResume() {
        super.onResume()
        initToolbar()
        initRecycleView()
    }

    private fun initRecycleView() {
        mRecyclerView = binding.chatRecycleView
        mAdapter = SingleChatAdapter()
        mRefMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES)
            .child(CURRENT_UID)
            .child(contact.id)
        mRecyclerView.adapter = mAdapter
        mMessagesListener = AppChildEventListener{
            mAdapter.addItem(it.getCommonModel())
            mRecyclerView.smoothScrollToPosition(mAdapter.itemCount)
            if (mSmoothScrollToPosition) {
                mRecyclerView.smoothScrollToPosition(mAdapter.itemCount)
            }
        }
        mRefMessages.limitToLast(mCountMessages).addChildEventListener(mMessagesListener)
        mListListeners.add(mMessagesListener)

        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (mIsScrolling && dy < 0) {
                    updateData()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    mIsScrolling = true
                }
            }
        })
    }

    private fun updateData() {
        mSmoothScrollToPosition = false
        mIsScrolling = false
        mCountMessages += 10
        mRefMessages.limitToLast(mCountMessages).addChildEventListener(mMessagesListener)
        mListListeners.add(mMessagesListener)
    }

    private fun initToolbar() {
        mToolbarInfo = APP_ACTIVITY.mToolbar.findViewById<ConstraintLayout>(R.id.toolbar_info)
        mToolbarInfo.visibility = View.VISIBLE
        mListenerInfoToolbar = AppValueEventListener {
            mReceivingUser = it.getUserModel()
            initInfoToolbar()
        }

        mRefUser = REF_DATABASE_ROOT.child(NODE_USERS).child(contact.id)
        mRefUser.addValueEventListener(mListenerInfoToolbar)

        binding.chatBtnSendMessage.setOnClickListener {
            mSmoothScrollToPosition = true
            val message = binding.chatInputMessage.text.toString()
            if (message.isEmpty()){
                showToast("Введите сообщение")
            } else sendMessage(message,contact.id, TYPE_TEXT){
                binding.chatInputMessage.setText("")
            }
        }
    }

    private fun initInfoToolbar() {
        mToolbarInfo.findViewById<CircleImageView>(R.id.toolbar_chat_image).downloadAndSetImage(mReceivingUser.photoUrl)
        mToolbarInfo.findViewById<TextView>(R.id.toolbar_chat_username).text = mReceivingUser.username
        mToolbarInfo.findViewById<TextView>(R.id.toolbar_chat_status).text = mReceivingUser.state
    }

    override fun onPause() {
        super.onPause()
        mToolbarInfo.visibility = View.GONE
        mRefUser.removeEventListener(mListenerInfoToolbar)
        mListListeners.forEach {
            mRefMessages.removeEventListener(it)
        }
    }
}