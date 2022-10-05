package lab.maxb.dark.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import lab.maxb.dark.R
import lab.maxb.dark.ui.theme.spacing

@Composable
fun ImageListEditPanel(
    onAdd: () -> Unit = {},
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {},
    allowAddition: Boolean = true,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painterResource(R.drawable.ic_delete),
            "",
            modifier = Modifier
                .padding(MaterialTheme.spacing.small)
                .clickable(onClick = onDelete),
        )
        Image(
            painterResource(R.drawable.ic_edit),
            "",
            modifier = Modifier
                .padding(MaterialTheme.spacing.small)
                .clickable(onClick = onEdit),
        )
        AnimatedVisibility(
            visible = allowAddition,
        ) {
            Image(
                painterResource(R.drawable.ic_add_photo),
                "",
                modifier = Modifier
                    .padding(MaterialTheme.spacing.small)
                    .clickable(onClick = onAdd),
            )
        }
    }
}