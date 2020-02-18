package hbs.com.linememo.ui.memo_make

import androidx.lifecycle.ViewModel

enum class GalleryStatus {
    GALLERY, CAMERA, INTERNET
}

class MemoMakeViewModel : ViewModel() {
    lateinit var navigator:MemoMakeNavigator

    fun showSelectionThumbnailDialog() {
        navigator.clickSelectionThumbnailDialog()
    }
}