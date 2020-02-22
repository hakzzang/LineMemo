package hbs.com.linememo.domain.local.repository

import hbs.com.linememo.dao.MemoDataBase
import hbs.com.linememo.domain.model.MemoGallery
import hbs.com.linememo.domain.model.WrapMemoGallery
import hbs.com.linememo.ui.memo_make.MemoGalleryViewType
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MemoGalleryRepository(private val memoDataBase: MemoDataBase) {
    fun insertMemoGalleries(memoId: Long, memoGalleries: MutableList<WrapMemoGallery>) =
        Observable
            .fromIterable(memoGalleries)
            .filter { wrapMemoGallery: WrapMemoGallery -> wrapMemoGallery.viewType != MemoGalleryViewType.ADD }
            .map { wrapMemoGallery: WrapMemoGallery -> wrapMemoGallery.memoGallery }
            .flatMap {memoGallery ->
                Observable.fromCallable {
                    memoDataBase.getMemoGalleryDao().removeItemAt(memoId.toInt())
                }.flatMap {
                    memoGallery.memoId = memoId.toInt()
                    Observable.fromCallable { memoDataBase.getMemoGalleryDao().insert(memoGallery) }
                }
            }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun selectMemoGalleries(memoId: Int): Observable<List<MemoGallery>> =
        Observable.fromCallable { memoDataBase.getMemoGalleryDao().findItemsAt(memoId) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}