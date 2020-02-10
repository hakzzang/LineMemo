package hbs.com.linememo.di

import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module
class ViewModelModule{
    @Provides
    fun getViewModelFactory(
        creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
    ): ViewModelFactory {
        return ViewModelFactory(creators)
    }
}