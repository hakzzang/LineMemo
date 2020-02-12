package hbs.com.linememo.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import hbs.com.linememo.domain.local.repository.MemoRepository
import hbs.com.linememo.ui.memo.MemoViewModel
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
    fun getMemoViewModel(memoRepository: MemoRepository): ViewModel {
        return MemoViewModel(memoRepository)
    }

    @Provides
    fun getViewModelFactory(
        creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
    ): ViewModelFactory {
        return ViewModelFactory(creators)
    }
}