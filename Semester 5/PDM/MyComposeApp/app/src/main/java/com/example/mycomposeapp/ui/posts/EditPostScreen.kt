package com.example.mycomposeapp.ui.posts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.mycomposeapp.data.repository.PostRepository
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPostScreen(
    repository: PostRepository,
    postId: Long? = null,
    onNavigateBack: () -> Unit
) {
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    // Dropdown Logic
    val photoOptions = listOf("No Image", "photo1.jpg", "photo2.jpg", "photo3.jpg", "photo4.jpg", "photo5.jpg", "photo6.jpg")
    var selectedPhotoName by remember { mutableStateOf(photoOptions[0]) }
    var expanded by remember { mutableStateOf(false) }

    // Map Logic
    var showMapDialog by remember { mutableStateOf(false) }
    val clujNapoca = LatLng(46.7712, 23.6236) // Default Center

    // Loading State
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(postId) {
        if (postId != null && postId != -1L) {
            val result = repository.getPostById(postId)
            result.onSuccess { post ->
                description = post.description ?: ""
                location = post.location ?: ""
                if (post.photoUrl != null && photoOptions.contains(post.photoUrl)) {
                    selectedPhotoName = post.photoUrl!!
                } else {
                    selectedPhotoName = "No Image"
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(if (postId == null || postId == -1L) "Create Post" else "Edit Post") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // --- IMAGE PREVIEW ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(bottom = 16.dp)
                    .background(Color.LightGray.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                if (selectedPhotoName == "No Image") {
                    Text("No image selected", color = Color.Gray)
                } else {
                    val currentImageId = getDrawableIdByName(selectedPhotoName)
                    Image(
                        painter = painterResource(id = currentImageId),
                        contentDescription = "Selected Image Preview",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // --- PHOTO DROPDOWN ---
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = selectedPhotoName,
                    onValueChange = { },
                    label = { Text("Select Photo") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    photoOptions.forEach { photoName ->
                        DropdownMenuItem(
                            text = { Text(text = photoName) },
                            onClick = {
                                selectedPhotoName = photoName
                                expanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- DESCRIPTION ---
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // --- LOCATION (CLICKABLE) ---
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location (Tap icon to map)") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { showMapDialog = true }) {
                        Icon(Icons.Default.LocationOn, contentDescription = "Pick Location")
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (errorMessage != null) {
                Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
            }

            // --- SAVE BUTTON ---
            Button(
                onClick = {
                    scope.launch {
                        isLoading = true
                        errorMessage = null
                        val finalPhotoUrl = if (selectedPhotoName == "No Image") null else selectedPhotoName

                        // Database Logic
                        if (postId == null || postId == -1L) {
                            repository.createPost(description, finalPhotoUrl, location, 0)
                                .onSuccess { onNavigateBack() }
                                .onFailure { errorMessage = it.message }
                        } else {
                            repository.updatePost(postId, description, finalPhotoUrl, location)
                                .onSuccess { onNavigateBack() }
                                .onFailure { errorMessage = it.message }
                        }
                        isLoading = false
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                Text(if (isLoading) "Saving..." else "Save")
            }
        }
    }

    // --- GOOGLE MAP DIALOG ---
    if (showMapDialog) {
        Dialog(onDismissRequest = { showMapDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp), // Height of the map dialog
                shape = MaterialTheme.shapes.large
            ) {
                Column {
                    // 1. Determine starting camera position
                    // Try to parse existing location, otherwise default to Cluj
                    val startPos = parseCoordinates(location) ?: clujNapoca
                    val cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(startPos, 13f)
                    }

                    var selectedLatLng by remember { mutableStateOf(parseCoordinates(location)) }

                    Box(modifier = Modifier.weight(1f)) {
                        GoogleMap(
                            modifier = Modifier.fillMaxSize(),
                            cameraPositionState = cameraPositionState,
                            onMapClick = { latLng ->
                                selectedLatLng = latLng
                            },
                            uiSettings = MapUiSettings(zoomControlsEnabled = true)
                        ) {
                            // Show marker if we have a selection
                            if (selectedLatLng != null) {
                                Marker(
                                    state = MarkerState(position = selectedLatLng!!),
                                    title = "Selected Location",
                                    snippet = "${selectedLatLng!!.latitude}, ${selectedLatLng!!.longitude}"
                                )
                            }
                        }
                    }

                    // 3. Buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showMapDialog = false }) {
                            Text("Cancel")
                        }
                        Button(onClick = {
                            if (selectedLatLng != null) {
                                // Save format: "46.123,23.123"
                                location = "${selectedLatLng!!.latitude},${selectedLatLng!!.longitude}"
                            }
                            showMapDialog = false
                        }) {
                            Text("Confirm")
                        }
                    }
                }
            }
        }
    }
}

