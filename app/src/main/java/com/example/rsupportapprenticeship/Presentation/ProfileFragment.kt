package com.example.rsupportapprenticeship.Presentation

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.rsupportapprenticeship.Data.SendbirdService
import com.example.rsupportapprenticeship.Data.UserResponse
import com.example.rsupportapprenticeship.Key
import com.example.rsupportapprenticeship.Presentation.Adapter.UserAdapter
import com.example.rsupportapprenticeship.R
import com.example.rsupportapprenticeship.databinding.FragmentProfileBinding
import com.sendbird.android.SendbirdChat
import com.sendbird.android.exception.SendbirdException
import com.sendbird.android.handler.FriendChangeLogsByTokenHandler
import com.sendbird.android.handler.UsersHandler
import com.sendbird.android.params.UserUpdateParams
import com.sendbird.android.user.User
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.CoroutineContext

class ProfileFragment : Fragment(R.layout.fragment_profile), CoroutineScope {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var retrofit: Retrofit
    private lateinit var sendbirdService: SendbirdService
    private lateinit var users: Array<UserResponse>
    private val adapter = UserAdapter()
    private val selectedUser = mutableListOf<String>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
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
        userList.adapter = adapter
        Glide.with(profileImage)
            .load(SendbirdChat.currentUser?.profileUrl)
            .into(profileImage)
        userList.layoutManager = LinearLayoutManager(requireContext())
        val job = launch {
            val response = sendbirdService.getUsers(Key.CONTENT_TYPE, Key.API_TOKEN)
            response.body()?.let {
                users = it.users
                withContext(Dispatchers.Main) {
                    adapter.setData(users) {
                        val id = it.user_id
                        selectUser(id)
                    }
                }
                users.forEach {
                    Log.e("users", "${it.user_id}")
                }
            }
        }
        val id = requireActivity().intent?.getStringExtra("userID")
        val nickname = requireActivity().intent?.getStringExtra("nickname")
        retrofit = Retrofit.Builder()
            .baseUrl(Key.API_REQUEST_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        sendbirdService = retrofit.create(SendbirdService::class.java)

        profileID.text = id
        profileNickname.text = nickname
        profileUpdateButton.setOnClickListener {
            val job = initDialog()
            if (job.isCompleted) requireActivity().finish()
        }
        profileDeleteButton.setOnClickListener {
            AlertDialog.Builder(requireContext()).apply {
                setTitle("DeleteUser")
                    .setPositiveButton(
                        "yes"
                    ) { dialog, which -> deleteUser() }
                    .setNegativeButton("no") { _, _ ->

                    }.show()
            }
        }
        signOutButton.setOnClickListener {
            Toast.makeText(requireContext(), "로그아웃", Toast.LENGTH_SHORT).show()
            SendbirdChat.disconnect() {
                requireActivity().finish()
            }
        }
        addFriendButton.setOnClickListener {
            addFriends(selectedUser)
            Toast.makeText(requireContext(), "친구추가 완료", Toast.LENGTH_SHORT).show()
        }
        setProfile.setOnClickListener {
            val input = EditText(requireContext())
            AlertDialog.Builder(requireContext()).apply {
                setTitle("ProfileURL")
                setView(input)
                setPositiveButton("yes") { dialog, which ->
                    Toast.makeText(requireContext(), "프로필 사진 변경", Toast.LENGTH_SHORT).show()
                    updateUserProfile(input.text.toString())
                    val main = activity as MainActivity
                    main.showFragment(ProfileFragment())
                }
                setNegativeButton("no") { _, _ -> }
                show()
            }
        }
    }

    private fun addFriends(users: List<String>) {
        Log.e("friend12421515135", "${users}")
        SendbirdChat.addFriends(users) { users, e ->
            Log.e("friend", "${users}")
            val main = activity as MainActivity
            main.showFragment(ProfileFragment())
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

    private fun deleteUser() = launch {
        val id = requireActivity().intent?.getStringExtra("userID").toString()
        sendbirdService.deleteUser(Key.CONTENT_TYPE, Key.API_TOKEN, id)
        requireActivity().finish()
    }

    private fun initDialog() = launch {
        withContext(Dispatchers.Main) {
            CreateAccountDialog(requireContext(), "update").show()
        }
    }

    private fun updateUserProfile(profileUrl: String) {
        val params = UserUpdateParams().apply {
            this.profileImageUrl = profileUrl
        }
        SendbirdChat.updateCurrentUserInfo(params) { e ->
            if (e != null) {

            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + Job()
}