package lab.maxb.dark.DI

import lab.maxb.dark.Presentation.Repository.Implementation.RecognitionTasksRepositoryImpl
import lab.maxb.dark.Presentation.Repository.Implementation.UsersRepositoryImpl
import lab.maxb.dark.Presentation.Repository.Interfaces.RecognitionTasksRepository
import lab.maxb.dark.Presentation.Repository.Interfaces.SynonymsRepository
import lab.maxb.dark.Presentation.Repository.Interfaces.UsersRepository
import lab.maxb.dark.Presentation.Repository.Network.Synonymizer.SynonymFounder
import lab.maxb.dark.Presentation.Repository.Room.LocalDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

internal val MODULE_repository = module {
    single { LocalDatabase.buildDatabase(androidApplication()) }
    single<SynonymsRepository> { SynonymFounder() }
    single<RecognitionTasksRepository> { RecognitionTasksRepositoryImpl(get()) }
    single<UsersRepository> { UsersRepositoryImpl(get()) }
}