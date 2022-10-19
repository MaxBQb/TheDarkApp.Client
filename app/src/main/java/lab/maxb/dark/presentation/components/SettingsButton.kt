package lab.maxb.dark.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import lab.maxb.dark.presentation.screens.destinations.SettingsScreenDestination

@Composable
fun SettingsButton(navigator: DestinationsNavigator) {
    IconButton(
        onClick = { navigator.navigate(SettingsScreenDestination) }
    ) {
        Icon(Icons.Filled.Settings, null)
    }
}