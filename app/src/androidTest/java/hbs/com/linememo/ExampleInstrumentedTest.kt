package hbs.com.linememo

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import hbs.com.linememo.dao.MemoDataBase
import hbs.com.linememo.di.*
import hbs.com.linememo.domain.local.repository.MemoRepository
import hbs.com.linememo.domain.local.usecase.MemoUseCase
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    lateinit var testScenario: TestScenario

    @Before
    fun injectDependency() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val memoDataBase = Room
            .databaseBuilder(appContext, MemoDataBase::class.java, "MemoDatabase.db")
            .build()
        testScenario = TestScenario(MemoUseCase(MemoRepository(memoDataBase)))

    }

    @Test
    fun hasAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("hbs.com.linememo", appContext.packageName)
    }

    @Test
    fun `MEMO_Database_아이템_갯수`(){
        testScenario.findAllMemo().subscribe({ result ->
            System.out.println("머엋ㅇ이")
            assertEquals("처음 메모의 갯수", result.size, 1)
        },{
            assertEquals("에러 발생:",it.message)
        })
    }

    @Test
    fun checkHasDatabase(){

    }
}
