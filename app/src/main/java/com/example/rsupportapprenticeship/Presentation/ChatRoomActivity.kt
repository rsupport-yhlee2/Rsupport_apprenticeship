package com.example.rsupportapprenticeship.Presentation

import android.os.Bundle
import android.util.Log
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
    }

    private fun retrieveGroupChannelByUrl(url: String) {
        GroupChannel.getChannel(url) { channel, e ->
            if (e != null) {
            }
            channel?.let {
                currentChannel = it
                Log.e("channel", "$currentChannel")
                getMessageFromChannel(it.name)
            }
        }
    }

    private fun sendMessage(message: String) {
        currentChannel.sendUserMessage(UserMessageCreateParams(message).apply {
            customType = SendbirdChat.currentUser?.userId.toString()
        }) { message, e ->
            if (e != null) {

            }
            message?.let {
                adapter.addMessage(it)
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()
    private fun getMessageFromChannel(id : String) {
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