package lab.maxb.dark.Presentation.Extra

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import lab.maxb.dark.Presentation.Extra.Delegates.property


class UserSettings(context: Context) {
    private val pref = PreferenceManager.getDefaultSharedPreferences(context)
    private val securePref: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secure_dark_preferences",
        MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    var token: String by securePref.property()
    var login: String by pref.property()
}