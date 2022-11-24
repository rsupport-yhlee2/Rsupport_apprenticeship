package com.example.rsupportapprenticeship.Presentation

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.rsupportapprenticeship.R
import com.example.rsupportapprenticeship.databinding.CreateAccountDialogBinding

class CreateAccountDialog(context: Context, private val mode: String) : Dialog(context) {
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
        when (mode) {
            "create" -> {
                createIDInput.isEnabled = true
                createButton.text = "create"
                createButton.setOnClickListener {
                    val id = createIDInput.text
                    val password = createPasswordInput.text
                    val nickname = createNicknameInput.text
                    Toast.makeText(context, "$id $password $nickname", Toast.LENGTH_SHORT).show()
                    context.startActivity(Intent(context, MainActivity::class.java).apply {
                        this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        this.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        this.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    })
                    dismiss()
                }
            }
            "update" -> {
                createIDInput.setText("set user ID")
                createIDInput.isEnabled = false
                createButton.text = "update"
                createButton.setOnClickListener {
                    val password = createPasswordInput.text
                    val nickname = createNicknameInput.text
                    Toast.makeText(context, " $password $nickname", Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            }
        }
    }
}