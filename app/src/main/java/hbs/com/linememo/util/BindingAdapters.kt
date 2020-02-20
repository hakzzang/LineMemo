package hbs.com.linememo.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat
import java.util.*


@BindingAdapter(value = ["onDateToString"])
fun TextView.onDateToString(date: Date?) {
    if (date == null) {
        return
    }
    val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일")
    val displayDate =dateFormat.format(date)
    text = displayDate
}

@BindingAdapter(value = ["setOnlyText"])
fun TextView.setOnlyText(text:String) {
    this.text = text
}