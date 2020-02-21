package hbs.com.linememo.ui.memo

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import hbs.com.linememo.R
import hbs.com.linememo.databinding.ActivityMainBinding
import hbs.com.linememo.di.*
import hbs.com.linememo.ui.core.BaseActivity
import hbs.com.linememo.ui.memo_make.MemoMakeActivity
import hbs.com.linememo.ui.memo_make.MemoNavigator
import hbs.com.linememo.util.ResourceKeys
import io.reactivex.disposables.CompositeDisposable
import java.util.*
import javax.inject.Inject

class MemoActivity : BaseActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var memoViewModel: MemoViewModel
    private val compositeDisposable = CompositeDisposable()
    lateinit var memoListAdapter : MemoListAdapter
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
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        initToolbar(binding.toolbar, "메모 리스트")
        initViewModel()
        initView(binding)
        findAndNotifyAllMemo()
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    private fun initViewModel() {
        memoViewModel = ViewModelProvider(viewModelStore, viewModelFactory).get(MemoViewModel::class.java)
        memoViewModel.navigator = object : MemoNavigator{
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ResourceKeys.MAKE_MEMO) {
            if (resultCode == ResourceKeys.COMPLETED) {
                findAndNotifyAllMemo()
            }
        }

        if(requestCode == ResourceKeys.UPDATE_MEMO){
            if(resultCode == ResourceKeys.COMPLETED){
                findAndNotifyAllMemo()
            }
        }
    }

    private fun findAndNotifyAllMemo(){
        compositeDisposable.add(
            memoViewModel.findAllMemo().subscribe {
                val sortedList =it.sortedByDescending { it.makeAt.time }
                memoListAdapter.submitList(sortedList)
            })
    }
}
