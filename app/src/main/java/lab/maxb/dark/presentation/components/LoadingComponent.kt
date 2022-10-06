package lab.maxb.dark.presentation.components

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import lab.maxb.dark.presentation.extra.Result
import lab.maxb.dark.presentation.extra.asString
import lab.maxb.dark.presentation.extra.uiTextOf
import lab.maxb.dark.ui.theme.DarkAppTheme
import lab.maxb.dark.ui.theme.spacing

@Composable
fun <T> LoadingComponent(
    result: Result<T>,
    content: @Composable (T) -> Unit,
) = when(result) {
    is Result.Loading -> LoadingScreen(true)
    is Result.Error -> AnimateAppearance {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            LoadingError(
                Modifier
                    .fillMaxSize(0.5f)
                    .padding(MaterialTheme.spacing.extraSmall)
            )
            Text(text = result.message.asString())
        }
    }
    is Result.Success<T> -> AnimateAppearance(
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        content(result.value)
    }
}

@Preview
@Composable
private fun Success() = DarkAppTheme {
    Surface {
        LoadingComponent(result = Result.Success("Hello")) {
            Text(it)
        }
    }
}

@Preview
@Composable
private fun Loading() = DarkAppTheme {
    Surface {
        LoadingComponent<String>(result = Result.Loading) {
            Text(it)
        }
    }
}


@Preview
@Composable
private fun Error() = DarkAppTheme {
    Surface {
        LoadingComponent<String>(
            result = Result.Error(
                null, uiTextOf("Some error occurred")
            )
        ) {
            Text(it)
        }
    }
}
