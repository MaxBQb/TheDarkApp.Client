package lab.maxb.dark.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.util.lerp
import com.google.accompanist.pager.HorizontalPagerIndicator
import lab.maxb.dark.ui.theme.spacing
import kotlin.math.absoluteValue


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageSlider(
    images: List<Any?>,
    modifier: Modifier = Modifier,
    pagerState: PagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) { images.size },
    onItemClick: () -> Unit = {},
    zoomable: Boolean = false,
) = Column(horizontalAlignment = Alignment.CenterHorizontally) {
    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(MaterialTheme.spacing.normal),
        modifier = modifier,
    ) { page ->
        RecognitionTaskImage(
            images[page],
            Modifier
                .fillMaxSize()
                .graphicsLayer {
                    val pageOffset = ((pagerState.currentPage - page)
                            + pagerState.currentPageOffsetFraction).absoluteValue

                    lerp(
                        start = 0.65f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    ).also { scale ->
                        scaleX = scale
                        scaleY = scale
                    }

                    alpha = lerp(
                        start = 0.3f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                }
                .clickable(onClick = onItemClick),
            zoomable = zoomable,
        )
    }
    AnimatedVisibility(visible = images.size > 1 && !zoomable) {
        HorizontalPagerIndicator(
            pagerState = pagerState,
            pageCount = images.size,
            modifier = Modifier.padding(MaterialTheme.spacing.extraSmall),
            activeColor = MaterialTheme.colorScheme.onBackground
        )
    }
}
