package com.example.rsupportapprenticeship.Presentation

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.rsupportapprenticeship.Data.SendbirdService
import com.example.rsupportapprenticeship.Key
import com.example.rsupportapprenticeship.R
import com.example.rsupportapprenticeship.databinding.FragmentProfileBinding
import com.sendbird.android.SendbirdChat
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.CoroutineContext

class ProfileFragment : Fragment(R.layout.fragment_profile), CoroutineScope {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var retrofit: Retrofit
    private lateinit var sendbirdService: SendbirdService

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
            SendbirdChat.disconnect(){
                requireActivity().finish()
            }
        }
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

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + Job()
}