package hbs.com.linememo.di

import dagger.Module
import dagger.Provides
import hbs.com.linememo.dao.MemoDataBase
import hbs.com.linememo.domain.local.repository.MemoGalleryRepository
import hbs.com.linememo.domain.local.repository.MemoRepository
import hbs.com.linememo.domain.local.usecase.MemoUseCase

@Module
class DomainModule{
    @Provides
    fun provideMemoRepository(memoDataBase: MemoDataBase) = MemoRepository(memoDataBase)

    @Provides
    fun provideMemoGalleryRepository(memoDataBase: MemoDataBase) = MemoGalleryRepository(memoDataBase)

    @Provides
    fun provideMemoUseCase(memoRepository: MemoRepository, memoGalleryRepository: MemoGalleryRepository) = MemoUseCase(memoRepository, memoGalleryRepository)
}