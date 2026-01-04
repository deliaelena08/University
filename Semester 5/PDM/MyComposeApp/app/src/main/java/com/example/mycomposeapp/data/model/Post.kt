package com.example.mycomposeapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class Post(
    @PrimaryKey
    val id: Long,

    val userId: Long? = null,
    val description: String?,
    val photoUrl: String? = null,
    val location: String? = null,
    val createdAt: String? = null,
    val isSynced: Boolean = true
)

data class CreatePostRequest(
    val description: String,
    val photoUrl: String? = null,
    val location: String? = null,
    val userId: Long
)

data class UpdatePostRequest(
    val description: String,
    val photoUrl: String? = null,
    val location: String? = null
)