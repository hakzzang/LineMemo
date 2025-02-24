package hbs.com.linememo.ui.memo_make

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hbs.com.linememo.R
import hbs.com.linememo.databinding.ActivityMakeMemoBinding
import hbs.com.linememo.di.*
import hbs.com.linememo.domain.model.WrapMemoGallery
import hbs.com.linememo.ui.core.BaseActivity
import hbs.com.linememo.ui.core.DataSender
import hbs.com.linememo.ui.gallery.GalleryActivity
import hbs.com.linememo.util.ImageSelectionBottomDialog
import hbs.com.linememo.util.ResourceKeys
import io.reactivex.Observable
import javax.inject.Inject


class MemoMakeActivity : BaseActivity() {
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

        val binding =
            DataBindingUtil.setContentView<ActivityMakeMemoBinding>(
                this,
                R.layout.activity_make_memo
            )
        initToolbar(binding.toolbar, getString(R.string.all_text_memo_insert_title), true)
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
        memoMakeViewModel.showThumbnail.observe(this, androidx.lifecycle.Observer { galleries ->
            Intent(this, GalleryActivity::class.java).apply {
                val galleryArrayList = arrayListOf<WrapMemoGallery>()
                galleryArrayList.addAll(galleries)
                putParcelableArrayListExtra(ResourceKeys.GALLERY_ITEM_KEY, galleryArrayList)
                startActivity(this)
            }
        })
    }

    private fun initView(binding: ActivityMakeMemoBinding) {
        binding.lifecycleOwner = this
        memoMakeGalleryAdapter = MemoMakeGalleryAdapter(memoMakeViewModel)
        binding.rvMemoGallery.adapter = memoMakeGalleryAdapter.apply {
            initItems()
        }
        binding.rvMemoGallery.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.memoItem = memoMakeViewModel.memoItem.value
        binding.layoutBottomBar.setOnClickListener {
            ImageSelectionBottomDialog(it.context, makeBottomImageSelectionDialogDelegation()).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_save_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_save_todo -> {
                memoMakeViewModel.memoItem.value?.run {
                    if (memoMakeGalleryAdapter.currentList.size > 0) {
                        this.thumbnail =
                            memoMakeGalleryAdapter.currentList[0].memoGallery?.filePath ?: ""
                    }
                    compositeDisposable.add(memoMakeViewModel.insertMemo(this)
                        .flatMap { memoId ->
                            if (memoMakeGalleryAdapter.currentList.size > 0) {
                                memoMakeViewModel.insertMemoGalleries(memoId.toInt(), memoMakeGalleryAdapter.currentList)
                            }else{
                                Observable.just(0)
                            }
                        }.subscribe({
                            setResult(ResourceKeys.COMPLETED)
                            finish()
                        }, {
                            Log.d("errorrr,", it.message)
                        })
                    )
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}