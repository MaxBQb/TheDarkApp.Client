package lab.maxb.dark.domain.model

fun interface Mapper<in T, out R> {
    fun map(model: T): R
    companion object
}

typealias NullableMapper<T, R> = Mapper<T?, R?>

fun <T, R> NullableMapper(mapper: Mapper<T, R>)= Mapper(mapper::mapNotNull)

fun <T, R> Mapper<T, R>.mapNotNull(model: T?)
    = model?.let { map(it) }

fun <T, R> Mapper.Companion.getCastMapper() = Mapper<T, R> {
    @Suppress("UNCHECKED_CAST")
    it as R
}

fun <T, R> Mapper.Companion.getNullableCastMapper() = Mapper<T, R?> {
    @Suppress("UNCHECKED_CAST")
    it as? R
}