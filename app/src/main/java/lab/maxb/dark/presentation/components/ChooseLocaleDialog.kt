package lab.maxb.dark.presentation.components

import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.os.LocaleListCompat
import com.vanpra.composematerialdialogs.*
import lab.maxb.dark.R
import lab.maxb.dark.ui.theme.DarkAppTheme

@Composable
fun ChooseLocaleDialog(
    dialogState: MaterialDialogState = rememberMaterialDialogState(),
    selectedLocale: String = "",
    onLocaleChosen: (String) -> Unit = {},
) {
    val defaultValueText = stringResource(R.string.dialogChooseLocale_systemDefault)
    val (names, keys) = getLocalesMap(defaultValueText)
    var selectedItem by remember(selectedLocale) {
        mutableStateOf(keys.indexOf(selectedLocale))
    }
    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            positiveButton(stringResource(R.string.dialogChooseLocale_button_apply)) {
                onLocaleChosen(keys[selectedItem])
            }
            negativeButton(stringResource(R.string.dialogChooseLocale_button_cancel))
        },
    ) {
        title(res = R.string.dialogChooseLocale_title)
        listItemsSingleChoice(
            list = names,
            initialSelection = selectedItem,
        ) {
            selectedItem = it
        }
    }
}


@Composable
private fun getLocalesMap(defaultValueText: String) = remember(defaultValueText) {
    val locales = LocaleListCompat.getDefault()
    val names = mutableListOf(defaultValueText)
    val keys = mutableListOf("")
    for (i in 0 until locales.size()) {
        val item = locales.get(i)!!
        names.add(item.displayLanguage.replaceFirstChar(Char::titlecase))
        keys.add(item.toLanguageTag())
    }
    names to keys
}

@Preview
@Composable
fun ChooseLocaleDialogPreview() = DarkAppTheme {
    val dialogState = rememberMaterialDialogState()
    ChooseLocaleDialog(dialogState)
    dialogState.show()
}