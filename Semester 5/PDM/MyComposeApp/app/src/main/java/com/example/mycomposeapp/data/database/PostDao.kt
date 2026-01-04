package com.example.mycomposeapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mycomposeapp.data.model.Post
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {

    @Query("SELECT * FROM posts ORDER BY isSynced ASC, id DESC")
    fun getAllPosts(): Flow<List<Post>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<Post>)

    @Query("DELETE FROM posts WHERE isSynced = 1")
    suspend fun clearAll()

    @Query("DELETE FROM posts WHERE id = :postId")
    suspend fun deleteById(postId: Long)

    @Query("SELECT * FROM posts WHERE isSynced = 0")
    suspend fun getUnsyncedPosts(): List<Post>
}