package hbs.com.linememo.domain.local.usecase

import hbs.com.linememo.domain.local.repository.MemoRepository

class MemoUseCase(private val memoRepository: MemoRepository){
    fun findAllMemo() = memoRepository.findAllMemo()
}