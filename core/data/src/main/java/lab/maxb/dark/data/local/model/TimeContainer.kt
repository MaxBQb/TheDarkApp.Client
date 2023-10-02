package lab.maxb.dark.data.local.model

import androidx.room.ColumnInfo

interface TimeContainer {
    var createdAt: Long
    var modifiedAt: Long
}

data class TimeContainerImpl(
    @ColumnInfo("creation_time")
    override var createdAt: Long = 0,

    @ColumnInfo("modification_time")
    override var modifiedAt: Long = 0,
) : TimeContainer

private fun <T : BaseLocalDTO> T.modifyIfTimeContainer(block: TimeContainer.() -> Unit) =
    if (this is TimeContainer) apply(block) else this

fun <T : BaseLocalDTO> T.updateTimeStamps() = modifyIfTimeContainer {
    if (createdAt == 0L)
        createdAt = System.currentTimeMillis()
    modifiedAt = System.currentTimeMillis()
}