package hbs.com.linememo.domain.local.repository

import hbs.com.linememo.dao.MemoDataBase
import hbs.com.linememo.domain.model.MemoItem
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MemoRepository(private val memoDataBase: MemoDataBase) {
    fun findAllMemo() = Observable
        .fromCallable { memoDataBase.getMemoItemDao().findAllItems() }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun insertMemo(memoItem: MemoItem) = Observable
        .fromCallable { memoDataBase.getMemoItemDao().insert(memoItem) }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun updateMemo(memoItem: MemoItem) = Observable
        .fromCallable { memoDataBase.getMemoItemDao().update(memoItem) }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun removeMemoItems(memoIds: MutableList<Int>): Observable<MutableList<Unit>> =
        Observable
            .fromIterable(memoIds)
            .flatMap { memoId ->
                Observable.fromCallable {
                    memoDataBase.getMemoItemDao().removeItemAt(memoId)
                }
            }
            .toList()
            .toObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}