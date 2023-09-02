package lab.maxb.dark.presentation.screens.core.effects

import androidx.compose.runtime.Immutable
import kotlin.reflect.KClass

@Immutable
interface UiSideEffectsHolder {
    fun get(key: EffectKey): UiSideEffect?
    fun <T: UiSideEffect> trigger(value: T, key: EffectKey): UiSideEffectsHolder
    fun consume(key: EffectKey): UiSideEffectsHolder
}

inline fun <reified T: UiSideEffect> UiSideEffectsHolder.get()
    = get(EffectKey<T>()) as T?

inline fun <reified T: UiSideEffect> UiSideEffectsHolder.trigger(value: T)
    = trigger(value, EffectKey<T>())

inline operator fun <reified T: UiSideEffect> UiSideEffectsHolder.plus(value: T)
    = trigger(value)

inline fun <reified T: UiSideEffect> UiSideEffectsHolder.consume()
    = consume(EffectKey<T>())

inline fun <reified T: UiSideEffect> UiSideEffectsHolder.consume(ignored: T)
    = consume<T>()

inline operator fun <reified T: UiSideEffect> UiSideEffectsHolder.minus(value: T)
    = consume(value)

operator fun UiSideEffectsHolder.minus(value: EffectKey)
    = consume(value)

operator fun <T: UiSideEffect> UiSideEffectsHolder.minus(value: KClass<T>)
    = consume(EffectKey(value))

val EmptyEffectsHolder = UiSideEffectsHolderImpl(emptyMap())

fun <T: Map<EffectKey, UiSideEffect>> effectsHolderOf(value: T): UiSideEffectsHolder
    = UiSideEffectsHolderImpl(value as Map<EffectKey, UiSideEffect>)

@Immutable
@JvmInline
value class UiSideEffectsHolderImpl(
    private val storage: Map<EffectKey, UiSideEffect>
): UiSideEffectsHolder {
    override fun get(key: EffectKey) = storage[key]

    override fun <T : UiSideEffect> trigger(value: T, key: EffectKey)
        = effectsHolderOf(storage + (key to value))

    override fun consume(key: EffectKey)
        = effectsHolderOf(storage - key)
}