package com.example.mycomposeapp.data.repository

import com.example.mycomposeapp.data.api.ApiService
import com.example.mycomposeapp.data.database.PostDao
import com.example.mycomposeapp.data.model.Post
import com.example.mycomposeapp.data.model.UpdatePostRequest
import kotlinx.coroutines.flow.Flow

class PostRepository(
    private val apiService: ApiService,
    private val postDao: PostDao
) {

    val posts: Flow<List<Post>> = postDao.getAllPosts()

    suspend fun refreshPosts(): Result<Unit> {
        return try {
            val response = apiService.getMyPosts()
            if (response.isSuccessful && response.body() != null) {
                val serverPosts = response.body()!!
                val syncedPosts = serverPosts.map { it.copy(isSynced = true) }

                postDao.clearAll() // Sterge doar cele vechi sincronizate
                postDao.insertAll(syncedPosts)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Refresh failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPostById(id: Long): Result<Post> {
        return try {
            val response = apiService.getPostById(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to fetch post"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createPost(description: String, photoUrl: String?, location: String?, userId: Long): Result<Post> {
        try {
            if (checkServerStatus()) {
                val response = apiService.createPost(description, photoUrl, location)
                if (response.isSuccessful && response.body() != null) {
                    val newPost = response.body()!!.copy(isSynced = true)
                    postDao.insertAll(listOf(newPost))
                    return Result.success(newPost)
                }
            }
        } catch (e: Exception) {}

        val offlineId = -System.currentTimeMillis()
        val offlinePost = Post(
            id = offlineId,
            userId = userId,
            description = description,
            photoUrl = photoUrl,
            location = location,
            createdAt = "Waiting for sync...",
            isSynced = false
        )

        postDao.insertAll(listOf(offlinePost))
        return Result.success(offlinePost)
    }

    suspend fun updatePost(id: Long, description: String, photoUrl: String?, location: String?): Result<Post> {
        return try {
            val request = UpdatePostRequest(description, photoUrl, location)
            val response = apiService.updatePost(id, request)
            if (response.isSuccessful && response.body() != null) {
                val updatedPost = response.body()!!.copy(isSynced = true) // Si aici e bine sa pui
                postDao.insertAll(listOf(updatedPost))
                Result.success(updatedPost)
            } else {
                Result.failure(Exception("Failed"))
            }
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun deletePost(id: Long): Result<Unit> {
        return try {
            val response = apiService.deletePost(id)
            if (response.isSuccessful) {
                postDao.deleteById(id)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed"))
            }
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun checkServerStatus(): Boolean {
        return try {
            val response = apiService.ping()
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}