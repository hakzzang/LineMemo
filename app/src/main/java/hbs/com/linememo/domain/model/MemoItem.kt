package hbs.com.linememo.domain.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity
@Parcelize
data class MemoItem(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "content") var content: String,
    @ColumnInfo(name = "thumbnail") var thumbnail: String,
    @ColumnInfo(name = "thumbnail_type") var thumbnailType: String,
    @ColumnInfo(name = "make_at") var makeAt: Date = Date()
) : Parcelable {
    constructor() : this(0, "", "", "", "")
}