package com.easyhi.manage

import com.easyhi.manage.util.printer.PrintContentParser
import com.easyhi.manage.util.printer.content
import com.google.gson.Gson
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val jsonStr = Gson().toJson(mapOf("name" to "haixiao"))
        val escapeStr = JSONObject.quote(jsonStr)
        println(jsonStr)
        println(escapeStr)
    }

    @Test
    fun testFlow() = runBlocking {
        testSharedFLow()
    }

    private suspend fun testSharedFLow(){
        val sharedFlow = MutableSharedFlow<Int>(
            replay = 4,
            extraBufferCapacity = 0,
            onBufferOverflow = BufferOverflow.SUSPEND
        )

        coroutineScope {
            launch {
                sharedFlow.collect{
                    println("collect1 received ago shared flow $it")
                }
            }
            launch {
                (1..5).forEach {
                    println("emit1  send ago  flow $it")
                    sharedFlow.emit(it)
                    println("emit1 send after flow $it")
                }
            }
            delay(100)
            launch {
                sharedFlow.collect {
                    println("collect2 received shared flow $it")
                }
            }
        }

    }

}