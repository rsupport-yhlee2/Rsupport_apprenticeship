package com.example.rsupportapprenticeship

import android.app.Dialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.rsupportapprenticeship.databinding.ActivitySignUpBinding
import com.example.rsupportapprenticeship.databinding.CreateAccountDialogBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initDialog() {
        CreateAccountDialog(this).show()
    }

    private fun initViews() = with(binding) {
        buttonSignup.setOnClickListener {
            initDialog()
        }
    }

}

