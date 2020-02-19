package hbs.com.linememo.ui.memo_make

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import hbs.com.linememo.R
import hbs.com.linememo.databinding.ActivityMakeMemoBinding
import hbs.com.linememo.di.*
import hbs.com.linememo.domain.model.WrapMemoGallery
import hbs.com.linememo.ui.core.BaseActivity
import javax.inject.Inject


class MemoMakeActivity : BaseActivity() {
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
        initToolbar(binding.toolbar, "메모 등록")
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
        binding.lifecycleOwner = this
        binding.rvMemoGallery.adapter = MemoMakeGalleryAdapter(memoMakeViewModel).apply {
            val list = mutableListOf<WrapMemoGallery>()
            list.add(WrapMemoGallery(MemoGalleryViewType.ADD))
            this.submitList(list)
        }
        binding.rvMemoGallery.layoutManager = LinearLayoutManager(this)
        binding.memoItem = memoMakeViewModel.memoItem.value
    }

    private fun showSelectionThumbnailDialog() {
        AlertDialog
            .Builder(this@MemoMakeActivity)
            .setTitle(R.string.thumbnail_selection_title)
            .setItems(R.array.thumbnail_selection_items)
            { dialogInterface, position ->

            }.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_save_todo-> {
                Log.d("isEmpty",memoMakeViewModel.memoItem.value?.toString())
                memoMakeViewModel.memoItem.value?.run {
                    Toast.makeText(this@MemoMakeActivity, this.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
//                memoMakeViewModel.memoItem.value?.run {
//                    memoMakeViewModel.saveMemo(this)
//                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}