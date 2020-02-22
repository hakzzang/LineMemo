package hbs.com.linememo.di

import androidx.lifecycle.ViewModel
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import hbs.com.linememo.domain.local.usecase.MemoUseCase
import hbs.com.linememo.ui.memo.MemoViewModel
import hbs.com.linememo.ui.memo_make.MemoMakeViewModel
import hbs.com.linememo.ui.memo_remove.MemoRemoveViewModel
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
    fun getMemoMakeViewModel(memoUseCase: MemoUseCase): ViewModel {
        return MemoMakeViewModel(memoUseCase)
    }

    @Provides
    @IntoMap
    @ViewModelKey(MemoRemoveViewModel::class)
    fun getMemoRemoveViewModel(memoUseCase: MemoUseCase): ViewModel {
        return MemoRemoveViewModel(memoUseCase)
    }


    @Provides
    fun getViewModelFactory(
        creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
    ): ViewModelFactory {
        return ViewModelFactory(creators)
    }
}