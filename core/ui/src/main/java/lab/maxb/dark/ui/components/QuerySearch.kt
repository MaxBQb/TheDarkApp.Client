package lab.maxb.dark.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import lab.maxb.dark.ui.extra.synchronizeChanges

@Composable
fun QuerySearch(
    modifier: Modifier = Modifier,
    query: String,
    label: @Composable (() -> Unit)? = null,
    onQueryChanged: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    val (synchronizedState, synchronizedOnChange) = synchronizeChanges(query, onQueryChanged)
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = synchronizedState,
        onValueChange = synchronizedOnChange,
        placeholder = label,
        singleLine = true,
        trailingIcon = {
            AnimatedVisibility(query.isNotEmpty()) {
                IconButton(onClick = { synchronizedOnChange("") }) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = "Clear")
                }
            }
        },
        textStyle = MaterialTheme.typography.bodySmall,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
    )
}
