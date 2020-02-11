package smarthome.client.presentation.scripts.addition.dependency.condition

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.viewpager.widget.PagerAdapter
import kotlinx.android.synthetic.main.scripts_condition_container.view.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.presentation.R
import smarthome.client.presentation.scripts.resolver.ConditionViewResolver
import smarthome.client.presentation.util.inflate

class ConditionViewContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), KoinComponent {
    
    private val viewResolver: ConditionViewResolver by inject()
    private val conditions = mutableListOf<Condition>()
    private val adapter = Adapter()
    
    init {
        inflate(R.layout.scripts_condition_container)
        conditions_pager.adapter = adapter
    }
    
    fun setConditions(conditions: List<Condition>) {
        this.conditions.clear()
        this.conditions.addAll(conditions)
        conditions_pager.adapter?.notifyDataSetChanged()
    }
    
    fun getSelectedCondition(): Condition? {
        val conditionView = adapter.getConditionView(conditions_pager.currentItem)
        return conditionView?.getCondition()
    }
    
    inner class Adapter : PagerAdapter() {
        private val items = mutableListOf<ConditionView>()
        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view == obj
        }
        
        override fun getCount(): Int {
            return conditions.size
        }
        
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            return viewResolver.resolve(context, conditions[position])
                ?.also { items.add(it) }
                ?.let { it as View? }
                ?.also(container::addView)
                ?: Object()
        }
        
        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeViewAt(position)
            items.removeAt(position)
        }
    
        fun getConditionView(position: Int): ConditionView? {
            return items[position]
        }
    }
}