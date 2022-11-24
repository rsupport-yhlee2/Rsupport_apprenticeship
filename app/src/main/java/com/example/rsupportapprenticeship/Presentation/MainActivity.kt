package com.example.rsupportapprenticeship.Presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.rsupportapprenticeship.Data.SendbirdService
import com.example.rsupportapprenticeship.Key
import com.example.rsupportapprenticeship.R
import com.example.rsupportapprenticeship.databinding.ActivityMainBinding
import com.sendbird.android.SendbirdChat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
    lateinit var binding: ActivityMainBinding
    private lateinit var retrofit: Retrofit
    private lateinit var sendbirdService: SendbirdService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() = with(binding) {
        retrofit = Retrofit.Builder()
            .baseUrl(Key.API_REQUEST_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        sendbirdService = retrofit.create(SendbirdService::class.java)
        launch {
            val response = sendbirdService.getOneUser(
                Key.CONTENT_TYPE,
                Key.API_TOKEN,
                SendbirdChat.currentUser?.userId!!
            )
            response.body().let { user ->
                intent.putExtra("nickname", user?.nickname)
                intent.putExtra("userID", user?.user_id)
            }
        }
        showFragment(ChatListFragment())
        bottomNav.run {
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.chat -> {
                        showFragment(ChatListFragment())
                    }
                    R.id.friends -> {
                        showFragment(FriendFragment())
                    }
                    R.id.profile -> {
                        showFragment(ProfileFragment())
                    }
                    else -> false
                }
            }
        }
    }

    private fun showFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment)
            commit()
        }
        return true
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + Job()
}