package smarthome.client.presentation.util

import android.content.Context
import android.text.InputType.TYPE_CLASS_TEXT
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.widget.addTextChangedListener
import kotlinx.android.synthetic.main.text_input.view.*
import smarthome.client.presentation.R

class TextInput @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var listener: TextWatcher? = null
    
    init {
        inflate(R.layout.text_input)
        
        attrs?.getAttributeValue(androidNamespace, "hint")?.let {
            input_layout.hint = it
        }
        attrs?.getAttributeIntValue(androidNamespace, "inputType", TYPE_CLASS_TEXT)?.let {
            input_field.inputType = it
        }
        attrs?.getAttributeBooleanValue(appNamespace, "passwordToggleEnabled", false)?.let {
            input_layout.isPasswordVisibilityToggleEnabled = it
        }
    }
    
    var text: String
        get() = input_field.text.toString()
        set(value) = input_field.setText(value)
    
    fun setOnTextChanged(listener: (String) -> Unit) {
        this.listener = input_field.addTextChangedListener {
            listener(it?.toString().orEmpty())
        }
    }
    
    fun removeTextChangedListeners() {
        input_field.removeTextChangedListener(listener)
    }
}