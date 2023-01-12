package com.easyhi.manage

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.easyhi.manage.data.local.settingsDataStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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
//        assertEquals("com.easyhi.manage", appContext.packageName)

        runBlocking {
            val settingDataStore = appContext.applicationContext.settingsDataStore
            launch {
                settingDataStore.data.map { setting ->
                    setting.exampleCounter
                }.collectLatest {
                    Log.d("test", "currentCounter = $it")
                }
            }
            launch {
                while (true) {
                    settingDataStore.updateData { setting ->
                        setting.toBuilder().setExampleCounter(setting.exampleCounter + 1)
                            .build()
                    }
                    delay(1000)
                }
            }
        }
    }
}