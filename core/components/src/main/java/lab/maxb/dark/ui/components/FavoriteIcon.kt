package lab.maxb.dark.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import lab.maxb.dark.ui.core.R


@Composable
fun FavoriteIcon(
    value: Boolean,
    modifier: Modifier = Modifier,
    onChanged: (Boolean) -> Unit,
) = IconButton(modifier=modifier, onClick = { onChanged(!value) }) {
    AnimatedScaleToggle(value) {
        Icon(
            painterResource(
                if (it) R.drawable.ic_favorite
                else R.drawable.ic_not_favorite
            ), null,
        )
    }
}