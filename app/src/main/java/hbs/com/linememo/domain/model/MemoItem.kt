package hbs.com.linememo.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MemoItem(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val content: String
)