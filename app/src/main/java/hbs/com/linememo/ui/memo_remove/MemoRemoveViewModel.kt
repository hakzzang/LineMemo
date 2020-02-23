package hbs.com.linememo.ui.memo_remove

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hbs.com.linememo.domain.local.usecase.MemoUseCase
import hbs.com.linememo.domain.model.MemoItem
import hbs.com.linememo.util.SingleLiveEvent
import io.reactivex.Observable

class MemoRemoveViewModel(val memoUseCase: MemoUseCase) : ViewModel() {
    private val _removeAllCheck = SingleLiveEvent<Boolean>()
    val removeAllCheck: LiveData<Boolean> = _removeAllCheck
    private val _removePositions: MutableLiveData<MutableList<Int>> = MutableLiveData()
    val removePositions: LiveData<MutableList<Int>> = _removePositions

    init {
        _removePositions.value = mutableListOf()
    }

    fun changeRemoveCheck() {
        val isCheck = removeAllCheck.value?:false
        _removeAllCheck.value = !isCheck
    }

    fun changeAllCheckState(memoItems: MutableList<MemoItem>) {
        val newList = mutableListOf<Int>()
        for (memoItem in memoItems) {
            newList.add(memoItem.id)
        }
        _removePositions.value = newList
    }

    fun changeAllRemoveCheckState() {
        val newList = mutableListOf<Int>()
        _removePositions.value = newList
    }

    fun addRemoveMemoId(memoId: Int) {
        val newList = mutableListOf<Int>()
        _removePositions.value?.let {
            newList.addAll(it)
            newList.add(memoId)
        }
        _removePositions.value = newList
    }

    fun removeRemoveMemoId(memoId: Int) {
        val newList = mutableListOf<Int>()
        _removePositions.value?.let {
            newList.addAll(it)
            newList.remove(memoId)
        }
        _removePositions.value = newList
    }

    fun containsRemovePositionOf(position: Int) = removePositions.value?.contains(position) ?: false

    fun findAllMemo() = memoUseCase.findAllMemo()
    fun removeMemoItems(memoIds: MutableList<Int>): Observable<MutableList<Unit>> =
        memoUseCase.removeMemoItems(memoIds)
}