package lab.maxb.dark.plugin.common
import org.gradle.api.JavaVersion

object ProjectConfig {
    const val applicationId = "lab.maxb.dark"
    const val compileSdk = 34
    const val minSdk = 21
    val version = AppVersion(4, "0.4.0")
    val javaVersion = JavaVersion.VERSION_11

    data class AppVersion(val code: Int, val name: String)
}