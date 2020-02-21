package hbs.com.linememo.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import hbs.com.linememo.domain.model.MemoGallery
import hbs.com.linememo.domain.model.MemoItem
import java.util.*

@Database(entities = [MemoItem::class, MemoGallery::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class MemoDataBase : RoomDatabase() {
    abstract fun getMemoItemDao(): MemoItemDao
    abstract fun getMemoGalleryDao(): MemoGalleryDao
}

class DateConverter{
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}