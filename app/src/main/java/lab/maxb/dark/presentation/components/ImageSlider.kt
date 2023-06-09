package lab.maxb.dark.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.util.lerp
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import lab.maxb.dark.ui.theme.spacing
import kotlin.math.absoluteValue


@Composable
fun ImageSlider(
    images: List<Any?>,
    modifier: Modifier = Modifier,
    pagerState: PagerState = rememberPagerState(),
    onItemClick: () -> Unit = {},
    zoomable: Boolean = false,
) = Column(horizontalAlignment = Alignment.CenterHorizontally) {
    HorizontalPager(
        count = images.size,
        state = pagerState,
        contentPadding = PaddingValues(MaterialTheme.spacing.normal),
        modifier = modifier,
    ) { page ->
        RecognitionTaskImage(
            images[page],
            Modifier
                .fillMaxSize()
                .clickable(onClick = onItemClick)
                .graphicsLayer {
                    val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                    lerp(
                        start = 0.85f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    ).also { scale ->
                        scaleX = scale
                        scaleY = scale
                    }

                    alpha = lerp(
                        start = 0.5f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                },
            zoomable=zoomable,
        )
    }
    AnimatedVisibility(visible = images.size > 1 && !zoomable) {
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier.padding(MaterialTheme.spacing.extraSmall),
            activeColor = MaterialTheme.colorScheme.onBackground
        )
    }
}