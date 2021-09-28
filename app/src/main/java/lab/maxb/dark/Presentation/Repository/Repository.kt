package lab.maxb.dark.Presentation.Repository

import android.app.Application
import lab.maxb.dark.Presentation.Repository.Mock.MockBase
import kotlin.reflect.KProperty

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

private class Once<T>() {
    private lateinit var mDefault: () -> T
    private var mValue: T? = null

    constructor(initializer: () -> T) : this() {
        mDefault = initializer
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>)
        = mValue ?: mDefault().also { mValue = it }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        mValue = value ?: value
    }
}