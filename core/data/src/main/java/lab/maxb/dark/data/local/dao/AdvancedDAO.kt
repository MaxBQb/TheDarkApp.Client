package lab.maxb.dark.data.local.dao

import androidx.room.RawQuery
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.room.withTransaction
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import lab.maxb.dark.data.local.model.BaseLocalDTO
import lab.maxb.dark.data.local.model.updateTimeStamps
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class AdvancedDAO<DTO: BaseLocalDTO>(
    private val tableName: String,
) : BaseDAO<DTO>, KoinComponent {
    protected val db by inject<RoomDatabase>()

    suspend fun clear() = run(SimpleSQLiteQuery("DELETE FROM $tableName"))

    suspend fun getById(id: String) = runForResult(SimpleSQLiteQuery(
        "SELECT * FROM $tableName WHERE id = :id",
        arrayOf(id),
    ))

    suspend fun delete(id: String) = db.withTransaction {
        run(SimpleSQLiteQuery(
            "DELETE FROM $tableName WHERE id = :id",
            arrayOf(id),
        ))
    }

    @Transaction
    open suspend fun save(vararg value: DTO) {
        for (it in value)
            save(it)
    }

    @Transaction
    open suspend fun save(value: DTO) {
        val storedValue = getById(value.id)
        if (storedValue == null)
            add(value.withLocalsDefault().updateTimeStamps())
        else
            update(value.withLocalsPreserved(storedValue).updateTimeStamps())
    }

    @RawQuery
    protected abstract suspend fun run(query: SupportSQLiteQuery): Long

    @RawQuery
    protected abstract suspend fun runForResult(query: SupportSQLiteQuery): DTO?

    protected open suspend fun DTO.withLocalsDefault() = this
    protected open suspend fun DTO.withLocalsPreserved(old: DTO) = this
}
