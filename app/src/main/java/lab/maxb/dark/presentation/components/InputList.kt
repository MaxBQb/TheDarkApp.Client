package lab.maxb.dark.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onPreviewKeyEvent
import lab.maxb.dark.presentation.components.utils.keyboardClose
import lab.maxb.dark.presentation.components.utils.keyboardNext
import lab.maxb.dark.presentation.extra.ItemHolder
import lab.maxb.dark.ui.theme.spacing

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InputList(
    values: List<ItemHolder<String>>,
    modifier: Modifier = Modifier,
    queryLabel: (@Composable () -> Unit)? = null,
    onValueChanged: (ItemHolder<String>) -> Unit = {},
    suggestions: List<String> = emptyList(),
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        contentPadding = PaddingValues(MaterialTheme.spacing.normal)
    ) {
        itemsIndexed(values, key = { pos, item -> item.id }) { pos, item ->
            val keyboardActions = if (item.value.isBlank())
                keyboardClose
            else keyboardNext
            AnimateAppearance(
                modifier = Modifier.animateItemPlacement(),
                initiallyVisible = item.value.isNotBlank()
            ) {
                AutoCompleteTextField(
                    query = item.value,
                    queryLabel = queryLabel,
                    onQueryChanged = { onValueChanged(item.copy(value = it)) },
                    predictions = suggestions,
                    keyboardOptions = keyboardActions.options,
                    keyboardActions = keyboardActions.actions,
                    modifier = Modifier
                        .onPreviewKeyEvent(keyboardActions.event)
                        .padding(vertical = MaterialTheme.spacing.extraSmall)
                )
            }
        }
    }
}