package com.example.rsupportapprenticeship.Presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rsupportapprenticeship.Data.SendbirdService
import com.example.rsupportapprenticeship.Data.UserResponse
import com.example.rsupportapprenticeship.Key
import com.example.rsupportapprenticeship.Presentation.Adapter.UserAdapter
import com.example.rsupportapprenticeship.R
import com.example.rsupportapprenticeship.databinding.FragmentFriendBinding
import com.google.gson.JsonObject
import com.sendbird.android.SendbirdChat
import com.sendbird.android.exception.SendbirdException
import com.sendbird.android.handler.CompletionHandler
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.CoroutineContext
import kotlin.math.min

class FriendFragment : Fragment(R.layout.fragment_friend), CoroutineScope {

    private lateinit var binding: FragmentFriendBinding
    private lateinit var retrofit: Retrofit
    private lateinit var sendbirdService: SendbirdService
    private val adapter = UserAdapter()
    private lateinit var users: Array<UserResponse>
    private val usersID = mutableListOf<String>()
    private val selectedUser = mutableListOf<String>()
    private val userList = mutableListOf<String>()
    private val friendIDList = mutableListOf<String>()
    private val friendList = mutableListOf<UserResponse>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFriendBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() = with(binding) {
        retrofit = Retrofit.Builder()
            .baseUrl(Key.API_REQUEST_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        sendbirdService = retrofit.create(SendbirdService::class.java)
        friendList.adapter = adapter
        friendList.layoutManager = LinearLayoutManager(requireContext())
        launch {
            val response = sendbirdService.getUsers(Key.CONTENT_TYPE, Key.API_TOKEN)
            response.body()?.let {
                users = it.users
                users.forEach {
                    usersID.add(it.user_id)
                }
                readFriends()
            }
        }
        deleteFriendButton.setOnClickListener {
            deleteFriends(selectedUser)
            adapter.notifyDataSetChanged()
            val main = activity as MainActivity
            main.showFragment(FriendFragment())
            Toast.makeText(requireContext(),"친구삭제 완료", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteFriends(users: List<String>) {
        SendbirdChat.deleteFriends(users) {

        }
    }

    private fun readFriends() {
        SendbirdChat.getFriendChangeLogsByToken(
            ""
        ) { updatedUsers, deletedUserIds, hasMore, token, e ->
            updatedUsers?.forEach {
                val id = it.userId
                userList.add(id)
            }
            userList.forEach {
                if (usersID.contains(it)) {
                    friendIDList.add(it)
                }
            }
            friendIDList.forEach {
                launch {
                    val friend = sendbirdService.getOneUser(Key.CONTENT_TYPE, Key.API_TOKEN, it)
                    friend.body()?.let {
                        friendList.add(it)
                    }
                    withContext(Dispatchers.Main) {
                        adapter.setData(friendList.toTypedArray()) {
                            selectUser(it.user_id)
                        }
                    }
                }
            }
        }
    }

    private fun selectUser(id: String) {
        if (selectedUser.contains(id)) {
            selectedUser.remove(id)
        } else {
            selectedUser.add(id)
        }
        Log.e("select", "${selectedUser}")
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + Job()
}