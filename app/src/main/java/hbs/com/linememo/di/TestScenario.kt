package hbs.com.linememo.di

import hbs.com.linememo.domain.local.usecase.MemoUseCase
import hbs.com.linememo.domain.model.MemoItem
import javax.inject.Inject

class TestScenario
constructor(val memoUseCase: MemoUseCase){
    fun findAllMemo() = memoUseCase.findAllMemo()
    fun insertMemo(memoItem: MemoItem) = memoUseCase.insertMemo(memoItem)
}