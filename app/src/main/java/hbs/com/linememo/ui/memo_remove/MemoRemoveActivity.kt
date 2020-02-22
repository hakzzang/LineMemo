package hbs.com.linememo.ui.memo_remove

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import hbs.com.linememo.R
import hbs.com.linememo.databinding.ActivityRemoveMemoBinding
import hbs.com.linememo.di.*
import hbs.com.linememo.ui.core.BaseActivity
import hbs.com.linememo.util.ResourceKeys
import javax.inject.Inject

class MemoRemoveActivity : BaseActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var memoRemoveViewModel: MemoRemoveViewModel
    lateinit var binding: ActivityRemoveMemoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this))
            .domainModule(DomainModule())
            .utilityModule(UtilityModule())
            .viewModelModule(ViewModelModule())
            .build()
            .inject(this)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_remove_memo)

        initToolbar(binding.toolbar, "메모 삭제", true)
        initViewModel()
        initView(binding)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_remove_menu, menu);
        val menuItem = menu?.findItem(R.id.item_remove_todo)
        memoRemoveViewModel.removePositions.observe(this, Observer {
            menuItem?.isVisible = it.size > 0
            binding.tvAllSelectionMount.text = getString(R.string.all_mount_text, it.size)
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_remove_todo -> {
                memoRemoveViewModel.removePositions.value?.apply {
                    compositeDisposable.add(memoRemoveViewModel.removeMemoItems(this).subscribe {
                        setResult(ResourceKeys.COMPLETED)
                        finish()
                    })
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initViewModel() {
        memoRemoveViewModel =
            ViewModelProvider(viewModelStore, viewModelFactory).get(MemoRemoveViewModel::class.java)
    }

    private fun initView(binding: ActivityRemoveMemoBinding) {
        binding.lifecycleOwner = this
        binding.memoRemoveViewModel = memoRemoveViewModel
        binding.rvMemoList.adapter =
            MemoRemoveAdapter(memoRemoveViewModel).also { memoRemoveAdapter ->
                compositeDisposable.add(memoRemoveViewModel.findAllMemo().subscribe {
                    val sortedList = it.sortedByDescending { it.makeAt.time }
                    memoRemoveAdapter.submitList(sortedList)
                })
            }
        binding.rvMemoList.layoutManager = LinearLayoutManager(this)
    }
}