package com.example.rsupportapprenticeship.Presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.rsupportapprenticeship.Data.SendbirdService
import com.example.rsupportapprenticeship.Key
import com.example.rsupportapprenticeship.databinding.ActivitySignUpBinding
import com.example.rsupportapprenticeship.databinding.CreateAccountDialogBinding
import com.sendbird.android.SendbirdChat
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.CoroutineContext

class SignUpActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var retrofit: Retrofit
    private lateinit var sendbirdService: SendbirdService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initDialog() {
        CreateAccountDialog(this, "create").show()

    }

    private fun initViews() = with(binding) {
        retrofit = Retrofit.Builder()
            .baseUrl(Key.API_REQUEST_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        sendbirdService = retrofit.create(SendbirdService::class.java)

        buttonSignup.setOnClickListener {
            initDialog()
        }

        buttonSignin.setOnClickListener {
            val id = tagEdittextUserId.text.toString()
            val password = tagEdittextUserPassword.text.toString()
            connectToServer(id, password)
        }
    }

    private fun connectToServer(userId: String, password: String) = launch {
        val response = sendbirdService.getOneUser(Key.CONTENT_TYPE, Key.API_TOKEN, userId)
        if (response.isSuccessful) {
            val token = response.body()?.metadata?.get(password).toString().replace("\"", "")
            Log.e("token", "${token}")
            if (token.equals("null")) {
                Log.e("login", "wrong password")
                withContext(Dispatchers.Main){
                    Toast.makeText(this@SignUpActivity, "패스워드가 틀렸습니다.", Toast.LENGTH_SHORT).show()
                }
                return@launch
            }
            SendbirdChat.connect(userId = userId, authToken = token) { user, e ->
                Log.e("username", "${user?.nickname}")
                startActivity(Intent(this@SignUpActivity, MainActivity::class.java).apply {
                    putExtra("userID", userId)
                    putExtra("password", password)
//                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                })
            }
        } else {
            withContext(Dispatchers.Main){
                Toast.makeText(this@SignUpActivity, "존재하지 않는 유저입니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + Job()
}

