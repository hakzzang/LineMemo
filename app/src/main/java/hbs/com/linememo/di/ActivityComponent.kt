package hbs.com.linememo.di

import dagger.Component
import hbs.com.linememo.ui.memo.MemoActivity

@Component(modules = [ActivityModule::class, ViewModelModule::class, DomainModule::class, UtilityModule::class])
interface ActivityComponent {
    fun inject(memoActivity: MemoActivity)
    fun inject(testManager: TestManager)
}