package lab.maxb.dark.data.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import lab.maxb.dark.domain.model.Article
import lab.maxb.dark.domain.model.modelRefOf
import lab.maxb.dark.domain.operations.randomUUID

@Entity(
    tableName = "article",
    foreignKeys = [
        ForeignKey(
            entity = UserLocalDTO::class,
            parentColumns = ["id"],
            childColumns = ["author_id"],
        )
    ]
)
data class ArticleLocalDTO(
    val title: String,
    val body: String,

    @ColumnInfo(name="author_id")
    val authorId: String,
    
    @PrimaryKey
    override var id: String = randomUUID,
): BaseLocalDTO(), TimeContainer {
    @ColumnInfo(name = TimeContainer.createdAt)
    override var createdAt: Long = 0

    @ColumnInfo(name = TimeContainer.modifiedAt)
    override var modifiedAt: Long = 0
}

fun Article.toLocalDTO() = ArticleLocalDTO(
    title,
    body,
    author.id,
    id,
)

fun ArticleLocalDTO.toDomain() = Article(
    title,
    body,
    modelRefOf(authorId),
    id,
)