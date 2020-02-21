package hbs.com.linememo.util

import android.Manifest

object ResourceKeys {
    const val MAKE_MEMO = 300
    const val UPDATE_MEMO = 301

    const val COMPLETED = 200

    const val MEMO_ITEM_KEY ="MemoItem"

    //permission
    const val CAMERA_PERMISSION_CODE = 101
    const val STORAGE_PERMISSION_CODE = 102

    val CAMERA_PERMISSIONS = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )
    val STORAGE_PERMISSIONS = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    const val TAKE_CAMERA = 201
    const val PICK_GALLERY = 202
}