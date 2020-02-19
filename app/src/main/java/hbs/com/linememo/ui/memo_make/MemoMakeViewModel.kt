package hbs.com.linememo.ui.memo_make

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hbs.com.linememo.domain.local.usecase.MemoUseCase
import hbs.com.linememo.domain.model.MemoItem

enum class GalleryStatus {
    GALLERY, CAMERA, INTERNET
}

class MemoMakeViewModel(val memoUseCase: MemoUseCase) : ViewModel() {
    lateinit var navigator:MemoMakeNavigator
    private val _memoItem = MutableLiveData<MemoItem>()
    val memoItem: LiveData<MemoItem> = _memoItem

    init {
        _memoItem.value = MemoItem()
    }

    fun showSelectionThumbnailDialog() {
        navigator.clickSelectionThumbnailDialog()
    }

    fun inputMemo(memoItem: MemoItem) = _memoItem.postValue(memoItem)

    fun saveMemo(memoItem: MemoItem) = memoUseCase.insertMemo(memoItem)
}