package com.example.rsupportapprenticeship.Presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rsupportapprenticeship.R
import com.example.rsupportapprenticeship.databinding.FragmentChatListBinding
import com.sendbird.android.channel.GroupChannel
import com.sendbird.android.channel.query.GroupChannelListQueryOrder
import com.sendbird.android.channel.query.MyMemberStateFilter
import com.sendbird.android.params.GroupChannelListQueryParams
import com.sendbird.android.params.PublicGroupChannelListQueryParams

class ChatListFragment : Fragment(R.layout.fragment_chat_list) {
    private lateinit var binding: FragmentChatListBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() = with(binding) {
        makeChatRoomButton.setOnClickListener {
            requireContext().startActivity(
                Intent(
                    requireContext(),
                    CreateChatRoomActivity::class.java
                ).apply {
                    this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    this.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    this.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                })
        }
        retrieveGroupChannel()
    }
    private fun retrieveGroupChannel() {
        val query = GroupChannel.createMyGroupChannelListQuery(
            GroupChannelListQueryParams().apply {
                includeEmpty = true
                myMemberStateFilter = MyMemberStateFilter.JOINED
                order = GroupChannelListQueryOrder.LATEST_LAST_MESSAGE
                limit = 15
            }
        )
        query.next { channels, e ->
            channels?.forEach { channel ->
                val channelName = channel.name
                var channelMember = ""
                channel.members.forEach { member ->
                    channelMember += member.nickname + " "
                }
                val channelUrl = channel.url
                Log.e("groupChannel:","${channelName} $channelMember $channelUrl")
            }
        }
    }
}