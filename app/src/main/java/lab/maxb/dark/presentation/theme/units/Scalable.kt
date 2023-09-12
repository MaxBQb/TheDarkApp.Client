package lab.maxb.dark.presentation.theme.units

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
private fun getFieldId(id: String) = with (LocalContext.current) {
    resources.getIdentifier(id, "dimen", packageName)
}

val Int.sdp: Dp @Composable get() {
    val id = when (this) {
        in 1..600 -> "_${this}sdp"
        in (-60..-1) -> "_minus${this}sdp"
        else -> return this.dp
    }

    val resourceField = getFieldId(id)
    return if (resourceField != 0) dimensionResource(resourceField) else this.dp
}


val Int.ssp: TextUnit
    @Composable get() = with(LocalDensity.current) { sdp.toSp() }
