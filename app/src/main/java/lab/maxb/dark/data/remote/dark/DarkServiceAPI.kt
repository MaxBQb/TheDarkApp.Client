package lab.maxb.dark.data.remote.dark

import lab.maxb.dark.data.remote.dark.routes.Auth
import lab.maxb.dark.data.remote.dark.routes.RecognitionTask
import lab.maxb.dark.data.remote.dark.routes.User

interface DarkServiceAPI :
    RecognitionTask,
    User,
    Auth