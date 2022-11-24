package com.example.rsupportapprenticeship.Presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rsupportapprenticeship.R
import com.example.rsupportapprenticeship.databinding.FragmentFriendBinding

class FriendFragment : Fragment(R.layout.fragment_friend) {
    private lateinit var binding: FragmentFriendBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFriendBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() = with(binding) {

    }
}