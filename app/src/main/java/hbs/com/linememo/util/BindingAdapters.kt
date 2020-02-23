package hbs.com.linememo.util

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat
import java.util.*


@BindingAdapter(value = ["onDateToString"])
fun TextView.onDateToString(date: Date?) {
    if (date == null) {
        return
    }
    val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일\nhh시 mm분")
    val displayDate =dateFormat.format(date)
    text = displayDate
}

fun TextView.onShortDateToString(date: Date?) {
    if (date == null) {
        return
    }

    val dateFormat = SimpleDateFormat("수정 : hh시 mm분")
    val displayDate =dateFormat.format(date)
    text = displayDate
}

@BindingAdapter(value = ["setOnlyText"])
fun TextView.setOnlyText(text:String) {
    this.text = text
}

@BindingAdapter(value= ["setThumbnail"])
fun ImageView.setThumbnail(filePath:String){
    Glide.with(this).load(filePath).apply(RequestOptions.circleCropTransform().override(width, height)).into(this)
}