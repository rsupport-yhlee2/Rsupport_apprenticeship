package com.example.rsupportapprenticeship.Presentation.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.example.rsupportapprenticeship.databinding.ItemMessageBinding
import com.sendbird.android.SendbirdChat
import com.sendbird.android.message.UserMessage
import com.sendbird.android.params.UserMessageCreateParams

class MessageAdapter : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {
    private var messageList = mutableListOf<UserMessage>()

    inner class ViewHolder(private val binding: ItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(message: UserMessage) = with(binding) {
            if (SendbirdChat.currentUser?.nickname == message.customType) {
                messageYou.text = message.message
                nicknameText.isGone = true
                messageOther.isGone = true
                profile.isGone = true
            } else {
                messageOther.text = message.message
                nicknameText.text = message.customType
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

    fun setData(list: List<UserMessage>){
        messageList = list.toMutableList()
        notifyDataSetChanged()
    }

    fun addMessage(message: UserMessage) {
        messageList.add(message)
        notifyDataSetChanged()
    }
}