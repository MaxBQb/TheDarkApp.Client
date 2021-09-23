package lab.maxb.dark.Presentation.Room.Model

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import lab.maxb.dark.Domain.Model.RecognitionTask
import lab.maxb.dark.Domain.Model.User

@Entity(tableName = "recognition_task", ignoredColumns = ["owner", "names"])
data class RecognitionTaskDTO(
    @PrimaryKey(autoGenerate = true) @ColumnInfo val id: Int,
    val names_serialized: String,
    val owner_serialized: String,
    override val image: Bitmap?,
): RecognitionTask() {
    override var names: Set<String>?
        get() = Gson().fromJson<Set<String>>(names_serialized, Set::class.java)
        set(value) { }

    override val owner: User?
        get() = Gson().fromJson(owner_serialized, User::class.java)

    constructor(task: RecognitionTask) : this(
        0,
        Gson().toJson(task.names),
        Gson().toJson(task.owner),
        task.image,
    )
}