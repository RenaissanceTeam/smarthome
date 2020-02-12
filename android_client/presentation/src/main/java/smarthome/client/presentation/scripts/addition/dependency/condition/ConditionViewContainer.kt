package smarthome.client.presentation.scripts.addition.dependency.condition

//@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
//class ConditionViewContainer @JvmOverloads constructor(
//    context: Context,
//    attrs: AttributeSet? = null,
//    defStyleAttr: Int = 0
//) : DependencyUnitContainer<Condition, ConditionView>(context, attrs), KoinComponent {
//
//    private val viewResolver: ConditionViewResolver by inject()
//
//    override fun inflateItem(context: Context, condition: Condition): ConditionView? {
//        return viewResolver.resolve(context, condition)?.also { it.setCondition(condition) }
//    }
//
//    @ModelProp lateinit var state: ConditionContainerState
//
//    @AfterPropsSet
//    fun onPropsReady() {
//        setItems(state.conditions)
//    }
//}