package hbs.com.linememo.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import hbs.com.linememo.dao.MemoDataBase

@Module
class UtilityModule{
    @Provides
    fun provideMemoRoomBuilder(context:Context) = Room
        .databaseBuilder(context, MemoDataBase::class.java, "MemoDatabase.db")
        .build()
}