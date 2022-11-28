package com.example.rsupportapprenticeship.Presentation.Adapter

import android.content.DialogInterface.OnClickListener
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rsupportapprenticeship.Data.UserResponse
import com.example.rsupportapprenticeship.R
import com.example.rsupportapprenticeship.databinding.ItemFriendBinding

class UserAdapter : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private var users = mutableListOf<UserResponse>()
    private lateinit var itemClickListener: (UserResponse) -> Unit

    inner class ViewHolder(
        private val binding: ItemFriendBinding,
        private val listener: (UserResponse) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        var check = false
        fun bind(user: UserResponse) {
            binding.friendId.text = user.user_id
            binding.friendNickname.text = user.nickname
            if (user.is_online) {
                binding.onlineStatus.setBackgroundResource(R.color.online)
            } else {
                binding.onlineStatus.setBackgroundResource(R.color.gray)
            }
            binding.root.setOnClickListener {
                listener(user)
                if(check){
                    binding.root.setBackgroundResource(R.color.white)
                    check = false
                }else{
                    binding.root.setBackgroundResource(R.color.purple_500)
                    check = true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view, itemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int = users.size

    fun setData(list: Array<UserResponse>, listener: (UserResponse) -> Unit) {
        users = list.toMutableList()
        itemClickListener = listener
        notifyDataSetChanged()
    }
}