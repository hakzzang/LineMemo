package hbs.com.linememo.ui.memo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import hbs.com.linememo.R
import hbs.com.linememo.databinding.ActivityMainBinding
import hbs.com.linememo.di.DaggerActivityComponent
import hbs.com.linememo.di.ViewModelFactory
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MemoActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var memoViewModel: MemoViewModel
    private val compositeDisposable = CompositeDisposable()
    private val memoListAdapter = MemoListAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerActivityComponent.builder()
            .build()
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
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

    }
}
