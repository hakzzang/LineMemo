package hbs.com.linememo.ui.memo_remove

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hbs.com.linememo.domain.local.usecase.MemoUseCase
import hbs.com.linememo.util.SingleLiveEvent

class MemoRemoveViewModel(val memoUseCase: MemoUseCase) : ViewModel() {
    private val _removeCheck = SingleLiveEvent<Boolean>()
    val removeCheck: LiveData<Boolean> = _removeCheck
    val _removePositions: MutableLiveData<MutableList<Int>> = MutableLiveData()
    val removePositions: LiveData<MutableList<Int>> = _removePositions

    init {
        _removePositions.value = mutableListOf()
    }

    fun changeRemoveCheck() = _removeCheck.setValue(true)

    fun findAllMemo() = memoUseCase.findAllMemo()
    fun addRemovePosition(position: Int) {
        val newList = mutableListOf<Int>()
        _removePositions.value?.let {
            newList.addAll(it)
            newList.add(position)
        }
        _removePositions.value = newList
    }

    fun removeRemovePosition(position: Int) {
        val newList = mutableListOf<Int>()
        _removePositions.value?.let {
            newList.addAll(it)
            newList.remove(position)
        }
        _removePositions.value = newList
    }

    fun containsRemovePositionOf(position: Int) = removePositions.value?.contains(position) ?: false
}