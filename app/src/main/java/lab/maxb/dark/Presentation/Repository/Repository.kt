package lab.maxb.dark.Presentation.Repository

import android.app.Application
import lab.maxb.dark.Domain.Extra.Delegates.Once
import lab.maxb.dark.Presentation.Repository.Mock.MockBase

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

        fun init(application: Application) {
            recognitionTasks = RecognitionTasksRepository(application)
            users = UsersRepository(application)
        }
    }
}
