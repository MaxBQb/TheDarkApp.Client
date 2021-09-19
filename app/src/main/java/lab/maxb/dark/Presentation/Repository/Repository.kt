package lab.maxb.dark.Presentation.Repository

import android.app.Application
import lab.maxb.dark.Presentation.Repository.Mock.MockBase

class Repository {
    companion object {
        private var instance: RepositoryTasks? = null

        fun init(application: Application?) {
            if (instance == null)
                instance = LocalDatabaseRepository(application)
        }

        fun getRepository(): RepositoryTasks {
            if (instance == null)
                instance = MockBase()
            return instance!!
        }
    }
}