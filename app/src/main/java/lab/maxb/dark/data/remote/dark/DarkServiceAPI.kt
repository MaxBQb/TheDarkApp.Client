package lab.maxb.dark.data.remote.dark

import lab.maxb.dark.data.remote.dark.routes.Article
import lab.maxb.dark.data.remote.dark.routes.Auth
import lab.maxb.dark.data.remote.dark.routes.Images
import lab.maxb.dark.data.remote.dark.routes.RecognitionTask
import lab.maxb.dark.data.remote.dark.routes.User

interface DarkServiceAPI :
    Article,
    RecognitionTask,
    Images,
    User,
    Auth
