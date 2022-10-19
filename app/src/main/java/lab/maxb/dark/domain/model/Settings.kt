package lab.maxb.dark.domain.model

@kotlinx.serialization.Serializable
data class Settings(
    val locale: String = "",
    val systemLocale: String = "",
)
