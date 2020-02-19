package hbs.com.linememo.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MemoItem(
    @PrimaryKey(autoGenerate = true) val id: Int,
    var title: String,
    var content: String,
    var thumbnail: String,
    var thumbnailType: String
) {
    constructor() : this(0, "", "", "", "")
}