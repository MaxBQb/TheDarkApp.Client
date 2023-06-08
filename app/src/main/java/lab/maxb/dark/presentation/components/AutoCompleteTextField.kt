package lab.maxb.dark.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import lab.maxb.dark.ui.theme.spacing


@Composable
fun <T> AutoCompleteTextField(
    query: String,
    modifier: Modifier = Modifier,
    queryLabel: @Composable (() -> Unit)? = null,
    onQueryChanged: (String) -> Unit = {},
    predictions: List<T>,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onItemClick: (T) -> Unit = {},
    itemContent: @Composable (T) -> Unit = {}
) {
    val view = LocalView.current
    val lazyListState = rememberLazyListState()
    LazyColumn(
        state = lazyListState,
        modifier = Modifier.heightIn(max = TextFieldDefaults.MinHeight * 6)
    ) {
        item {
            QuerySearch(
                query = query,
                label = queryLabel,
                modifier = modifier,
                onQueryChanged = onQueryChanged,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
            )
        }

        if (predictions.isNotEmpty()) {
            items(predictions) { prediction ->
                Row(
                    Modifier
                        .padding(MaterialTheme.spacing.normal)
                        .fillMaxWidth()
                        .clickable {
                            view.clearFocus()
                            onItemClick(prediction)
                        }
                ) {
                    itemContent(prediction)
                }
            }
        }
    }
}