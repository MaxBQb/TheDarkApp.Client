package lab.maxb.dark.ui.models

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import lab.maxb.dark.domain.model.Role
import lab.maxb.dark.ui.welcome.R

@StringRes
fun Role.toStringResourceId() = when (this) {
    Role.USER -> R.string.role_user
    Role.PREMIUM_USER -> R.string.role_premium_user
    Role.MODERATOR -> R.string.role_moderator
    Role.ADMINISTRATOR -> R.string.role_admin
    Role.CONSULTOR -> R.string.role_consultor
}

@Composable
fun Role.toStringResource() = stringResource(toStringResourceId())