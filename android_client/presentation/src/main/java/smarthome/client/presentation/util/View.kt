package smarthome.client.presentation.util

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.doOnAttach
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import smarthome.client.util.Position
import smarthome.client.util.toPosition

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

val View.center: Position get() = Position(width / 2, height / 2)

fun View.isXInside(toTest: Int): Boolean {
    return toTest >= x && toTest <= (x + width)
}

fun View.isYInside(toTest: Int): Boolean {
    return toTest >= y && toTest <= (y + height)
}

fun View.isPositionInside(position: Position): Boolean {
    return (isXInside(position.x) && isYInside(position.y))
}

fun View.setPosition(position: Position) {
    x = position.x.toFloat()
    y = position.y.toFloat()
}

val View.rawPosition: Position get() = IntArray(2).also { getLocationOnScreen(it) }.toPosition()

fun View.doOnFirstLayout(block: (view: View) -> Unit) {
    doOnAttach {
        OneTimeGlobalLayoutListener(viewTreeObserver) {
            block(this)
        }
    }
}

val View.viewScope: CoroutineScope get() = GlobalScope

fun View.wrapHeight() {
    layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
}