package com.example.mycomposeapp.ui.posts

import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Modifier
import com.example.mycomposeapp.R
import com.google.android.gms.maps.model.LatLng
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.lazy.LazyListState
import kotlin.math.abs
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

fun getDrawableIdByName(imageName: String?): Int {
    return when (imageName) {
        "photo1.jpg" -> R.drawable.photo1
        "photo2.jpg" -> R.drawable.photo2
        "photo3.jpg" -> R.drawable.photo3
        "photo4.jpg" -> R.drawable.photo4
        "photo5.jpg" -> R.drawable.photo5
        "photo6.jpg" -> R.drawable.photo6
        else -> R.drawable.img_default
    }
}

fun parseCoordinates(locationString: String): LatLng? {
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


fun Modifier.scaleOnScroll(
    listState: LazyListState,
    itemIndex: Int,
    minScale: Float = 0.85f
): Modifier = composed {
    val scale by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visibleItemInfo = layoutInfo.visibleItemsInfo.find { it.index == itemIndex }

            if (visibleItemInfo != null) {
                val viewportCenter = layoutInfo.viewportEndOffset / 2f
                val itemCenter = visibleItemInfo.offset + (visibleItemInfo.size / 2f)

                val distanceFromCenter = abs(viewportCenter - itemCenter)

                val targetScale = 1f - (distanceFromCenter / viewportCenter) * (1f - minScale)

                targetScale.coerceIn(minScale, 1f)
            } else {
                1f
            }
        }
    }

    this.graphicsLayer {
        scaleX = scale
        scaleY = scale
        alpha = scale
    }
}