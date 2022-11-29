package com.example.rsupportapprenticeship.Presentation

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.rsupportapprenticeship.Data.SendbirdService
import com.example.rsupportapprenticeship.Data.UserDTO
import com.example.rsupportapprenticeship.Key
import com.example.rsupportapprenticeship.R
import com.example.rsupportapprenticeship.databinding.CreateAccountDialogBinding
import com.sendbird.android.SendbirdChat
import com.sendbird.android.params.UserUpdateParams
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.CoroutineContext

class CreateAccountDialog(context: Context, private val mode: String) : Dialog(context),
    CoroutineScope {
    private lateinit var binding: CreateAccountDialogBinding
    private lateinit var retrofit: Retrofit
    private lateinit var sendbirdService: SendbirdService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retrofit = Retrofit.Builder()
            .baseUrl(Key.API_REQUEST_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        sendbirdService = retrofit.create(SendbirdService::class.java)
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
                    val id = createIDInput.text.toString()
                    val password = createPasswordInput.text.toString()
                    val nickname = createNicknameInput.text.toString()
                    Toast.makeText(context, "계정 생성 성공!", Toast.LENGTH_SHORT).show()
                    createUser(id, nickname, password)
                    dismiss()
                }
            }
            "update" -> {
                createIDInput.setText("Not Editable")
                createIDInput.isEnabled = false
                createButton.text = "update"
                createButton.setOnClickListener {
                    val password = createPasswordInput.text.toString()
                    val nickname = createNicknameInput.text.toString()
                    val token =
                        SendbirdChat.currentUser?.metaData?.values.toString().replace("[", "")
                            .replace("]", "")
                    updateUser(nickname, password, token)
                    Toast.makeText(context, " $password $nickname", Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            }
        }
    }

    private fun updateUser(nickname: String, password: String, token: String) =
        launch(Dispatchers.IO) {
            SendbirdChat.currentUser?.deleteAllMetaData() {
                SendbirdChat.currentUser?.createMetaData(mapOf(password to token)) { data, e ->

                }
            }
            val params = UserUpdateParams().apply {
                this.nickname = nickname
            }
            SendbirdChat.updateCurrentUserInfo(params) { e ->
                if (e != null) {
                    Log.e("update", "success")
                }
            }
        }

    private fun createUser(userID: String, nickname: String, password: String) = launch {
        val job = sendbirdService.createOneUser(
            Key.CONTENT_TYPE,
            Key.API_TOKEN,
            UserDTO(userID, nickname, "", "", true)
        )
        setPassword(userID, password)
    }

    private fun setPassword(userID: String, password: String) = launch {
        val response = sendbirdService.getOneUser(Key.CONTENT_TYPE, Key.API_TOKEN, userID)
        response.body().let {
            SendbirdChat.connect(userID, it?.access_token) { user, e ->
                if (user != null) {
                    it?.let {
                        user.createMetaData(mapOf(password to it.access_token)) { data, e ->
                            context.startActivity(Intent(context, MainActivity::class.java).apply {
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            })
                        }
                    }
                }
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + Job()
}