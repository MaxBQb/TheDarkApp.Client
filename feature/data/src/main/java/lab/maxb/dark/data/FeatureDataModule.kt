package lab.maxb.dark.data

import lab.maxb.dark.data.local.LocalDatabase
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.scope.Scope
import org.koin.dsl.module

@Module([FeatureDataModuleManual::class])
@ComponentScan
class FeatureDataModule

internal class FeatureDataModuleManual {
    private val Scope.db get() = get<LocalDatabase>()
    internal val module = module {
        factory { db.articles() }
        factory { db.recognitionTasks() }
        factory { db.remoteKeys() }
        factory { db.users() }
    }
}