package hbs.com.linememo.ui.memo_make

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
import hbs.com.linememo.ui.core.BaseActivity
import hbs.com.linememo.ui.core.DataSender
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
        initToolbar(binding.toolbar, "메모 등록", true)
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

    private fun initView(binding: ActivityMakeMemoBinding) {
        binding.lifecycleOwner = this
        memoMakeGalleryAdapter = MemoMakeGalleryAdapter(memoMakeViewModel)
        binding.rvMemoGallery.adapter = memoMakeGalleryAdapter.apply {
            initItems()
        }
        binding.rvMemoGallery.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
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
                    if (memoMakeGalleryAdapter.currentList.size > 1) {
                        //1인 이유는 0은 항상 ADD 라서,
                        this.thumbnail = memoMakeGalleryAdapter.currentList[1].memoGallery?.filePath?:""
                    }
                    compositeDisposable.add(memoMakeViewModel.insertMemo(this)
                        .flatMap { memoId ->
                            if(memoMakeGalleryAdapter.currentList.size>1){
                                memoMakeViewModel.insertMemoGalleries(memoId, memoMakeGalleryAdapter.currentList)
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