package com.example.rsupportapprenticeship.Presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rsupportapprenticeship.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initDialog() {
        CreateAccountDialog(this,"create").show()

    }

    private fun initViews() = with(binding) {
        buttonSignup.setOnClickListener {
            initDialog()
        }
    }

}

