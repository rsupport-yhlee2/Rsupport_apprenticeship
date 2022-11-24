package com.example.rsupportapprenticeship.Data

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

class UserResponse (
    @SerializedName("user_id") val user_id: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("profile_url") val profile_url: String,
    @SerializedName("access_token") val access_token: String,
    @SerializedName("has_ever_logged_in") val has_ever_logged_in: Boolean,
    @SerializedName("is_active") val is_active: Boolean,
    @SerializedName("is_online") val is_online: Boolean,
    @SerializedName("metadata") val metadata: JsonObject,
)