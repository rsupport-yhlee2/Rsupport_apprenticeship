package com.example.rsupportapprenticeship.Data

import com.google.gson.annotations.SerializedName

class UsersResponse (
    @SerializedName("users") val users: Array<UserResponse>,
    @SerializedName("next") val next: String
    )