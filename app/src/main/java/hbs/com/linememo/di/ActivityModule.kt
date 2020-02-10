package hbs.com.linememo.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val context: Context){
    @Provides
    fun providerContext() = context
}