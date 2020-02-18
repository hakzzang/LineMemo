package hbs.com.linememo.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MemoGallery(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val filePath: String,
    val urlType:String
)