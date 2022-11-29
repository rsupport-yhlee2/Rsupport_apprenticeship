package com.example.rsupportapprenticeship.Presentation

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rsupportapprenticeship.Presentation.Adapter.MessageAdapter
import com.example.rsupportapprenticeship.R
import com.example.rsupportapprenticeship.databinding.ActivityGroupChannelChatBinding
import com.sendbird.android.SendbirdChat
import com.sendbird.android.channel.BaseChannel
import com.sendbird.android.channel.GroupChannel
import com.sendbird.android.handler.OpenChannelHandler
import com.sendbird.android.message.BaseMessage
import com.sendbird.android.message.UserMessage
import com.sendbird.android.params.GroupChannelUpdateParams
import com.sendbird.android.params.PreviousMessageListQueryParams
import com.sendbird.android.params.UserMessageCreateParams
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ChatRoomActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var binding: ActivityGroupChannelChatBinding
    private lateinit var currentChannel: GroupChannel
    private val adapter = MessageAdapter()
    private lateinit var job: Job
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupChannelChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() = with(binding) {
        recyclerviewChat.adapter = adapter
        recyclerviewChat.layoutManager = LinearLayoutManager(this@ChatRoomActivity)
        val url = intent.getStringExtra("channel")
        url?.let {
            retrieveGroupChannelByUrl(it)
        }
        chatSendButton.setOnClickListener {
            val message = chatInputView.text.toString()
            sendMessage(message)
        }
        setCustomToolBar()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.set_chat_room_profile -> {
                Toast.makeText(this, "select", Toast.LENGTH_SHORT).show()
                setChatProfile()
            }
            R.id.quit_chat_room -> {
                alertPopup()
            }
            R.id.invite_user -> {
                inviteUserPopup()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun alertPopup() {
        AlertDialog.Builder(this).apply {
            setTitle("leave room")
            setPositiveButton("yes") { dialog, which ->
                currentChannel.leave {
                    finish()
                }
            }
            setNegativeButton("no") { _, _ -> }
            show()
        }
    }

    private fun inviteUserPopup() {
        val input = EditText(this)
        input.hint = "UserID"
        AlertDialog.Builder(this).apply {
            setTitle("invite User!")
            setView(input)
            setPositiveButton("yes") { dialog, which ->
                val userID = input.text.toString()
                inviteUser(userID)
            }
            setNegativeButton("no") { _, _ -> }
            show()
        }
    }

    private fun inviteUser(userID: String) {
        currentChannel.invite(listOf(userID)) {

        }
    }

    private fun setChatProfile() {
        val input = EditText(this)
        input.hint = "profileURL"
        AlertDialog.Builder(this).apply {
            setTitle("set CoverImage")
            setView(input)
            setPositiveButton("yes") { dialog, which ->
                val profileURL = input.text.toString()
                updateGroupChannel(profileURL)
            }
            setNegativeButton("no") { _, _ -> }
            show()
        }
    }

    private fun updateGroupChannel(url: String) {
        currentChannel.updateChannel(GroupChannelUpdateParams().apply {
            coverUrl = url
        }) { channel, e ->

        }
    }

    private fun setCustomToolBar() {
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        actionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun retrieveGroupChannelByUrl(url: String) {
        GroupChannel.getChannel(url) { channel, e ->
            if (e != null) {
            }
            channel?.let {
                currentChannel = it
                getMessageFromChannel(it.name)
                loadPreviousMessage(currentChannel)
            }
        }
    }

    private fun sendMessage(message: String) {
        currentChannel.sendUserMessage(UserMessageCreateParams(message).apply {
            if (SendbirdChat.currentUser?.profileUrl?.length == 0) {
                customType =
                    SendbirdChat.currentUser?.nickname.toString() + " " + SendbirdChat.currentUser?.userId.toString()
            } else {
                customType =
                    SendbirdChat.currentUser?.nickname.toString() + " " + SendbirdChat.currentUser?.userId.toString() + " " + SendbirdChat.currentUser?.profileUrl
            }
        }) { message, e ->
            if (e != null) {

            }
            message?.let {
                adapter.addMessage(it)
                Log.e("mess", "$it")
            }
        }
    }

    private fun loadPreviousMessage(groupChannel: GroupChannel) {
        val query = groupChannel.createPreviousMessageListQuery(
            PreviousMessageListQueryParams()
        )
        query.load() { messages, e ->
            if (e != null) {

            }
            messages?.forEach {
                val message = it as UserMessage
                adapter.addMessage(message)
                //Log.e("message", "$message")
            }
        }
    }


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private fun getMessageFromChannel(id: String) {
        SendbirdChat.addChannelHandler(
            id,
            object : OpenChannelHandler() {
                override fun onMessageReceived(channel: BaseChannel, message: BaseMessage) {
                    if (message is UserMessage) {
                        adapter.addMessage(message = message)
                    }
                }
            }
        )
    }
}