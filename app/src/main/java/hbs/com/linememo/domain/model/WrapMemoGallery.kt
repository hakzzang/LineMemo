package hbs.com.linememo.domain.model

import hbs.com.linememo.ui.memo_make.MemoGalleryViewType

class WrapMemoGallery(val memoGallery: MemoGallery?, val viewType: MemoGalleryViewType){
    constructor(viewType: MemoGalleryViewType) : this(null, viewType)
}