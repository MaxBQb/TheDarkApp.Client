package lab.maxb.dark.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.animation.crossfade.CrossfadePlugin
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.glide.GlideImage
import lab.maxb.dark.R
import lab.maxb.dark.ui.theme.units.sdp

@Composable
fun RecognitionTaskImage(
    imageModel: Any?,
    modifier: Modifier = Modifier,
    imageOptions: ImageOptions = ImageOptions(contentScale = ContentScale.Inside),
    zoomable: Boolean = false,
) {
    if (zoomable)
        ZoomableBox(minScale = 1f) {
            _RecognitionTaskImage(
                imageModel,
                modifier.graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offsetX,
                    translationY = offsetY
                ),
                imageOptions,
            )
        }
    else
        _RecognitionTaskImage(
            imageModel,
            modifier,
            imageOptions,
        )
}

@Composable
private fun _RecognitionTaskImage(
    imageModel: Any?,
    modifier: Modifier,
    imageOptions: ImageOptions
) {
    GlideImage(
        { imageModel },
        modifier = modifier,
        imageOptions = imageOptions,
        failure = {
            LoadingError(
                Modifier
                    .fillMaxSize()
                    .padding(16.sdp)
            )
        },
        loading = {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingCircle(0.7f)
            }
        },
        component = rememberImageComponent {
            +CrossfadePlugin(duration = 1500)
        }
    )
}

@Composable
fun LoadingError(modifier: Modifier = Modifier) {
    Image(
        painterResource(R.drawable.ic_error),
        null,
        modifier
    )
}
