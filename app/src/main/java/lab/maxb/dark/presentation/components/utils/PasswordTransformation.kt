package lab.maxb.dark.presentation.components.utils

import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

fun getPasswordTransformation(showPassword: Boolean = false) = if (showPassword)
    VisualTransformation.None
else
    PasswordVisualTransformation()