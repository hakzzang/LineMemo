package hbs.com.linememo.ui.memo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import hbs.com.linememo.R
import hbs.com.linememo.databinding.ActivityMainBinding
import hbs.com.linememo.di.DaggerActivityComponent
import hbs.com.linememo.di.ViewModelFactory
import hbs.com.linememo.di.ViewModelModule
import javax.inject.Inject

class MemoActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var memoViewModel : MemoViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerActivityComponent.builder()
            .build()
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        initViewModel()
    }

    private fun initViewModel(){
        memoViewModel = ViewModelProvider(viewModelStore, viewModelFactory).get(MemoViewModel::class.java)
    }
}
