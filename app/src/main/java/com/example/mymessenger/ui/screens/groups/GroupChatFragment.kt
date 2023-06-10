package com.example.mymessenger.ui.screens.groups

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.mymessenger.R
import com.example.mymessenger.database.*
import com.example.mymessenger.databinding.ChoiceUploadBinding
import com.example.mymessenger.databinding.FragmentSingleChatBinding
import com.example.mymessenger.models.CommonModel
import com.example.mymessenger.models.UserModel
import com.example.mymessenger.ui.screens.base.BaseFragment
import com.example.mymessenger.ui.message_recycler_view.view.AppViewFactory
import com.example.mymessenger.ui.screens.main_list.MainListFragment
import com.example.mymessenger.utilits.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.database.DatabaseReference
import com.theartofdev.edmodo.cropper.CropImage
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GroupChatFragment(private val group: CommonModel) : BaseFragment(R.layout.fragment_single_chat) {

    private var _binding: FragmentSingleChatBinding? = null
    private val binding get() = _binding!!
    private var bindingU: ChoiceUploadBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSingleChatBinding.inflate(inflater, container, false)
        bindingU = ChoiceUploadBinding.inflate(inflater)
        return binding.root
    }
        private lateinit var mListenerInfoToolbar: AppValueEventListener
        private lateinit var mReceivingUser: UserModel
        private lateinit var mToolbarInfo: View
        private lateinit var mRefUser: DatabaseReference
        private lateinit var mRefMessages: DatabaseReference
        private lateinit var mAdapter: GroupChatAdapter
        private lateinit var mRecyclerView: RecyclerView
        private lateinit var mMessagesListener: AppChildEventListener
        private var mCountMessages = 10
        private var mIsScrolling = false
        private var mSmoothScrollToPosition = true
        private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
        private lateinit var mLayoutManager: LinearLayoutManager
        private lateinit var mAppVoiceRecorder: AppVoiceRecorder
        private lateinit var mBottomSheetBehavior: BottomSheetBehavior<*>

        override fun onResume() {
            super.onResume()
            initFields()
            initToolbar()
            initRecycleView()
        }

        @SuppressLint("ClickableViewAccessibility")
        private fun initFields() {
            setHasOptionsMenu(true)
            val bottomSheetChoice: View? = view?.findViewById(R.id.bottom_sheet_choice)
            if (bottomSheetChoice != null) {
                mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheetChoice)
            }
            mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            mAppVoiceRecorder = AppVoiceRecorder()
            mSwipeRefreshLayout = binding.chatSwipeRefresh
            mLayoutManager = LinearLayoutManager(this.context)
            binding.chatInputMessage.addTextChangedListener(AppTextWatcher {
                val string = binding.chatInputMessage.text.toString()
                if (string.isEmpty() || string == "Запись") {
                    binding.chatBtnSendMessage.visibility = View.GONE
                    binding.chatBtnAttach.visibility = View.VISIBLE
                    binding.chatBtnVoice.visibility = View.VISIBLE
                } else {
                    binding.chatBtnSendMessage.visibility = View.VISIBLE
                    binding.chatBtnAttach.visibility = View.GONE
                    binding.chatBtnVoice.visibility = View.GONE
                }
            })

            binding.chatBtnAttach.setOnClickListener { attach() }

            CoroutineScope(Dispatchers.IO).launch {
                binding.chatBtnVoice.setOnTouchListener { v, event ->
                    if (checkPermission(RECORD_AUDIO)) {
                        if (event.action == MotionEvent.ACTION_DOWN) {
                            //TODO record
                            binding.chatInputMessage.setText("Запись...")
                            binding.chatBtnVoice.setColorFilter(
                                ContextCompat.getColor(
                                    APP_ACTIVITY,
                                    com.mikepenz.materialize.R.color.colorPrimary
                                )
                            )
                            val messageKey = getMessageKey(group.id)
                            mAppVoiceRecorder.startRecord(messageKey)
                        } else if (event.action == MotionEvent.ACTION_UP) {
                            //TODO stop record
                            binding.chatInputMessage.setText("")
                            binding.chatBtnVoice.colorFilter = null
                            mAppVoiceRecorder.stopRecord { file, messageKey ->
                                uploadFileToStorage(Uri.fromFile(file),messageKey,group.id, TYPE_MESSAGE_VOICE)
                                mSmoothScrollToPosition = true
                            }
                        }
                    }
                    true
                }
            }

        }

        private fun attach() {
            mBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            view?.findViewById<ImageView>(R.id.btn_attach_file)?.setOnClickListener { attachFile() }
            view?.findViewById<ImageView>(R.id.btn_attach_image)?.setOnClickListener { attachImage() }
        }

        private fun attachFile(){
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            startActivityForResult(intent, PICK_FILE_REQUEST_CODE)
        }


        private fun attachImage() {
            CropImage.activity()
                .setAspectRatio(1, 1)
                .setRequestedSize(250, 250)
                .start(APP_ACTIVITY, this)
        }

        private fun initRecycleView() {
            mRecyclerView = binding.chatRecycleView
            mAdapter = GroupChatAdapter()


            mRefMessages = REF_DATABASE_ROOT
                .child(NODE_GROUPS)
                .child(group.id)
                .child(NODE_MESSAGES)

            mRecyclerView.adapter = mAdapter
            mRecyclerView.setHasFixedSize(true)
            mRecyclerView.isNestedScrollingEnabled = false
            mRecyclerView.layoutManager = mLayoutManager
            mMessagesListener = AppChildEventListener {
                val message = it.getCommonModel()

                if (mSmoothScrollToPosition) {
                    mAdapter.addItemToBottom(AppViewFactory.getView(message)) {
                        mRecyclerView.smoothScrollToPosition(mAdapter.itemCount)
                    }
                } else {
                    mAdapter.addItemToTop(AppViewFactory.getView(message)) {
                        mSwipeRefreshLayout.isRefreshing = false
                    }
                }

            }
            mRefMessages.limitToLast(mCountMessages).addChildEventListener(mMessagesListener)

            mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    println(mRecyclerView.recycledViewPool.getRecycledViewCount(0))
                    if (mIsScrolling && dy < 0 && mLayoutManager.findFirstVisibleItemPosition() <= 3) {
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


            mSwipeRefreshLayout.setOnRefreshListener { updateData() }
        }

        private fun updateData() {
            mSmoothScrollToPosition = false
            mIsScrolling = false
            mCountMessages += 10
            mRefMessages.removeEventListener(mMessagesListener)
            mRefMessages.limitToLast(mCountMessages).addChildEventListener(mMessagesListener)
        }

        private fun initToolbar() {
            mToolbarInfo = APP_ACTIVITY.mToolbar.findViewById<ConstraintLayout>(R.id.toolbar_info)
            mToolbarInfo.visibility = View.VISIBLE
            mListenerInfoToolbar = AppValueEventListener {
                mReceivingUser = it.getUserModel()
                initInfoToolbar()
            }

            mRefUser = REF_DATABASE_ROOT.child(
                NODE_USERS
            ).child(group.id)
            mRefUser.addValueEventListener(mListenerInfoToolbar)

            binding.chatBtnSendMessage.setOnClickListener {
                mSmoothScrollToPosition = true
                val message = binding.chatInputMessage.text.toString()
                if (message.isEmpty()) {
                    showToast("Введите сообщение")
                } else sendMessageToGroup(
                    message,
                    group.id,
                    TYPE_TEXT
                ) {
                    binding.chatInputMessage.setText("")
                }
            }
        }




        private fun initInfoToolbar() {
            if (mReceivingUser.username.isEmpty()) {
                mToolbarInfo.findViewById<TextView>(R.id.toolbar_chat_username).text = group.username
            } else mToolbarInfo.findViewById<TextView>(R.id.toolbar_chat_username).text = mReceivingUser.username

            mToolbarInfo.findViewById<CircleImageView>(R.id.toolbar_chat_image).downloadAndSetImage(group .photoUrl)
            mToolbarInfo.findViewById<TextView>(R.id.toolbar_chat_status).text = mReceivingUser.state
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            /* Активность которая запускается для получения картинки для фото пользователя */
            super.onActivityResult(requestCode, resultCode, data)
            if (data!=null){
                when(requestCode){
                    CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                        val uri = CropImage.getActivityResult(data).uri
                        val messageKey = getMessageKey(group.id)
                        uploadFileToStorage(uri,messageKey,group.id, TYPE_MESSAGE_IMAGE)
                        mSmoothScrollToPosition = true
                    }

                    PICK_FILE_REQUEST_CODE -> {
                        val uri = data.data
                        val messageKey = getMessageKey(group.id)
                        val filename = getFilenameFromUri(uri!!)
                        uploadFileToStorage(uri,messageKey,group.id, TYPE_MESSAGE_FILE,filename)
                        mSmoothScrollToPosition = true
                    }
                }
            }
        }



        override fun onPause() {
            super.onPause()
            mToolbarInfo.visibility = View.GONE
            mRefUser.removeEventListener(mListenerInfoToolbar)
            mRefMessages.removeEventListener(mMessagesListener)
        }

        override fun onDestroyView() {
            super.onDestroyView()
            mAppVoiceRecorder.releaseRecorder()
            mAdapter.onDestroy()
        }



        override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
            /* Создания выпадающего меню*/
            activity?.menuInflater?.inflate(R.menu.single_chat_action_menu, menu)
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            /* Слушатель выбора пунктов выпадающего меню */
            when (item.itemId) {
                R.id.menu_clear_chat -> clearChat(group.id){
                    showToast("Чат очищен")
                    replaceFragment(MainListFragment())
                }
                R.id.menu_delete_chat -> deleteChat(group.id){
                    showToast("Чат удален")
                    replaceFragment(MainListFragment())
                }
            }
            return true
        }


    }