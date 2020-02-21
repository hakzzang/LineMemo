package hbs.com.linememo.ui.memo_make

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hbs.com.linememo.R
import hbs.com.linememo.databinding.ActivityMakeMemoBinding
import hbs.com.linememo.di.*
import hbs.com.linememo.ui.core.BaseActivity
import hbs.com.linememo.ui.core.DataSender
import hbs.com.linememo.util.ResourceKeys
import javax.inject.Inject


class MemoMakeActivity : BaseActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var memoMakeViewModel: MemoMakeViewModel
    lateinit var memoMakeAdapter: MemoMakeGalleryAdapter
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
        initToolbar(binding.toolbar, "메모 등록", true)
        initViewModel()
        initView(binding)

        dataSender = object : DataSender {
            override fun sendImage(imageUri: String) {
                memoMakeAdapter.addItem(imageUri)
            }
        }
    }

    private fun initViewModel() {
        memoMakeViewModel =
            ViewModelProvider(viewModelStore, viewModelFactory).get(MemoMakeViewModel::class.java)

        memoMakeViewModel.navigator = object : MemoNavigator {
            override fun showChoiceThumbnailDialog() {
                showSelectionThumbnailDialog()
            }
        }
    }

    private fun initView(binding: ActivityMakeMemoBinding) {
        binding.lifecycleOwner = this
        memoMakeAdapter = MemoMakeGalleryAdapter(memoMakeViewModel)
        binding.rvMemoGallery.adapter = memoMakeAdapter.apply {
            memoMakeAdapter.initItems()
        }
        binding.rvMemoGallery.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.memoItem = memoMakeViewModel.memoItem.value
    }

    private fun showSelectionThumbnailDialog() {
        AlertDialog
            .Builder(this@MemoMakeActivity)
            .setTitle(R.string.thumbnail_selection_title)
            .setItems(R.array.thumbnail_selection_items)
            { dialogInterface, position ->
                if (position == 0) {
                    checkPermissions(
                        ResourceKeys.CAMERA_PERMISSIONS,
                        ResourceKeys.CAMERA_PERMISSION_CODE
                    )
                } else if (position == 1) {
                    checkPermissions(
                        ResourceKeys.STORAGE_PERMISSIONS,
                        ResourceKeys.STORAGE_PERMISSION_CODE
                    )
                }
            }.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_save_todo -> {
                memoMakeViewModel.memoItem.value?.run {
                    memoMakeViewModel.saveMemo(this).subscribe {
                        setResult(ResourceKeys.COMPLETED)
                        finish()
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}