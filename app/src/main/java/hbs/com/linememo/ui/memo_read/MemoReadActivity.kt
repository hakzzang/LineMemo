package hbs.com.linememo.ui.memo_read

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import hbs.com.linememo.R
import hbs.com.linememo.databinding.ActivityReadMemoBinding
import hbs.com.linememo.di.*
import hbs.com.linememo.domain.model.MemoItem
import hbs.com.linememo.domain.model.WrapMemoGallery
import hbs.com.linememo.ui.core.BaseActivity
import hbs.com.linememo.ui.core.DataSender
import hbs.com.linememo.ui.core.requestFocusOffMemo
import hbs.com.linememo.ui.core.requestFocusOnMemo
import hbs.com.linememo.ui.gallery.GalleryActivity
import hbs.com.linememo.ui.memo_make.MemoMakeGalleryAdapter
import hbs.com.linememo.ui.memo_make.MemoMakeViewModel
import hbs.com.linememo.util.ImageSelectionBottomDialog
import hbs.com.linememo.util.ResourceKeys
import hbs.com.linememo.util.onShortDateToString
import kotlinx.android.synthetic.main.layout_bottom_bar.view.*
import java.util.*
import javax.inject.Inject


class MemoReadActivity : BaseActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var memoMakeViewModel: MemoMakeViewModel
    lateinit var memoMakeGalleryAdapter: MemoMakeGalleryAdapter

    private val MAX_CLICK_DURATION = 50
    private var startClickTime: Long = 0

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
        initToolbar(binding.toolbar, getString(R.string.all_text_memo_read_title), true)
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

    private fun initView(binding: ActivityReadMemoBinding) {
        binding.lifecycleOwner = this
        memoMakeGalleryAdapter = MemoMakeGalleryAdapter(memoMakeViewModel)
        binding.rvMemoGallery.adapter = memoMakeGalleryAdapter
        binding.rvMemoGallery.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val memoItem = intent.getParcelableExtra<MemoItem>(ResourceKeys.MEMO_ITEM_KEY) ?: return
        compositeDisposable.add(memoMakeViewModel.selectMemoGalleries(memoItem.id).subscribe { memoGalleries ->
            memoMakeGalleryAdapter.initItems(memoGalleries)
        })
        memoMakeViewModel.inputMemo(memoItem)
        binding.memoItem = memoMakeViewModel.memoItem.value
        binding.layoutBottomBar.setOnClickListener {
            ImageSelectionBottomDialog(it.context,makeBottomImageSelectionDialogDelegation()).show()
        }
        binding.layoutBottomBar.tv_update_time.visibility = View.VISIBLE
        binding.layoutBottomBar.tv_update_time.onShortDateToString(memoItem.makeAt)

        binding.etMemoTitle.setOnTouchListener(makeMemoViewTouchListener {
            binding.etMemoTitle.requestFocusOnMemo()
            binding.etMemoContent.requestFocusOffMemo()
        })

        binding.etMemoContent.setOnTouchListener(makeMemoViewTouchListener {
            binding.etMemoTitle.requestFocusOffMemo()
            binding.etMemoContent.requestFocusOnMemo()
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_save_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_save_todo -> {
                memoMakeViewModel.memoItem.value?.let { memoItem ->
                    memoMakeViewModel.updateMemo(updateCurrentItem(memoItem))
                        .flatMap { memoId ->
                            if (memoMakeGalleryAdapter.currentList.size > 0) {
                                memoMakeViewModel.insertMemoGalleries(
                                    memoItem.id,
                                    memoMakeGalleryAdapter.currentList
                                )
                            } else {
                                memoMakeViewModel.removeMemoGalleries(memoItem.id)
                            }
                        }.subscribe {
                        setResult(ResourceKeys.COMPLETED)
                        finish()
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateCurrentItem(memoItem: MemoItem): MemoItem {
        return memoItem.apply {
            if (memoMakeGalleryAdapter.currentList.size > 0) {
                this.thumbnail = memoMakeGalleryAdapter.currentList[0].memoGallery?.filePath ?: ""
            }else{
                this.thumbnail = ""
            }
            this.makeAt = Date()
        }
    }

    fun makeMemoViewTouchListener(focusLogic: () -> Unit) =
        View.OnTouchListener { view, motionEvent ->
            if (motionEvent.actionMasked == MotionEvent.ACTION_DOWN) {
                startClickTime = SystemClock.currentThreadTimeMillis()
                false
            } else if (motionEvent.actionMasked == MotionEvent.ACTION_UP) {
                val endClickTime = SystemClock.currentThreadTimeMillis()
                if (endClickTime - startClickTime < MAX_CLICK_DURATION) {
                    focusLogic()
                    showKeyBoard(view)
                }
                true
            }
            false
        }

    private fun showKeyBoard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, 0)
    }

}