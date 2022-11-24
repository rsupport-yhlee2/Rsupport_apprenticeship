package com.example.rsupportapprenticeship

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rsupportapprenticeship.databinding.ActivityCreateChatRoomBinding

class CreateChatRoomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateChatRoomBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateChatRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() = with(binding){

    }
}