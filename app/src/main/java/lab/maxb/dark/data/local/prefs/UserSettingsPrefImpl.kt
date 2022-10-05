package lab.maxb.dark.data.local.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import lab.maxb.dark.BuildConfig
import lab.maxb.dark.data.utils.property
import lab.maxb.dark.domain.repository.UserSettings

class UserSettingsPrefImpl(context: Context) : UserSettings {
    private val pref = PreferenceManager.getDefaultSharedPreferences(context)
    private val securePref: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secure_dark.preferences",
        MasterKey.Builder(context, BuildConfig.SECURE_PREFS_MASTER_KEY)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    override var token: String by securePref.property()
    override var login: String by pref.property()
}