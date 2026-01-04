package com.example.mycomposeapp.data.api

import com.example.mycomposeapp.data.model.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("api/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/signup")
    suspend fun signup(@Body request: SignupRequest): Response<SignupResponse>

    @POST("api/logout")
    suspend fun logout(): Response<Map<String, String>>

    @GET("api/posts")
    suspend fun getMyPosts(): Response<List<Post>>

    @GET("api/posts/{id}")
    suspend fun getPostById(@Path("id") id: Long): Response<Post>

    @POST("api/posts")
    @FormUrlEncoded
    suspend fun createPost(
        @Field("description") description: String,
        @Field("photoUrl") photoUrl: String? = null,
        @Field("location") location: String? = null
    ): Response<Post>

    @PUT("api/posts/{id}")
    suspend fun updatePost(
        @Path("id") id: Long,
        @Body request: UpdatePostRequest
    ): Response<Post>

    @DELETE("api/posts/{id}")
    suspend fun deletePost(@Path("id") id: Long): Response<Unit>

    @GET("/api/ping")
    suspend fun ping(): Response<ResponseBody>
}