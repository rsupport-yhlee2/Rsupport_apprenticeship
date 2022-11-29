package com.example.rsupportapprenticeship.Presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rsupportapprenticeship.Presentation.Adapter.ChatRoomAdapter
import com.example.rsupportapprenticeship.R
import com.example.rsupportapprenticeship.databinding.FragmentChatListBinding
import com.sendbird.android.channel.GroupChannel
import com.sendbird.android.channel.query.GroupChannelListQueryOrder
import com.sendbird.android.channel.query.MyMemberStateFilter
import com.sendbird.android.params.GroupChannelListQueryParams
import com.sendbird.android.params.PublicGroupChannelListQueryParams

class ChatListFragment : Fragment(R.layout.fragment_chat_list) {
    private lateinit var binding: FragmentChatListBinding
    private val adapter = ChatRoomAdapter()
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
        refresh.setOnRefreshListener {
            val main = activity as MainActivity
            main.showFragment(ChatListFragment())
        }
        binding.chatRoomList.adapter = adapter
        binding.chatRoomList.layoutManager = LinearLayoutManager(requireContext())
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
            channels?.let {
                adapter.setData(channels){
                    context?.startActivity(Intent(requireContext(),ChatRoomActivity::class.java).apply {
                        putExtra("channel",it.url)
                    })
                }
            }
        }
    }
}