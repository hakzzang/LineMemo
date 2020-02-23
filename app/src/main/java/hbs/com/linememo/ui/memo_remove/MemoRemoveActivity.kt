package hbs.com.linememo.ui.memo_remove

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
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
    lateinit var adapter: MemoRemoveAdapter
    private var menuItem: MenuItem? = null
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

        initToolbar(binding.toolbar, getString(R.string.all_text_memo_remove_title), true)
        initViewModel()
        initView(binding)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_remove_menu, menu);
        menuItem = menu?.findItem(R.id.item_remove_todo)
        menuItem?.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_remove_todo -> {
                AlertDialog.Builder(this).setTitle(getString(R.string.all_text_want_delete))
                    .setMessage(getString(R.string.all_text_delete_confirm))
                    .setPositiveButton(R.string.all_text_yes) { dialogInterface, i ->
                        memoRemoveViewModel.removePositions.value?.apply {
                            compositeDisposable.add(memoRemoveViewModel.removeMemoItems(this).subscribe {
                                setResult(ResourceKeys.COMPLETED)
                                finish()
                            })
                        }
                    }.setNegativeButton(R.string.all_text_no) { dialogInterface, _ -> dialogInterface.dismiss()  }.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initViewModel() {
        memoRemoveViewModel =
            ViewModelProvider(viewModelStore, viewModelFactory).get(MemoRemoveViewModel::class.java)
        memoRemoveViewModel.removeAllCheck.observe(this, Observer {
            binding.cbAllSelectionItems.isChecked = it
            if(binding.cbAllSelectionItems.isChecked){
                memoRemoveViewModel.changeAllCheckState(adapter.currentList)
                adapter.notifyCurrentItems()
            }else{
                memoRemoveViewModel.changeAllRemoveCheckState()
                adapter.notifyCurrentItems()
            }
        })
        compositeDisposable.add(memoRemoveViewModel.findAllMemo().subscribe {
            adapter.addAllItems(it)
        })
        memoRemoveViewModel.removePositions.observe(this, Observer {
            menuItem?.setVisible(it.size > 0)
            binding.tvAllSelectionMount.text = getString(R.string.all_mount_text, it.size)
            binding.cbAllSelectionItems.isChecked = it.size == adapter.currentList.size
        })
    }

    private fun initView(binding: ActivityRemoveMemoBinding) {
        adapter = MemoRemoveAdapter(memoRemoveViewModel)
        binding.lifecycleOwner = this
        binding.memoRemoveViewModel = memoRemoveViewModel
        binding.rvMemoList.adapter = adapter
        binding.rvMemoList.layoutManager = LinearLayoutManager(this)
    }
}