//package smarthome.client.presentation.controllers.controllerdetail.statechanger
//
//import android.text.Editable
//import android.text.InputType.TYPE_CLASS_NUMBER
//import android.text.TextWatcher
//import android.view.ViewGroup
//import android.widget.EditText
//import com.dd.processbutton.iml.ActionProcessButton
//import smarthome.client.presentation.R
//
//const val DEFAULT_TEXT_TYPE = "default"
//const val DIGITS_ONLY_TEXT_TYPE = "digits"
//
//class TextReadWriteStateChanger(container: ViewGroup,
//                                writeListener: (String) -> Unit,
//                                readListener: () -> Unit,
//                                textType: String = DEFAULT_TEXT_TYPE) :
//    ControllerStateChanger(container) {
//
//    private val normalProgress = 0
//    private val loadingProgress = 50
//    private val unknownState = "Unknown"
//
//    override val layout: Int
//        get() = R.layout.state_changer_text_read_write
//
//    private var currentState = ""
//    private val editText = rootView.findViewById<EditText>(R.id.state_changer_text_input)
//    private val writeButton = rootView.findViewById<ActionProcessButton>(R.id.write_button)
//    private val readButton = rootView.findViewById<ActionProcessButton>(R.id.read_button)
//    private var pressedButton = writeButton
//
//    init {
//        readButton.setOnClickListener {
//            pressedButton = readButton
//            readListener()
//        }
//        writeButton.normalText = WRITE_DEFAULT_TITLE
//        writeButton.setOnClickListener {
//            pressedButton = writeButton
//            writeListener(currentState)
//        }
//
//        if (textType == DIGITS_ONLY_TEXT_TYPE)
//            editText.inputType = TYPE_CLASS_NUMBER
//
//        editText.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                currentState = s.toString()
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//        })
//    }
//
//
//    override fun invalidateNewState(state: String) {
//        currentState = state
//        writeButton.progress = normalProgress
//    }
//}