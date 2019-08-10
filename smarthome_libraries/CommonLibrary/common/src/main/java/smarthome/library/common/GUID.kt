package smarthome.library.common

import java.text.SimpleDateFormat
import java.util.Date
import java.util.HashSet

import kotlin.random.Random

/**
 * IoT device can be identified by its guid. As well as its controllers
 */
object GUID {
    private val mGuids = HashSet<Long>()

    val guidForMessage: String
        get() {
            val sdf = SimpleDateFormat("dd.MM.yyyy_HH:mm:ss")
            return sdf.format(Date())
        }

    fun remove(guid: Long) {
        mGuids.remove(guid)
    }

    fun getGuidForName(name: String): Long {
        return name.hashCode().toLong()
    }

    fun random(): Long {
        return Random.nextLong()
    }
}
