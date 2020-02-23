package hbs.com.linememo.util

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import hbs.com.linememo.R
import kotlinx.android.synthetic.main.dialog_image_selection_bottom.*

interface ImageSelectionBottomDialogDelegation {
    fun selectItem(position: Int)
}

class ImageSelectionBottomDialog(context: Context, private val dialogDelegation:ImageSelectionBottomDialogDelegation) : BottomSheetDialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_image_selection_bottom)
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        ll_selection_camera.setOnClickListener(clickListener)
        ll_selection_gallery.setOnClickListener(clickListener)
        ll_selection_link.setOnClickListener(clickListener)
    }

    private val clickListener = View.OnClickListener {
        when(it.id){
            R.id.ll_selection_camera->
                dialogDelegation.selectItem(0)
            R.id.ll_selection_gallery->
                dialogDelegation.selectItem(1)
            R.id.ll_selection_link->
                dialogDelegation.selectItem(2)
        }
        dismiss()
    }
}