package hbs.com.linememo.ui.memo_make

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import hbs.com.linememo.R
import hbs.com.linememo.databinding.ActivityMakeMemoBinding
import hbs.com.linememo.di.*
import hbs.com.linememo.domain.model.WrapMemoGallery
import javax.inject.Inject

class MemoMakeActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var memoMakeViewModel: MemoMakeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this))
            .domainModule(DomainModule())
            .utilityModule(UtilityModule())
            .viewModelModule(ViewModelModule())
            .build()
            .inject(this)

        val binding =
            DataBindingUtil.setContentView<ActivityMakeMemoBinding>(
                this,
                R.layout.activity_make_memo
            )
        initToolbar(binding)
        initViewModel()
        initView(binding)

    }

    private fun initViewModel() {
        memoMakeViewModel =
            ViewModelProvider(viewModelStore, viewModelFactory).get(MemoMakeViewModel::class.java)

        memoMakeViewModel.navigator = object : MemoMakeNavigator {
            override fun clickSelectionThumbnailDialog() {
                showSelectionThumbnailDialog()
            }
        }
    }

    private fun initView(binding: ActivityMakeMemoBinding) {
        binding.rvMemoGallery.adapter = MemoMakeGalleryAdapter(memoMakeViewModel).apply {
            val list = mutableListOf<WrapMemoGallery>()
            list.add(WrapMemoGallery(MemoGalleryViewType.ADD))
            this.submitList(list)
        }
        binding.rvMemoGallery.layoutManager = LinearLayoutManager(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initToolbar(binding: ActivityMakeMemoBinding) {
        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = "메모 등록"
    }

    private fun showSelectionThumbnailDialog(){
        AlertDialog
            .Builder(this@MemoMakeActivity)
            .setTitle(R.string.thumbnail_selection_title)
            .setItems(R.array.thumbnail_selection_items)
            { dialogInterface, position ->

            }.show()
    }
}