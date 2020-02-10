package hbs.com.linememo.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import hbs.com.linememo.domain.model.MemoItem

@Database(entities = [MemoItem::class], version = 1)
abstract class MemoDataBase : RoomDatabase() {
    abstract fun getMemoItemDao(): MemoItemDao
}