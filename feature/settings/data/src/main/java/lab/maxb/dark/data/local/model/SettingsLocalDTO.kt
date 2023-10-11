package lab.maxb.dark.data.local.model

import lab.maxb.dark.domain.model.Settings

@kotlinx.serialization.Serializable
data class SettingsLocalDTO(
    val locale: String,
    val systemLocale: String,
    val useExternalSuggestions: Boolean,
)

fun SettingsLocalDTO.toDomain() = Settings(
    locale = locale,
    systemLocale = systemLocale,
    useExternalSuggestions = useExternalSuggestions
)

fun Settings.toLocalDTO() = SettingsLocalDTO(
    locale = locale,
    systemLocale = systemLocale,
    useExternalSuggestions = useExternalSuggestions
)