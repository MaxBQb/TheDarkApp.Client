package lab.maxb.dark.data.model.local

interface TimeContainer {
    var createdAt: Long
    var modifiedAt: Long

    companion object {
        const val createdAt = "creation_time"
        const val modifiedAt = "modification_time"
    }
}
/*
TimeContainer Implementation: TimeContainer {
    @ColumnInfo(name = TimeContainer.createdAt)
    override var createdAt: Long = 0

    @ColumnInfo(name = TimeContainer.modifiedAt)
    override var modifiedAt: Long = 0
}
*/

private fun <T:BaseLocalDTO> T.modifyIfTimeContainer(block: TimeContainer.() -> Unit)
    = if (this is TimeContainer) apply(block) else this

fun <T:BaseLocalDTO> T.markModified() = modifyIfTimeContainer {
    modifiedAt = System.currentTimeMillis()
}

fun <T:BaseLocalDTO> T.markCreated() = modifyIfTimeContainer {
    createdAt = System.currentTimeMillis()
    modifiedAt = System.currentTimeMillis()
}