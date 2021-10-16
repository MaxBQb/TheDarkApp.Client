package lab.maxb.dark.Presentation.Repository

import android.app.Application
import lab.maxb.dark.Domain.Extra.Delegates.Once
import lab.maxb.dark.Presentation.Repository.Implementation.RecognitionTasksRepository
import lab.maxb.dark.Presentation.Repository.Implementation.UsersRepository
import lab.maxb.dark.Presentation.Repository.Interfaces.IRecognitionTasksRepository
import lab.maxb.dark.Presentation.Repository.Interfaces.ISynonymsRepository
import lab.maxb.dark.Presentation.Repository.Interfaces.IUsersRepository
import lab.maxb.dark.Presentation.Repository.Mock.MockBase
import lab.maxb.dark.Presentation.Repository.Network.Synonymizer.SynonymFounder

class Repository {
    companion object {
        var recognitionTasks: IRecognitionTasksRepository
            by Once {
                MockBase()
            }
            private set

        var users: IUsersRepository
            by Once()
            private set

        var synonyms: ISynonymsRepository
            by Once()
            private set

        fun init(application: Application) {
            recognitionTasks = RecognitionTasksRepository(application)
            users = UsersRepository(application)
            synonyms = SynonymFounder()
        }
    }
}
