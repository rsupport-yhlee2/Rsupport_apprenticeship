package com.example.rsupportapprenticeship.Presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.rsupportapprenticeship.R
import com.example.rsupportapprenticeship.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }
    private fun initViews() = with(binding){
        showFragment(ChatListFragment())
        bottomNav.run {
            setOnItemSelectedListener { item ->
                when(item.itemId){
                    R.id.chat -> {showFragment(ChatListFragment())}
                    R.id.friends ->{showFragment(FriendFragment())}
                    R.id.profile ->{showFragment(ProfileFragment())}
                    else -> false
                }
            }
        }
    }

    private fun showFragment(fragment: Fragment): Boolean{
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container,fragment)
            commit()
        }
        return true
    }
}