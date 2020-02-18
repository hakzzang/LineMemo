package hbs.com.linememo.di

import dagger.Component
import hbs.com.linememo.ui.memo.MemoActivity
import hbs.com.linememo.ui.memo_make.MemoMakeActivity

@Component(modules = [ActivityModule::class, ViewModelModule::class, DomainModule::class, UtilityModule::class])
interface ActivityComponent {
    fun inject(memoActivity: MemoActivity)
    fun inject(memoMakeActivity: MemoMakeActivity)
    fun inject(testManager: TestManager)
}