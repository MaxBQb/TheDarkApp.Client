package lab.maxb.dark.domain.operations

import lab.maxb.dark.domain.model.Profile
import lab.maxb.dark.domain.model.ReceivedAuthCredentials
import lab.maxb.dark.domain.model.User

inline fun ReceivedAuthCredentials.toProfile(
    user: (String) -> User? = { null },
) = Profile(
    request.login,
    user(id),
    token,
    role = role
)