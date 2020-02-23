package hbs.com.linememo.domain.local.usecase

import hbs.com.linememo.domain.local.repository.MemoGalleryRepository
import hbs.com.linememo.domain.local.repository.MemoRepository
import hbs.com.linememo.domain.model.MemoGallery
import hbs.com.linememo.domain.model.MemoItem
import hbs.com.linememo.domain.model.WrapMemoGallery
import io.reactivex.Observable

class MemoUseCase(
    private val memoRepository: MemoRepository,
    private val memoGalleryRepository: MemoGalleryRepository
) {
    fun findAllMemo() = memoRepository.findAllMemo()
    fun insertMemo(memoItem: MemoItem) = memoRepository.insertMemo(memoItem)
    fun updateMemo(memoItem: MemoItem) = memoRepository.updateMemo(memoItem)
    fun removeMemoItems(memoIds:MutableList<Int>): Observable<MutableList<Unit>> = memoRepository.removeMemoItems(memoIds)

    fun insertMemoGalleries(memoId: Int, memoGalleries: MutableList<WrapMemoGallery>) =
        memoGalleryRepository.insertMemoGalleries(memoId, memoGalleries)

    fun selectMemoGalleries(memoId: Int) : Observable<List<MemoGallery>> = memoGalleryRepository.selectMemoGalleries(memoId)
    fun removeMemoGalleries(memoId: Int): Observable<Unit> = memoGalleryRepository.removeMemoGalleries(memoId)
}