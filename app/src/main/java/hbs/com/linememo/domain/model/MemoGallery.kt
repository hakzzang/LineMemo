package hbs.com.linememo.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = MemoItem::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("memo_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class MemoGallery(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "file_path") val filePath: String,
    @ColumnInfo(name = "memo_id") var memoId: Int
)