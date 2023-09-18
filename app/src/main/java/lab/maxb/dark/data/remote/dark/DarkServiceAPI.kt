package lab.maxb.dark.data.remote.dark

import lab.maxb.dark.data.remote.dark.routes.ArticlesAPI
import lab.maxb.dark.data.remote.dark.routes.AuthAPI
import lab.maxb.dark.data.remote.dark.routes.ImagesAPI
import lab.maxb.dark.data.remote.dark.routes.RecognitionTasksAPI
import lab.maxb.dark.data.remote.dark.routes.UsersAPI

interface DarkServiceAPI :
    ArticlesAPI,
    RecognitionTasksAPI,
    ImagesAPI,
    UsersAPI,
    AuthAPI
