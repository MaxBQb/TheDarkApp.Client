package lab.maxb.dark.data

import lab.maxb.dark.data.local.dataStore.getAead
import lab.maxb.dark.data.local.dataStore.serializers.ProfileSerializer
import lab.maxb.dark.data.local.dataStore.serializers.SettingsSerializer
import lab.maxb.dark.data.local.room.LocalDatabase
import lab.maxb.dark.domain.repository.LocalStorage
import org.koin.android.ext.koin.androidApplication
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.dsl.module

@Module([CoreDataModuleManual::class])
@ComponentScan("lab.maxb.dark")
class CoreDataModule

internal class CoreDataModuleManual {
    internal val module = module {
        single { LocalDatabase.build(androidApplication()) }
        single<LocalStorage> { get<LocalDatabase>() }
        factory { get<LocalDatabase>().articles() }
        factory { get<LocalDatabase>().recognitionTasks() }
        factory { get<LocalDatabase>().remoteKeys() }
        factory { get<LocalDatabase>().users() }
        factory { SettingsSerializer() }
        factory { ProfileSerializer() }
        single { getAead(androidApplication()) }
    }
}