package lab.maxb.dark.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.listItemsSingleChoice
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vanpra.composematerialdialogs.title
import lab.maxb.dark.ui.BuildConfig
import lab.maxb.dark.ui.R
import lab.maxb.dark.ui.theme.DarkAppTheme
import java.util.Locale


@Composable
fun ChooseLocaleDialog(
    dialogState: MaterialDialogState = rememberMaterialDialogState(),
    selectedLocale: String = "",
    onLocaleChosen: (String) -> Unit = {},
) {
    val defaultValueText = stringResource(R.string.dialogChooseLocale_systemDefault)
    val (names, keys) = getLocalesMap(defaultValueText)
    var selectedItem by remember(selectedLocale) {
        mutableIntStateOf(keys.indexOf(selectedLocale))
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
    val names = mutableListOf(defaultValueText)
    val keys = mutableListOf("")
    getSupportedLocales().forEach {
        names.add(getLanguageName(it))
        keys.add(it.toLanguageTag())
    }
    names to keys
}

fun getLanguageName(locale: Locale = Locale.getDefault()) =
    locale.displayLanguage.replaceFirstChar(Char::titlecase)

@Preview
@Composable
fun ChooseLocaleDialogPreview() = DarkAppTheme {
    val dialogState = rememberMaterialDialogState()
    ChooseLocaleDialog(dialogState)
    dialogState.show()
}

fun getSupportedLocales() = BuildConfig.LOCALES.map(Locale::forLanguageTag)
