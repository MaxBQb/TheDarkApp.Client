package lab.maxb.dark.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    imageOptions: ImageOptions = ImageOptions(contentScale = ContentScale.Inside)
) = GlideImage(
    imageModel,
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

@Composable
fun LoadingError(modifier: Modifier = Modifier) {
    Image(
        painterResource(R.drawable.ic_error),
        null,
        modifier
    )
}