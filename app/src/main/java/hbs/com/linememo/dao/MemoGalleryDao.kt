package hbs.com.linememo.dao

import androidx.room.*
import hbs.com.linememo.domain.model.MemoGallery

@Dao
interface MemoGalleryDao {
    @Query("SELECT * FROM MemoGallery")
    fun findAllItems(): List<MemoGallery>

    @Query("SELECT * FROM MemoGallery WHERE memo_id =:memoId")
    fun findItemsAt(memoId: Int): List<MemoGallery>

    @Insert
    fun insert(memoGallery: MemoGallery): Long

    @Update
    fun update(memoGallery: MemoGallery)

    @Delete
    fun delete(memoGallery: MemoGallery)

    @Query("DELETE FROM MemoGallery WHERE memo_id =:memoId")
    fun removeItemAt(memoId:Int)
}