package hbs.com.linememo.di

import dagger.Component
import hbs.com.linememo.ui.memo.MemoActivity

@Component(modules = [ActivityModule::class, ViewModelModule::class, UtilityModule::class])
interface ActivityComponent {
    fun inject(memoActivity: MemoActivity)
}