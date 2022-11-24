package com.example.rsupportapprenticeship.Data

import retrofit2.Response
import retrofit2.http.*

interface SendbirdService {

    @GET("/v3/users")
    suspend fun getUsers(
        @Header("Content-Type") type: String,
        @Header("Api-Token") token: String
    ) : Response<UsersResponse>

    @GET("/v3/users/{user_id}")
    suspend fun getOneUser(
        @Header("Content-Type") type: String,
        @Header("Api-Token") token: String,
        @Path("user_id") user_id: String
    ) : Response<UserResponse>

    @POST("/v3/users")
    suspend fun createOneUser(
        @Header("Content-Type") type: String,
        @Header("Api-Token") token: String,
        @Body setInfo : UserDTO
    )

    @DELETE("/v3/users/{user_id}")
    suspend fun deleteUser(
        @Header("Content-Type") type: String,
        @Header("Api-Token") token: String,
        @Path("user_id") user_id: String
    )

}