package hbs.com.linememo.dao

import androidx.room.*
import hbs.com.linememo.domain.model.MemoGallery

@Dao
interface MemoGalleryDao{
    @Query("SELECT * FROM MemoGallery")
    fun findAllItems(): List<MemoGallery>

    @Insert
    fun insert(memoGallery: MemoGallery) : Long

    @Update
    fun update(memoGallery: MemoGallery)

    @Delete
    fun delete(memoGallery: MemoGallery)
}