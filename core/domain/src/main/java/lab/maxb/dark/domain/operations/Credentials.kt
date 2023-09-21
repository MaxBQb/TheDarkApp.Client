package lab.maxb.dark.domain.operations

import lab.maxb.dark.domain.model.Profile
import lab.maxb.dark.domain.model.ReceivedAuthCredentials

fun ReceivedAuthCredentials.toProfile() = Profile(
    request.login,
    id,
    token,
    role = role
)