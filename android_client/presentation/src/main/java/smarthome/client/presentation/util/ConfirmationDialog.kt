package smarthome.client.presentation.util

import android.content.Context
import androidx.appcompat.app.AlertDialog
import kotlinx.coroutines.suspendCancellableCoroutine

suspend fun confirmAction(context: Context?, block: ConfirmationDialog.() -> Unit): Boolean {
    return ConfirmationDialog()
        .apply(block)
        .apply { this.context = context }
        .show()
}

class ConfirmationDialog {
    var context: Context? = null
    var title = ""
    var message = ""
    var icon = NO_ICON
    
    suspend fun show(): Boolean {
        val context = context ?: return false
        return suspendCancellableCoroutine { continuation ->
            AlertDialog.Builder(context)
                .setTitle(title)
                .apply { if (message.isNotEmpty()) setMessage(message) }
                .apply { if (icon != NO_ICON) setIcon(icon) }
                .setPositiveButton(android.R.string.yes) { _, _ ->
                    continuation.resumeWith(Result.success(true))
                }
                .setNegativeButton(android.R.string.no, null)
                .setOnCancelListener { continuation.resumeWith(Result.success(false)) }
                .show()
            
        }
    }
    
    companion object {
        private const val NO_ICON = -1
    }
}