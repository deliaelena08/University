package com.example.mycomposeapp.ui.posts

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycomposeapp.data.model.Post
import com.example.mycomposeapp.data.repository.PostRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

class PostListViewModel(private val repository: PostRepository) : ViewModel() {

    private var allPosts by mutableStateOf<List<Post>>(emptyList())

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    var sessionExpired by mutableStateOf(false)

    var searchQuery by mutableStateOf("")
    var isPhotoFilterActive by mutableStateOf(false)

    var isServerOnline by mutableStateOf(false)

    val filteredPosts: List<Post>
        get() {
            var result = allPosts
            if (searchQuery.isNotEmpty()) {
                result = result.filter {
                    it.description?.contains(searchQuery, ignoreCase = true) == true
                }
            }
            if (isPhotoFilterActive) {
                result = result.filter { !it.photoUrl.isNullOrEmpty() }
            }
            return result
        }

    init {
        viewModelScope.launch {
            repository.posts.collect { listFromDb ->
                allPosts = listFromDb
            }
        }
        loadPosts()
        startHealthCheck()
    }

    fun loadPosts() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            val result = repository.refreshPosts()

            result.onFailure { e ->
                if (e.message?.contains("401") == true || e.message?.contains("403") == true) {
                    sessionExpired = true
                } else {
                    errorMessage = "No network connection"
                }
            }

            isLoading = false
        }
    }

    fun deletePost(post: Post) {
        viewModelScope.launch {
            repository.deletePost(post.id).onFailure {
                errorMessage = "Delete failed: ${it.message}"
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        searchQuery = query
    }

    fun onPhotoFilterChanged(isActive: Boolean) {
        isPhotoFilterActive = isActive
    }

    private fun startHealthCheck() {
        viewModelScope.launch {
            while (isActive) {
                val isOnline = repository.checkServerStatus()

                isServerOnline = isOnline

                if (isOnline) {
                    if (errorMessage != null) {
                        errorMessage = null
                        loadPosts()
                    }
                }
                delay(3000)
            }
        }
    }
}