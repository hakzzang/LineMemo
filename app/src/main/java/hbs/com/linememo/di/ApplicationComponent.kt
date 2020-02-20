package hbs.com.linememo.di

import android.app.Application
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DomainModule::class, UtilityModule::class, TestModule::class])
interface ApplicationComponent {

}