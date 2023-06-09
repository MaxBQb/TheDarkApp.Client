package lab.maxb.dark.data.local.room.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.room.Update
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import lab.maxb.dark.data.model.local.BaseLocalDTO
import lab.maxb.dark.data.model.local.markCreated
import lab.maxb.dark.data.model.local.markModified

interface BaseDAO<DTO: BaseLocalDTO> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun add(value: DTO): Long

    @Update
    suspend fun update(value: DTO)

    @Delete
    suspend fun delete(vararg value: DTO)
}

abstract class AdvancedDAO<DTO: BaseLocalDTO>(
    private val tableName: String,
) : BaseDAO<DTO> {
    suspend fun clear() = run(SimpleSQLiteQuery("DELETE FROM $tableName"))

    suspend fun getById(id: String) = runForResult(SimpleSQLiteQuery(
        "SELECT * FROM $tableName WHERE id = :id",
        arrayOf(id),
    ))

    suspend fun delete(id: String) = run(SimpleSQLiteQuery(
        "DELETE FROM $tableName WHERE id = :id",
        arrayOf(id),
    ))

    @Transaction
    open suspend fun save(vararg value: DTO) {
        for (it in value)
            save(it)
    }

    @Transaction
    open suspend fun save(value: DTO) {
        val storedValue = getById(value.id)
        if (storedValue == null)
            add(value.withLocalsDefault().markCreated())
        else
            update(value.withLocalsPreserved(storedValue).markModified())
    }

    @RawQuery
    protected abstract suspend fun run(query: SupportSQLiteQuery): Long

    @RawQuery
    protected abstract suspend fun runForResult(query: SupportSQLiteQuery): DTO?

    protected open suspend fun DTO.withLocalsDefault() = this
    protected open suspend fun DTO.withLocalsPreserved(old: DTO) = this
}
