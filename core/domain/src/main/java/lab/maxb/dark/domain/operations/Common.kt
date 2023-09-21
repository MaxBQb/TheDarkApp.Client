package lab.maxb.dark.domain.operations

import java.util.*

inline val randomUUID get() = UUID.randomUUID().toString()
@Suppress("unused")
inline val<reified T> T.randomFieldKey get() = "${T::class}.$randomUUID"
