package hbs.com.linememo.domain.local.usecase

import hbs.com.linememo.domain.local.repository.MemoRepository
import hbs.com.linememo.domain.model.MemoItem

class MemoUseCase(private val memoRepository: MemoRepository){
    fun findAllMemo() = memoRepository.findAllMemo()
    fun insertMemo(memoItem: MemoItem) = memoRepository.insert(memoItem)
}