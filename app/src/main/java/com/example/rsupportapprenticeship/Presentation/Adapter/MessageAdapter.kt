package com.example.rsupportapprenticeship.Presentation.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rsupportapprenticeship.databinding.ItemMessageBinding
import com.sendbird.android.SendbirdChat
import com.sendbird.android.message.UserMessage

class MessageAdapter : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {
    private var messageList = mutableListOf<UserMessage>()

    inner class ViewHolder(private val binding: ItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(message: UserMessage) = with(binding) {
            var url = ""
            var nickname = ""
            var userID = ""
            val data = message.customType.split(" ")
            Log.e("data:","${data}")
            if (data.size == 2) {
                nickname = data[0]
                userID = data[1]
            } else {
                nickname = data[0]
                userID = data[1]
                url = data[2]
            }
            //userID 기준으로 판단하도록 수정
            if (SendbirdChat.currentUser?.userId == userID) {
                messageYou.text = message.message
                nicknameText.isGone = true
                messageOther.isGone = true
                profile.isGone = true
            } else {
                messageOther.text = message.message
                nicknameText.text = nickname
                if (url.isNotEmpty()) {
                    Glide.with(profile)
                        .load(url)
                        .into(profile)
                }
                messageYou.isGone = true
            }
        }
    }

    override fun getItemCount(): Int = messageList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(messageList[position])
    }

    fun setData(list: List<UserMessage>) {
        messageList = list.toMutableList()
        notifyDataSetChanged()
    }

    fun addMessage(message: UserMessage) {
        messageList.add(message)
        notifyDataSetChanged()
    }
}