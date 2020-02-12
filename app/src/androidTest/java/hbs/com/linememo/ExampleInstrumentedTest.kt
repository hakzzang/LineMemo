package hbs.com.linememo

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.android.DaggerActivity
import hbs.com.linememo.di.DaggerActivityComponent
import hbs.com.linememo.di.DomainModule
import hbs.com.linememo.di.TestManager

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("hbs.com.linememo", appContext.packageName)
    }

    @Test
    fun checkHasDatabase(){
        val testManager = TestManager()
        DaggerActivityComponent.builder()
            .domainModule(DomainModule())
            .build()
            .inject(testManager)
//        testManager.hasMemoDatabase()
    }
}
