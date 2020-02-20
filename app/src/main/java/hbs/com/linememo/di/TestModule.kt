package hbs.com.linememo.di

import dagger.Module
import dagger.Provides
import hbs.com.linememo.domain.local.usecase.MemoUseCase

@Module
class TestModule{
    @Provides
    fun provideTestScenario(memoUseCase: MemoUseCase) = TestScenario(memoUseCase)
}