package hbs.com.linememo.dao

import androidx.room.*
import hbs.com.linememo.domain.model.MemoItem

@Dao
interface MemoItemDao{
    @Query("SELECT * FROM MemoItem")
    fun findAllItems(): List<MemoItem>

    @Insert
    fun insert(memoItem: MemoItem) : Long

    @Update
    fun update(memoItem: MemoItem)

    @Delete
    fun delete(memoItem: MemoItem)
}