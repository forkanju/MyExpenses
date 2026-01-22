package ngo.friendship.mhealth.dc.presentation.base

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import coil3.compose.SubcomposeAsyncImage
import ngo.friendship.mhealth.dc.theme.Dimen

/**
 * A composable function that displays an image viewer dialog with pan and zoom functionality.
 *
 * This dialog allows the user to:
 * - View an image provided by the `image` parameter.
 * - Zoom in and out of the image using pinch gestures or double-tap.
 * - Pan the image when zoomed in by dragging.
 * - Close the dialog by tapping the close button.
 *
 * @param image The image to be displayed. Can be any type supported by Coil's SubcomposeAsyncImage,
 *              such as a URL string, a drawable resource ID, a File, or a Uri.
 * @param onDismissRequest Callback invoked when the user requests to dismiss the dialog, typically by
 *                         tapping the close button.
 */
@Composable
fun ImageViewerDialog(
    image: Any,
    onDismissRequest: () -> Unit
) {
    var scale by remember{ mutableFloatStateOf(1f) }
    var offset by remember{ mutableStateOf(Offset(0f, 0f)) }

    Box {
        SubcomposeAsyncImage(
            model = image,
            loading = {
                LoadingIndicator(modifier = Modifier.padding(Dimen.Standard).alpha(.5f))
            },
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
//                .padding(10.dp)
//                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.onPrimaryContainer.copy(.2f))
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        // Update the scale based on zoom gestures.
                        scale *= zoom
                        // Limit the zoom levels within a certain range (optional).
                        scale = scale.coerceIn(0.5f, 3f)
                        // Update the offset to implement panning when zoomed.
                        offset = if (scale == 1f) Offset(0f, 0f) else offset + pan
                    }
                }.pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            // Toggle zoom state on double tap
                            if (scale == 1f) {
                                scale = 2f // Zoom in
                            } else {
                                scale = 1f // Zoom out to initial state
                                offset = Offset(0f, 0f) // Reset offset when zooming out
                            }
                        }
                    )
                }.graphicsLayer(
                    scaleX = scale, scaleY = scale,
                    translationX = offset.x, translationY = offset.y
                )
        )
        IconButton(onClick = onDismissRequest, modifier = Modifier.align(Alignment.TopEnd)) {
            Icon(Icons.Rounded.Close, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer)
        }
    }

}