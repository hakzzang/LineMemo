package hbs.com.linememo.ui.memo

import androidx.lifecycle.ViewModel
import hbs.com.linememo.domain.local.repository.MemoRepository
import hbs.com.linememo.domain.local.usecase.MemoUseCase
import hbs.com.linememo.ui.memo_make.MemoNavigator
import javax.inject.Inject

class MemoViewModel @Inject constructor(val memoUseCase: MemoUseCase) : ViewModel() {
    lateinit var navigator: MemoNavigator

    fun findAllMemo() = memoUseCase.findAllMemo()
}