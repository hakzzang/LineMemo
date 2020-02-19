package hbs.com.linememo.ui.memo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import hbs.com.linememo.R
import hbs.com.linememo.databinding.ActivityMainBinding
import hbs.com.linememo.di.*
import hbs.com.linememo.ui.core.BaseActivity
import hbs.com.linememo.ui.memo_make.MemoMakeActivity
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MemoActivity : BaseActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var memoViewModel: MemoViewModel
    private val compositeDisposable = CompositeDisposable()
    private val memoListAdapter = MemoListAdapter()
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
    }

    private fun initViewModel() {
        memoViewModel =
            ViewModelProvider(viewModelStore, viewModelFactory).get(MemoViewModel::class.java)
    }

    private fun initView(binding: ActivityMainBinding) {
        binding.rvMemoList.adapter = memoListAdapter
        binding.rvMemoList.layoutManager = LinearLayoutManager(this)
        compositeDisposable.add(
            memoViewModel.findAllMemo().subscribe {
                memoListAdapter.submitList(it)
            })
        binding.fabAddMemo.setOnClickListener {
            startActivity(Intent(this, MemoMakeActivity::class.java))
        }
    }
}
