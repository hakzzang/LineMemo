package hbs.com.linememo.ui.memo

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import hbs.com.linememo.R
import hbs.com.linememo.databinding.ActivityMainBinding
import hbs.com.linememo.di.*
import hbs.com.linememo.ui.core.BaseActivity
import hbs.com.linememo.ui.memo_make.MemoMakeActivity
import hbs.com.linememo.ui.memo_make.MemoNavigator
import hbs.com.linememo.ui.memo_remove.MemoRemoveActivity
import hbs.com.linememo.util.ResourceKeys
import javax.inject.Inject

class MemoActivity : BaseActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var memoViewModel: MemoViewModel
    lateinit var memoListAdapter: MemoListAdapter
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this))
            .domainModule(DomainModule())
            .utilityModule(UtilityModule())
            .viewModelModule(ViewModelModule())
            .build()
            .inject(this)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initToolbar(binding.toolbar, getString(R.string.all_text_memo_list_title))
        initViewModel()
        initView(binding)
        findAndNotifyAllMemo()
    }

    private fun initViewModel() {
        memoViewModel =
            ViewModelProvider(viewModelStore, viewModelFactory).get(MemoViewModel::class.java)
        memoViewModel.navigator = object : MemoNavigator {
            override fun callingIntent(intent: Intent) {
                startActivityForResult(intent, ResourceKeys.UPDATE_MEMO)
            }
        }
    }

    private fun initView(binding: ActivityMainBinding) {
        memoListAdapter = MemoListAdapter(memoViewModel)
        binding.rvMemoList.adapter = memoListAdapter
        binding.rvMemoList.layoutManager = LinearLayoutManager(this)

        binding.fabAddMemo.setOnClickListener {
            startActivityForResult(
                Intent(this, MemoMakeActivity::class.java),
                ResourceKeys.MAKE_MEMO
            )
        }

        binding.splMemoList.setOnRefreshListener {
            binding.splMemoList.isRefreshing = true
            findAndNotifyAllMemo()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ResourceKeys.MAKE_MEMO || requestCode == ResourceKeys.UPDATE_MEMO || requestCode == ResourceKeys.REMOVE_MEMO) {
            if (resultCode == ResourceKeys.COMPLETED) {
                findAndNotifyAllMemo()
            }
        }
    }

    private fun findAndNotifyAllMemo() {
        compositeDisposable.add(
            memoViewModel.findAllMemo().subscribe {
                binding.clEmptyContainer.visibility = if (it.size == 0) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
                memoListAdapter.addItems(it, Runnable {
                    binding.rvMemoList.smoothScrollToPosition(0)
                })
            })
        if(binding.splMemoList.isRefreshing){
            binding.splMemoList.isRefreshing = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_remove_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_remove_todo -> {
                val intent = Intent(this, MemoRemoveActivity::class.java)
                startActivityForResult(intent, ResourceKeys.REMOVE_MEMO)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
