package lab.maxb.dark.data.repository

import android.content.Context
import lab.maxb.dark.data.local.prefs.UserSettingsPrefImpl
import lab.maxb.dark.domain.repository.UserSettings
import org.koin.core.annotation.Single

@Single
class UserSettingsRepositoryImpl(context: Context)
    : UserSettings by UserSettingsPrefImpl(context)