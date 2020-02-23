package hbs.com.linememo.ui.gallery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import hbs.com.linememo.R
import hbs.com.linememo.databinding.ActivityGalleryBinding
import hbs.com.linememo.domain.model.WrapMemoGallery
import hbs.com.linememo.util.ResourceKeys

class GalleryActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val memoGalleries= intent.getParcelableArrayListExtra<WrapMemoGallery>(ResourceKeys.GALLERY_ITEM_KEY)
        val binding = DataBindingUtil.setContentView<ActivityGalleryBinding>(this, R.layout.activity_gallery)
        binding.vpGallery.adapter = GalleryAdapter().apply {
            submitList(memoGalleries)
        }

        binding.vpGallery.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tvGalleryChip.text = "${position+1}/${memoGalleries.size}"
            }
        })

        binding.ivGalleryExit.setOnClickListener {
            finish()
        }
    }
}