package com.example.rsupportapprenticeship.Presentation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rsupportapprenticeship.R
import com.example.rsupportapprenticeship.databinding.FragmentChatListBinding

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
    }
}