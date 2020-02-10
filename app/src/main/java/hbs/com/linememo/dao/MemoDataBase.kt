package hbs.com.linememo.dao

import androidx.room.Database
import hbs.com.linememo.domain.model.MemoItem

@Database(entities = [MemoItem::class], version = 1)
abstract class MemoDataBase{
    abstract fun getMemoItemDao(): MemoItemDao
}