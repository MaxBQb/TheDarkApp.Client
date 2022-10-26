package lab.maxb.dark.domain.repository.utils

import kotlinx.coroutines.flow.Flow

interface Resource<Input, Output> {
    fun query(args: Input,
              force: Boolean = false,
              useCache: Boolean = false): Flow<Output?>
    suspend fun refresh(args: Input): Boolean
    suspend fun retry(): Boolean
    suspend fun checkIsFresh(args: Input): Boolean
}