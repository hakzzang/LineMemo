package hbs.com.linememo.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import hbs.com.linememo.domain.local.repository.MemoRepository
import hbs.com.linememo.domain.local.usecase.MemoUseCase
import hbs.com.linememo.ui.memo.MemoViewModel
import hbs.com.linememo.ui.memo_make.MemoMakeViewModel
import javax.inject.Provider
import kotlin.reflect.KClass

@MapKey
@Target(AnnotationTarget.FUNCTION)
annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
class ViewModelModule{
    @Provides
    @IntoMap
    @ViewModelKey(MemoViewModel::class)
    fun getMemoViewModel(memoUseCase: MemoUseCase): ViewModel {
        return MemoViewModel(memoUseCase)
    }

    @Provides
    @IntoMap
    @ViewModelKey(MemoMakeViewModel::class)
    fun getMemoMakeViewModel(): ViewModel {
        return MemoMakeViewModel()
    }

    @Provides
    fun getViewModelFactory(
        creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
    ): ViewModelFactory {
        return ViewModelFactory(creators)
    }
}