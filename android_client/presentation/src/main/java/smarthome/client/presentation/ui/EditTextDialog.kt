package smarthome.client.presentation.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import smarthome.client.presentation.R

class DialogParameters(val title: String,
                       val usePredicate: Boolean = true,
                       val currentValue: String = "",
                       val changedListener: (String) -> Unit)

class EditTextDialog private constructor(
    context: Context,
    private val params: DialogParameters) : AlertDialog(context), TextWatcher {
    
    companion object {
        fun create(context: Context, params: DialogParameters) = EditTextDialog(context, params)
    }
    
    private val editText: EditText? by lazy { rootContentView?.findViewById<EditText>(R.id.input) }
    private var rootContentView: View? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        rootContentView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_text, null)
        setView(rootContentView)
        
        editText?.setText(params.currentValue)
        editText?.addTextChangedListener(this)
        if (params.usePredicate)
            setTitle(context.getString(R.string.edit_text_title).format(params.title))
        else setTitle(params.title)
        setButton(BUTTON_POSITIVE, context.getString(android.R.string.ok)) { _, _ ->
            params.changedListener(editText?.text.toString()); cancel()
        }
        setButton(BUTTON_NEGATIVE, context.getString(android.R.string.cancel)) { _, _ -> cancel() }
        super.onCreate(savedInstanceState)
        
        invalidateOkButton()
    }
    
    override fun afterTextChanged(s: Editable?) {}
    
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) =
        invalidateOkButton()
    
    private fun invalidateOkButton() {
        getButton(BUTTON_POSITIVE).isEnabled = (params.currentValue != editText?.text.toString())
    }
}