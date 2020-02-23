package hbs.com.linememo.util

import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import hbs.com.linememo.R
import kotlinx.android.synthetic.main.dialog_link_image_bottom.*

interface ImageLinkBottomDialogDelegation {
    fun sendUrl(url:String)
}

class ImageLinkBottomDialog(context: Context, private val dialogDelegation:ImageLinkBottomDialogDelegation) : BottomSheetDialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_link_image_bottom)
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        btn_image_link.setOnClickListener {
            dialogDelegation.sendUrl(et_image_link_input.text.toString())
            dismiss()
        }
    }
}