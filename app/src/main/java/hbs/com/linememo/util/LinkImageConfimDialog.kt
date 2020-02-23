package hbs.com.linememo.util

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import hbs.com.linememo.R

class AdMobDialog(context: Context) : AlertDialog(context) {
    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_link_image_confirm)
    }
}
