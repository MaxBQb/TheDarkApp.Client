package lab.maxb.dark.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuerySearch(
    modifier: Modifier = Modifier,
    query: String,
    label: @Composable (() -> Unit)? = null,
    onQueryChanged: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) = OutlinedTextField(
    modifier = modifier.fillMaxWidth(),
    value = query,
    onValueChange = onQueryChanged,
    label = label,
    singleLine = true,
    trailingIcon = {
        AnimatedVisibility(query.isNotEmpty()) {
            IconButton(onClick = { onQueryChanged("") }) {
                Icon(imageVector = Icons.Filled.Close, contentDescription = "Clear")
            }
        }
    },
    textStyle = MaterialTheme.typography.bodySmall,
    keyboardActions = keyboardActions,
    keyboardOptions = keyboardOptions,
)