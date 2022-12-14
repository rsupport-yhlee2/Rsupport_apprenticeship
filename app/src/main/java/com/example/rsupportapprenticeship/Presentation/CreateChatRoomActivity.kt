package com.example.rsupportapprenticeship.Presentation

import android.content.Intent
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
        createChatRoomButton.setOnClickListener {
            startActivity(Intent(this@CreateChatRoomActivity,MainActivity::class.java).apply {
                this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                this.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                this.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            })
        }
    }
}