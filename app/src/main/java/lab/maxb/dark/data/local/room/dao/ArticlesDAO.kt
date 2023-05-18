package lab.maxb.dark.data.local.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import lab.maxb.dark.data.model.local.ArticleLocalDTO

@Dao
abstract class ArticlesDAO: AdvancedDAO<ArticleLocalDTO>("article") {
    @Transaction
    @Query("SELECT * FROM article")
    abstract fun getAll(): Flow<List<ArticleLocalDTO>?>

    @Transaction
    @Query("SELECT * FROM article")
    abstract fun getAllPaged(): PagingSource<Int, ArticleLocalDTO>

    @Query("SELECT * FROM article WHERE id = :id")
    abstract fun get(id: String): Flow<ArticleLocalDTO?>
}