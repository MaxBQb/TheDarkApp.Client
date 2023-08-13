package lab.maxb.dark.domain.model

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

sealed interface ModelRef<V: BaseModel>: BaseModel {
    val value: V?

    data class Ref<V: BaseModel>(
        override val value: V
    ): ModelRef<V> {
        override val id: String = value.id
    }

    data class EmptyRef<V: BaseModel>(override val id: String): ModelRef<V> {
        override var value: V? = null
            set(value) {
                require(value?.id == id)
                field = value
            }
    }
}

@OptIn(ExperimentalContracts::class)
fun <V : BaseModel> ModelRef<V>.isEmpty(): Boolean {
    contract {
        returns(true) implies (this@isEmpty is ModelRef.EmptyRef<V>)
    }
    return this@isEmpty is ModelRef.EmptyRef<V>
}

@OptIn(ExperimentalContracts::class)
fun <V : BaseModel> ModelRef<V>.requireValue(): V {
    contract {
        returns() implies (this@requireValue is ModelRef.Ref<V>)
    }
    return (this@requireValue as ModelRef.Ref<V>).value
}

fun <V : BaseModel> ModelRef<V>.setDefault(value: V) {
    if (isEmpty())
        this.value = value
}

fun <V: BaseModel> modelRefOf(value: V) = ModelRef.Ref(value)
fun <V: BaseModel> modelRefOf(id: String) = ModelRef.EmptyRef<V>(id)
