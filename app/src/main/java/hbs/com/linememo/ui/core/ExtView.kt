package hbs.com.linememo.ui.core

import android.widget.EditText

fun EditText.requestFocusOnMemo(){
    isFocusableInTouchMode = true
    isFocusable = true
    requestFocus()
}

fun EditText.requestFocusOffMemo(){
    isFocusableInTouchMode = false
    isFocusable = false
}