package hbs.com.linememo.di

import hbs.com.linememo.dao.MemoDataBase

class TestManager{
    fun hasMemoDatabase(memoDataBase: MemoDataBase?):Boolean = memoDataBase != null
}