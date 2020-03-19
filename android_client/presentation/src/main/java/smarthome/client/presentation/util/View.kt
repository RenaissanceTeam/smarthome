package smarthome.client.presentation.util

import android.app.Activity
import android.content.ContextWrapper
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.jakewharton.rxbinding3.view.touches
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import smarthome.client.util.Position
import smarthome.client.util.log

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

val View.center: Position get() = Position(width / 2, height / 2)

fun View.isXInside(toTest: Int): Boolean {
    return toTest >= x && toTest <= (x + width)
}

fun View.isYInside(toTest: Int): Boolean {
    return toTest >= y && toTest <= (y + height)
}

fun View.isPositionInside(position: Position): Boolean {
    return (isXInside(position.x) && isYInside(position.y)).apply {
        if (this) {
//            log("pos $position is inside. x=$x, y=$y, wi=$width, he=$height")
            //pos Position(x=490, y=1411) is inside. x=205.0, y=1249.0, wi=710, he=310
        }
    }
}

fun View.setPosition(position: Position) {
    x = position.x.toFloat()
    y = position.y.toFloat()
}

val View.viewScope: CoroutineScope get() = GlobalScope

fun View.wrapHeight() {
    layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
}