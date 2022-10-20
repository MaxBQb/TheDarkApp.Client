package lab.maxb.dark.domain.model

import lab.maxb.dark.domain.operations.randomUUID

data class User(
    val name: String,
    val rating: Int,
    val id: String = randomUUID,
)