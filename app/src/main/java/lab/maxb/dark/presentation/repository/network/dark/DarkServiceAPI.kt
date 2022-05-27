package lab.maxb.dark.presentation.repository.network.dark

import lab.maxb.dark.presentation.repository.network.dark.routes.Auth
import lab.maxb.dark.presentation.repository.network.dark.routes.RecognitionTask
import lab.maxb.dark.presentation.repository.network.dark.routes.User

interface DarkServiceAPI :
    RecognitionTask,
    User,
    Auth