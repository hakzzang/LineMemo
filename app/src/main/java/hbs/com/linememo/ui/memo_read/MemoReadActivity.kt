package hbs.com.linememo.ui.memo_read

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hbs.com.linememo.R
import hbs.com.linememo.databinding.ActivityReadMemoBinding
import hbs.com.linememo.di.*
import hbs.com.linememo.domain.model.MemoItem
import hbs.com.linememo.ui.core.BaseActivity
import hbs.com.linememo.ui.core.DataSender
import hbs.com.linememo.ui.memo_make.MemoMakeGalleryAdapter
import hbs.com.linememo.ui.memo_make.MemoMakeViewModel
import hbs.com.linememo.ui.memo_make.MemoNavigator
import hbs.com.linememo.util.ResourceKeys
import java.util.*
import javax.inject.Inject

class MemoReadActivity : BaseActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var memoMakeViewModel: MemoMakeViewModel
    lateinit var memoMakeGalleryAdapter: MemoMakeGalleryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this))
            .domainModule(DomainModule())
            .utilityModule(UtilityModule())
            .viewModelModule(ViewModelModule())
            .build()
            .inject(this)

        val binding = DataBindingUtil.setContentView<ActivityReadMemoBinding>(
            this,
            R.layout.activity_read_memo
        )
        initToolbar(binding.toolbar, "메모 수정", true)
        initViewModel()
        initView(binding)

        dataSender = object : DataSender {
            override fun sendImage(imageUri: String) {
                memoMakeGalleryAdapter.addItem(imageUri)
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

    private fun initView(binding: ActivityReadMemoBinding) {
        binding.lifecycleOwner = this
        memoMakeGalleryAdapter = MemoMakeGalleryAdapter(memoMakeViewModel)
        binding.rvMemoGallery.adapter = memoMakeGalleryAdapter
        binding.rvMemoGallery.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        compositeDisposable.add(memoMakeViewModel.selectMemoGalleries().subscribe { memoGalleries ->
            memoMakeGalleryAdapter.initItems(memoGalleries)
        })
        val memoItem = intent.getParcelableExtra<MemoItem>(ResourceKeys.MEMO_ITEM_KEY) ?: return
        memoMakeViewModel.inputMemo(memoItem)
        binding.memoItem = memoMakeViewModel.memoItem.value
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_save_todo -> {
                memoMakeViewModel.memoItem.value?.run {
                    memoMakeViewModel.updateMemo(updateDateAt(this)).subscribe {
                        setResult(ResourceKeys.COMPLETED)
                        finish()
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateDateAt(memoItem: MemoItem): MemoItem {
        return memoItem.apply {
            this.makeAt = Date()
        }
    }
}