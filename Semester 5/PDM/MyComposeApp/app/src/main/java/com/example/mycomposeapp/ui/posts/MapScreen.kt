package com.example.mycomposeapp.ui.posts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mycomposeapp.data.repository.PostRepository
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MapScreen(
    repository: PostRepository,
    onClose: () -> Unit
) {
    val posts by repository.posts.collectAsState(initial = emptyList())

    val clujNapoca = LatLng(46.7712, 23.6236)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(clujNapoca, 12f)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(zoomControlsEnabled = true)
        ) {
            posts.forEach { post ->
                val latLng = parseLocation(post.location)
                if (latLng != null) {
                    Marker(
                        state = MarkerState(position = latLng),
                        title = post.description ?: "Post",
                        snippet = "Click pentru detalii"
                    )
                }
            }
        }

        SmallFloatingActionButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            Icon(Icons.Default.Close, contentDescription = "Close Map")
        }
    }
}

fun parseLocation(locationString: String?): LatLng? {
    if (locationString.isNullOrEmpty()) return null
    return try {
        val parts = locationString.split(",")
        if (parts.size == 2) {
            val lat = parts[0].trim().toDouble()
            val lng = parts[1].trim().toDouble()
            LatLng(lat, lng)
        } else {
            null
        }
    } catch (e: Exception) {
        null
    }
}