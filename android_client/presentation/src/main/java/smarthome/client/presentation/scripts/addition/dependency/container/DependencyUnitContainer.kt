package smarthome.client.presentation.scripts.addition.dependency.container

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.scripts_dependency_unit_wrapper.view.*
import org.koin.core.KoinComponent
import smarthome.client.presentation.R
import smarthome.client.presentation.util.inflate


abstract class DependencyUnitContainer <ITEM, ITEMVIEW> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ViewPager(context, attrs), KoinComponent {
    
    private val items: MutableList<ITEM> = mutableListOf()
    
    private val unitsAdapter = Adapter()
    
    init {
        adapter = unitsAdapter
    }
    
    fun setItems(items: List<ITEM>) {
        this.items.clear()
        this.items.addAll(items)
        unitsAdapter.notifyDataSetChanged()
    }
    
    abstract fun inflateItem(context: Context, item: ITEM): ITEMVIEW?
    
    fun getSelectedItemView(): ITEMVIEW? {
        return unitsAdapter.getItemView(currentItem)
    }
    
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // copy pasted because wrap content doesn't work with viewpager height
        // https://stackoverflow.com/questions/8394681/android-i-am-unable-to-have-viewpager-wrap-content
        
        var heightMeasureSpec = heightMeasureSpec
        var height = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.measure(widthMeasureSpec,
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
            val h = child.measuredHeight
            if (h > height) height = h
        }
        if (height != 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
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
                ?.also { dependencyUnitView ->
//                    container.inflate(R.layout.scripts_dependency_unit_wrapper)
//                    val wrapper = View.inflate(context, R.layout.scripts_dependency_unit_wrapper, null)
//                    container.addView(wrapper)
//                    wrapper.unit_wrapper.addView(dependencyUnitView)
//                    wrapper.requestLayout()
                    container.addView(dependencyUnitView)
                }
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