package com.example.mycomposeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mycomposeapp.data.api.RetrofitClient
import com.example.mycomposeapp.data.api.WebSocketManager
import com.example.mycomposeapp.data.database.AppDatabase
import com.example.mycomposeapp.data.repository.AuthRepository
import com.example.mycomposeapp.data.repository.PostRepository
import com.example.mycomposeapp.data.repository.TokenManager
import com.example.mycomposeapp.ui.login.LoginScreen
import com.example.mycomposeapp.ui.posts.EditPostScreen
import com.example.mycomposeapp.ui.posts.MapScreen
import com.example.mycomposeapp.ui.posts.PostListScreen
import com.example.mycomposeapp.worker.SyncWorker
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = PeriodicWorkRequestBuilder<SyncWorker>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "SyncPostsWork",
            ExistingPeriodicWorkPolicy.KEEP,
            syncRequest
        )

        //OneTimeRequest
//        val oneTimeRequest = androidx.work.OneTimeWorkRequestBuilder<SyncWorker>()
//             .setConstraints(constraints)
//             .build()
//        WorkManager.getInstance(applicationContext).enqueue(oneTimeRequest)


        setContent {
            val context = LocalContext.current
            val tokenManager = remember { TokenManager(context) }
            val scope = rememberCoroutineScope()
            val snackbarHostState = remember { SnackbarHostState() }

            val database = remember { AppDatabase.getDatabase(context) }
            val postDao = remember { database.postDao() }

            val tokenProvider = suspend { tokenManager.token.first() }
            val apiService = remember { RetrofitClient.create(tokenProvider) }
            val webSocketManager = remember { WebSocketManager(tokenManager) }
            val authRepository = remember { AuthRepository(apiService, tokenManager) }
            val postRepository = remember { PostRepository(apiService, postDao) }

            val navController = rememberNavController()
            var startDestination by remember { mutableStateOf<String?>(null) }
            var refreshTrigger by remember { mutableIntStateOf(0) }

            LaunchedEffect(Unit) {
                webSocketManager.messageFlow.collect { message ->
                    snackbarHostState.showSnackbar(
                        message = "New update available!",
                        duration = SnackbarDuration.Short
                    )
                    refreshTrigger++
                }
            }

            LaunchedEffect(Unit) {
                val hasLocalToken = authRepository.isUserLoggedIn()
                if (hasLocalToken) {
                    val result = postRepository.refreshPosts()
                    if (result.isSuccess) {
                        webSocketManager.connect()
                        startDestination = "list"
                    } else {
                        startDestination = "list"
                    }
                } else {
                    startDestination = "login"
                }
            }

            if (startDestination != null) {
                Scaffold(
                    snackbarHost = {
                        SnackbarHost(
                            hostState = snackbarHostState,
                            modifier = Modifier.padding(16.dp)
                        ) { data ->
                            Surface(
                                shadowElevation = 8.dp,
                                shape = RoundedCornerShape(16.dp),
                                color = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Notifications,
                                        contentDescription = "Notification",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "Feed Update",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = data.visuals.message,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        NavHost(navController = navController, startDestination = startDestination!!) {
                            composable("login") {
                                LoginScreen(
                                    repository = authRepository,
                                    onLoginSuccess = {
                                        webSocketManager.connect()
                                        navController.navigate("list") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    }
                                )
                            }
                            composable("list") {
                                PostListScreen(
                                    repository = postRepository,
                                    refreshTrigger = refreshTrigger,
                                    onPostClick = { postId -> navController.navigate("edit/$postId") },
                                    onAddClick = { navController.navigate("edit/-1") },
                                    onLogoutClick = {
                                        scope.launch {
                                            authRepository.logout()
                                            webSocketManager.disconnect()
                                            navController.navigate("login") {
                                                popUpTo(0) { inclusive = true }
                                            }
                                        }
                                    },
                                    onMapClick = { navController.navigate("map") }
                                )
                            }
                            composable(
                                route = "edit/{postId}",
                                arguments = listOf(navArgument("postId") { type = NavType.LongType })
                            ) { backStackEntry ->
                                val postId = backStackEntry.arguments?.getLong("postId")
                                EditPostScreen(
                                    repository = postRepository,
                                    postId = postId,
                                    onNavigateBack = { navController.popBackStack() }
                                )
                            }
                            composable("map") {
                                MapScreen(
                                    repository = postRepository,
                                    onClose = { navController.popBackStack() }
                                )
                            }
                        }
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}