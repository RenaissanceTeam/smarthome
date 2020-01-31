package smarthome.client.presentation.util

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

fun Fragment.hideSoftKeyboard() {
    val activity = activity ?: return
    val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
    activity.currentFocus?.windowToken?.let {
        inputMethodManager?.hideSoftInputFromWindow(it, 0)
    }
}