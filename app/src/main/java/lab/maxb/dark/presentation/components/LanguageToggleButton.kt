package lab.maxb.dark.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import lab.maxb.dark.R
import lab.maxb.dark.ui.theme.spacing

@Composable
fun LanguageToggleButton(onClick: () -> Unit = {}) {
    val languageCode = remember(Locale.current) {
        Locale.current.toLanguageTag().split('-')[0].uppercase()
    }
    Row(
        modifier = Modifier.clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(painterResource(R.drawable.ic_language), null)
        Spacer(Modifier.width(MaterialTheme.spacing.extraSmall))
        Text(
            languageCode,
            fontWeight = FontWeight.Bold,
        )
    }
}