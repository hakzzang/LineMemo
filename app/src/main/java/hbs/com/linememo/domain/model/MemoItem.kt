package hbs.com.linememo.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity
@Parcelize
data class MemoItem(
    @PrimaryKey(autoGenerate = true) val id: Int,
    var title: String,
    var content: String,
    var thumbnail: String,
    var thumbnailType: String,
    val makeAt: Date = Date()
) : Parcelable {
    constructor() : this(0, "", "", "", "")
}