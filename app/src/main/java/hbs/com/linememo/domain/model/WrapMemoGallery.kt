package hbs.com.linememo.domain.model

import android.os.Parcelable
import hbs.com.linememo.ui.memo_make.MemoGalleryViewType
import kotlinx.android.parcel.Parcelize

@Parcelize
class WrapMemoGallery(val memoGallery: MemoGallery?, val viewType: MemoGalleryViewType) :
    Parcelable {
    constructor(viewType: MemoGalleryViewType) : this(null, viewType)
}