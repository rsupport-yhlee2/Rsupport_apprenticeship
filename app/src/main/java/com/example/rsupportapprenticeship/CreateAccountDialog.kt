package com.example.rsupportapprenticeship

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import com.example.rsupportapprenticeship.databinding.CreateAccountDialogBinding

class CreateAccountDialog(context: Context) : Dialog(context) {
    private lateinit var binding: CreateAccountDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CreateAccountDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() = with(binding) {
        setCancelable(false)
        window?.setBackgroundDrawableResource(R.color.transparent)
        cancelButton.setOnClickListener {
            dismiss()
        }
        createButton.setOnClickListener {
            val id = createIDInput.text
            val password = createPasswordInput.text
            val nickname = createNicknameInput.text
            Toast.makeText(context, "$id $password $nickname", Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }
}