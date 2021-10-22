package lab.maxb.dark.DI

import lab.maxb.dark.Presentation.Repository.Implementation.RecognitionTasksRepository
import lab.maxb.dark.Presentation.Repository.Implementation.UsersRepository
import lab.maxb.dark.Presentation.Repository.Interfaces.IRecognitionTasksRepository
import lab.maxb.dark.Presentation.Repository.Interfaces.ISynonymsRepository
import lab.maxb.dark.Presentation.Repository.Interfaces.IUsersRepository
import lab.maxb.dark.Presentation.Repository.Network.Synonymizer.SynonymFounder
import org.koin.dsl.module

internal val MODULE_repository = module {
    single<ISynonymsRepository> { SynonymFounder() }
    single<IRecognitionTasksRepository> { RecognitionTasksRepository(get()) }
    single<IUsersRepository> { UsersRepository(get()) }
}