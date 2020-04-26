package smarthome.client.util

import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.tylerthrailkill.helpers.prettyprint.pp

private class Logs {
    
    
    companion object {
        init {
            val s = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(0)         // (Optional) How many method line to show. Default 2
                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
                .tag("shlog")
                .build()
            
            Logger.addLogAdapter(AndroidLogAdapter(s))
        }
        
        fun log(msg: String) {
            Logger.d(msg)
        }
        
        fun log(thr: Throwable) {
            Logger.e(thr, thr.message.orEmpty())
        }
    }
}

fun log(msg: String, tag: String = "shlog") = Logs.log(msg)
fun log(thr: Throwable, tag: String = "shlog") = Logs.log(thr)
fun logObject(a: Any?) {
    val result = StringBuilder()
    pp(a, writeTo = result)
    Logs.log(result.toString())
}