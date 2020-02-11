package smarthome.client.presentation.scripts.addition.dependency.container

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.viewpager.widget.PagerAdapter
import kotlinx.android.synthetic.main.scripts_dependency_unit_container.view.*
import org.koin.core.KoinComponent
import smarthome.client.presentation.R
import smarthome.client.presentation.scripts.addition.dependency.condition.ConditionView
import smarthome.client.presentation.util.inflate

abstract class DependencyUnitContainer <ITEM, ITEMVIEW> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), KoinComponent {
    
    private val items: MutableList<ITEM> = mutableListOf()
    
    private val adapter = Adapter()
    
    init {
        inflate(R.layout.scripts_dependency_unit_container)
        units_pager.adapter = adapter
    }
    
    fun setItems(items: List<ITEM>) {
        this.items.clear()
        this.items.addAll(items)
        units_pager.adapter?.notifyDataSetChanged()
    }
    
    abstract fun inflateItem(context: Context, item: ITEM): ITEMVIEW?
    
    fun getSelectedItemView(): ITEMVIEW? {
        return adapter.getItemView(units_pager.currentItem)
    }
    
    inner class Adapter : PagerAdapter() {
        private val itemviews = mutableListOf<ITEMVIEW>()
        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view == obj
        }
        
        override fun getCount(): Int {
            return items.size
        }
        
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            return inflateItem(context, items[position])
                ?.also { itemviews.add(it) }
                ?.let { it as View? }
                ?.also(container::addView)
                ?: Object()
        }
        
        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeViewAt(position)
            itemviews.removeAt(position)
        }
        
        fun getItemView(position: Int): ITEMVIEW? {
            return itemviews[position]
        }
    }
}