package lab.maxb.dark.domain.repository

import lab.maxb.dark.domain.model.User
import lab.maxb.dark.domain.repository.utils.Resource

interface UsersRepository {
    val userResource: Resource<String, User>
}