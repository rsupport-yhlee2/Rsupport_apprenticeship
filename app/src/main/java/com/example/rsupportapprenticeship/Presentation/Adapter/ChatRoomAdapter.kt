package com.example.rsupportapprenticeship.Presentation.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rsupportapprenticeship.databinding.ItemChatRoomBinding
import com.sendbird.android.channel.GroupChannel

class ChatRoomAdapter : RecyclerView.Adapter<ChatRoomAdapter.ViewHolder>() {
    private var chatRooms = mutableListOf<GroupChannel>()
    private lateinit var listener: (GroupChannel) -> Unit

    inner class ViewHolder(
        private val binding: ItemChatRoomBinding,
        private val listener: (GroupChannel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(channel: GroupChannel) = with(binding) {
            if (channel.coverUrl.isNotEmpty()) {
                roomProfile.setPadding(0)
                Glide.with(roomProfile)
                    .load(channel.coverUrl)
                    .into(roomProfile)
            }
            chatRoomName.text = channel.name
            var channelMember = ""
            channel.members.forEach { member ->
                channelMember += member.nickname + " "
            }
            chatRoomMember.text = channelMember
            binding.root.setOnClickListener {
                listener(channel)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemChatRoomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(chatRooms[position])
    }

    override fun getItemCount(): Int = chatRooms.size

    fun setData(list: List<GroupChannel>, listener: (GroupChannel) -> Unit) {
        chatRooms = list.toMutableList()
        this.listener = listener
        notifyDataSetChanged()
    }
}