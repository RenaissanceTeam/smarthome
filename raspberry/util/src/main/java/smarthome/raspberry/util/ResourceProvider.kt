package smarthome.raspberry.util

import android.content.Context
import android.content.res.Resources

class ResourceProvider(private val context: Context) {
    val resources: Resources
        get() = context.resources
}