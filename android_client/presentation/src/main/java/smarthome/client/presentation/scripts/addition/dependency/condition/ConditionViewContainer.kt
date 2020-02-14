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
    
    init {
        inflate(R.layout.scripts_condition_container)
        conditions_pager.adapter = Adapter()
    }
    
    fun setConditions(conditions: List<Condition>) {
        this.conditions.clear()
        this.conditions.addAll(conditions)
        conditions_pager.adapter?.notifyDataSetChanged()
    }
    
    inner class Adapter : PagerAdapter() {
        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view == obj
        }
        
        override fun getCount(): Int {
            return conditions.size
        }
        
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            return (viewResolver.resolve(context, conditions[position]) as? View)
                ?.also(container::addView)
                ?: Object()
        }
        
        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeViewAt(position)
        }
    }
}