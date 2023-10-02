package lab.maxb.dark.data.local

import android.content.Context
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager


internal fun getAead(context: Context): Aead {
    AeadConfig.register()
    return AndroidKeysetManager.Builder()
        .withSharedPref(
            context,
            "master_keyset",
            "secure_dark.preferences"
        )
        .withKeyTemplate(KeyTemplates.get("AES256_GCM"))
        .withMasterKeyUri("android-keystore://master_key")
        .build()
        .keysetHandle
        .getPrimitive(Aead::class.java)
}
