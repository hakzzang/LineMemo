package hbs.com.linememo.ui.memo_make

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

enum class GalleryStatus {
    GALLERY, CAMERA, INTERNET
}

class MemoMakeViewModel : ViewModel() {
    private val _galleryStatus = MutableLiveData<GalleryStatus>()
    val galleryStatus: LiveData<GalleryStatus> = _galleryStatus
    fun goGallery() {
        _galleryStatus.postValue(GalleryStatus.GALLERY)
    }
}