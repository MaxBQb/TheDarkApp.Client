package lab.maxb.dark.data.local.room.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import lab.maxb.dark.data.model.local.BaseLocalDTO

interface BaseDAO<DTO: BaseLocalDTO> {
    @Insert(onConflict = IGNORE)
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
            if (add(it) == -1L)
                update(it)
    }

    @RawQuery
    protected abstract suspend fun run(query: SupportSQLiteQuery): Long

    @RawQuery
    protected abstract suspend fun runForResult(query: SupportSQLiteQuery): DTO?
}
