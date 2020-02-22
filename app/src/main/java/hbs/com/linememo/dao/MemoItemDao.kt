package hbs.com.linememo.dao

import androidx.room.*
import hbs.com.linememo.domain.model.MemoItem

@Dao
interface MemoItemDao{
    @Query("SELECT * FROM MemoItem")
    fun findAllItems(): List<MemoItem>

    @Query("DELETE FROM MemoItem WHERE id = :position")
    fun removeItemAt(position:Int)

    @Insert
    fun insert(memoItem: MemoItem) : Long

    @Update
    fun update(memoItem: MemoItem)

    @Delete
    fun delete(memoItem: MemoItem)
}