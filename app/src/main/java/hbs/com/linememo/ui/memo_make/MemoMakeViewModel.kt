package hbs.com.linememo.ui.memo_make

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hbs.com.linememo.domain.local.usecase.MemoUseCase
import hbs.com.linememo.domain.model.MemoGallery
import hbs.com.linememo.domain.model.MemoItem
import hbs.com.linememo.domain.model.WrapMemoGallery
import hbs.com.linememo.util.SingleLiveEvent
import io.reactivex.Observable
import javax.inject.Inject

class MemoMakeViewModel @Inject constructor(
    val memoUseCase: MemoUseCase
) : ViewModel() {
    lateinit var navigator:MemoNavigator
    private val _memoItem = MutableLiveData<MemoItem>()
    val memoItem: LiveData<MemoItem> = _memoItem

    private val _showThumbnail = SingleLiveEvent<List<WrapMemoGallery>>()
    val showThumbnail: LiveData<List<WrapMemoGallery>> = _showThumbnail

    init {
        _memoItem.value = MemoItem()
    }

    fun showSelectionThumbnailDialog() {
        navigator.showChoiceThumbnailDialog()
    }

    fun inputMemo(memoItem: MemoItem) = _memoItem.setValue(memoItem)
    fun insertMemo(memoItem: MemoItem) = memoUseCase.insertMemo(memoItem)
    fun updateMemo(memoItem: MemoItem) = memoUseCase.updateMemo(memoItem)

    fun insertMemoGalleries(memoId: Int, memoGalleries: MutableList<WrapMemoGallery>) =
        memoUseCase.insertMemoGalleries(memoId, memoGalleries)

    fun selectMemoGalleries(memoId: Int): Observable<List<MemoGallery>> = memoUseCase.selectMemoGalleries(memoId)
    fun removeMemoGalleries(memoId: Int): Observable<Unit> = memoUseCase.removeMemoGalleries(memoId)

    fun showThumbnail(thumbnails: List<WrapMemoGallery>) {
        _showThumbnail.value = thumbnails
    }
}