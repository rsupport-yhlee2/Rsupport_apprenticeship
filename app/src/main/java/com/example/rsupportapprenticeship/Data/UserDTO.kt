package com.example.rsupportapprenticeship.Data

data class UserDTO(
    val user_id: String,
    val nickname: String,
    val profile_url: String,
    val profile_file: String,
    val issue_access_token: Boolean
)