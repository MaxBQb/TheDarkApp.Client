package lab.maxb.dark.data.datasource.local

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow

interface BasePagedLocalDataSource<T, TFull> : BaseLocalDataSource<T> {
    fun getAll(): Flow<List<T>?>
    fun getAllPaged(): PagingSource<Int, TFull & Any>
}
typealias SimplePagedLocalDataSource<T> = BasePagedLocalDataSource<T, T>