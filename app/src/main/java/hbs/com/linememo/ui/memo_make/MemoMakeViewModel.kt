package hbs.com.linememo.ui.memo_make

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hbs.com.linememo.domain.local.usecase.MemoUseCase
import hbs.com.linememo.domain.model.MemoGallery
import hbs.com.linememo.domain.model.MemoItem
import hbs.com.linememo.domain.model.WrapMemoGallery
import io.reactivex.Observable
import javax.inject.Inject

class MemoMakeViewModel @Inject constructor(
    val memoUseCase: MemoUseCase
) : ViewModel() {
    lateinit var navigator:MemoNavigator
    private val _memoItem = MutableLiveData<MemoItem>()
    val memoItem: LiveData<MemoItem> = _memoItem

    init {
        _memoItem.value = MemoItem()
    }

    fun showSelectionThumbnailDialog() {
        navigator.showChoiceThumbnailDialog()
    }

    fun inputMemo(memoItem: MemoItem) = _memoItem.setValue(memoItem)
    fun insertMemo(memoItem: MemoItem) = memoUseCase.insertMemo(memoItem)
    fun updateMemo(memoItem: MemoItem) = memoUseCase.updateMemo(memoItem)

    fun insertMemoGalleries(memoId: Long, memoGalleries: MutableList<WrapMemoGallery>) =
        memoUseCase.insertMemoGalleries(memoId, memoGalleries)

    fun selectMemoGalleries(memoId: Int): Observable<List<MemoGallery>> = memoUseCase.selectMemoGalleries(memoId)

}