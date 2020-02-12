package hbs.com.linememo.ui.memo

import androidx.lifecycle.ViewModel
import hbs.com.linememo.domain.local.repository.MemoRepository
import hbs.com.linememo.domain.local.usecase.MemoUseCase

class MemoViewModel(private val memoUseCase: MemoUseCase) : ViewModel() {
    fun findAllMemo() = memoUseCase.findAllMemo()
}