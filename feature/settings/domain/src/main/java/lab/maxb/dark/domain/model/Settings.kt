package lab.maxb.dark.domain.model

data class Settings(
    val locale: String = "",
    val systemLocale: String = "",
    val useExternalSuggestions: Boolean = false,
)
