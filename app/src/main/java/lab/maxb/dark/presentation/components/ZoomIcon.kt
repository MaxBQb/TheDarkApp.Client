package lab.maxb.dark.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import lab.maxb.dark.R


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ZoomIcon(
    value: Boolean,
    modifier: Modifier = Modifier,
    onChanged: (Boolean) -> Unit,
) {
    var state by remember { mutableStateOf(value) }
    state = value
    IconButton(onClick = { onChanged(!value) }) {
        AnimatedContent(
            targetState = state,
            modifier = modifier,
            transitionSpec = {
                EnterTransition.None togetherWith ExitTransition.None
            },
        ) {
            if (it)
                Icon(
                    painterResource(R.drawable.ic_zoom_on), null,
                    modifier = modifier.animateEnterExit(
                        enter = scaleIn(),
                        exit = scaleOut()
                    ),
                )
            else
                Icon(
                    painterResource(R.drawable.ic_zoom_off), null,
                    modifier = modifier.animateEnterExit(
                        enter = scaleIn(),
                        exit = scaleOut()
                    ),
                )
        }
    }
}