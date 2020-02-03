package smarthome.client.presentation.util

import android.app.Activity
import android.content.ContextWrapper
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner

fun Fragment.hideSoftKeyboard() {
    val activity = activity ?: return
    val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
    activity.currentFocus?.windowToken?.let {
        inputMethodManager?.hideSoftInputFromWindow(it, 0)
    }
}

fun ViewGroup.inflate(layout: Int): View {
    return View.inflate(context, layout, this)
}

val View.lifecycleOwner: LifecycleOwner? get() {
    var currentContext = context
    
    while (currentContext is ContextWrapper) {
        if (currentContext is LifecycleOwner) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    
    return null
}

val View.center: Position get() = Position(width / 2f, height / 2f)

fun View.isXInside(toTest: Float): Boolean {
    return toTest >= x && toTest <= (x + width)
}

fun View.isYInside(toTest: Float): Boolean {
    return toTest >= y && toTest <= (y + height)
}

fun View.isPositionInside(position: Position): Boolean {
    return isXInside(position.x) && isYInside(position.y)
}