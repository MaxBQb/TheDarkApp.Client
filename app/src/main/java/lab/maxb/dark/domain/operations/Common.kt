package lab.maxb.dark.domain.operations

import java.util.*

inline val randomUUID get() = UUID.randomUUID().toString()
inline val<reified T> T.unicname get() = "${T::class}.$randomUUID"
