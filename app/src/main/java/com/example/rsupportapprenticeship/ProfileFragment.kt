package com.example.rsupportapprenticeship

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.rsupportapprenticeship.databinding.FragmentProfileBinding

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding
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

    private fun initViews() = with(binding){
        profileID.text = "im yunho test"
        profileUpdateButton.setOnClickListener {
            initDialog()
        }
        profileDeleteButton.setOnClickListener {
            AlertDialog.Builder(requireContext())
        }
    }

    private fun initDialog() {
        CreateAccountDialog(requireContext(),"update").show()
    }
}