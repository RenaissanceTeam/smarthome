package smarthome.client.presentation.scripts.addition.dependency.container

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import kotlinx.android.synthetic.main.scripts_dependency_units_container.view.*
import smarthome.client.presentation.R
import smarthome.client.presentation.util.inflate

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class DependencyUnitContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    
    init {
        inflate(R.layout.scripts_dependency_units_container)
    }
    
    @ModelProp
    lateinit var dependencyUnitModels: List<EpoxyModel<*>>
    
    @AfterPropsSet
    fun onPropsReady() {
        dependency_units_carousel.setModels(dependencyUnitModels)
    }
}

//
//abstract class DependencyUnitContainer <ITEM, ITEMVIEW> @JvmOverloads constructor(
//    context: Context,
//    attrs: AttributeSet? = null
//) : ViewPager(context, attrs), KoinComponent {
//
//    private val items: MutableList<ITEM> = mutableListOf()
//
//    private val unitsAdapter = Adapter()
//
//    init {
//        adapter = unitsAdapter
//    }
//
//    fun setItems(items: List<ITEM>) {
//        this.items.clear()
//        this.items.addAll(items)
//        unitsAdapter.notifyDataSetChanged()
//    }
//
//    abstract fun inflateItem(context: Context, item: ITEM): ITEMVIEW?
//
//    fun getSelectedItemView(): ITEMVIEW? {
//        return unitsAdapter.getItemView(currentItem)
//    }
//
//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        // copy pasted because wrap content doesn't work with viewpager height
//        // https://stackoverflow.com/questions/8394681/android-i-am-unable-to-have-viewpager-wrap-content
//
//        var heightMeasureSpec = heightMeasureSpec
//        var height = 0
//        for (i in 0 until childCount) {
//            val child = getChildAt(i)
//            child.measure(widthMeasureSpec,
//                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
//            val h = child.measuredHeight
//            if (h > height) height = h
//        }
//        if (height != 0) {
//            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
//        }
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//    }
//
//    inner class Adapter : PagerAdapter() {
//        private val itemviews = mutableListOf<ITEMVIEW>()
//        override fun isViewFromObject(view: View, obj: Any): Boolean {
//            return view == obj
//        }
//
//        override fun getCount(): Int {
//            return items.size
//        }
//
//        override fun instantiateItem(container: ViewGroup, position: Int): Any {
//            val item = inflateItem(context, items[position]) ?: return Object()
//
//            itemviews.add(item)
//            val itemView = item as? View
//
//            val wrapper = View.inflate(context, R.layout.scripts_dependency_unit_wrapper, null)
//            wrapper.unit_wrapper.addView(itemView)
//            container.addView(wrapper)
//
//            return wrapper
//        }
//
//        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//            container.removeViewAt(position)
//            itemviews.removeAt(position)
//        }
//
//        fun getItemView(position: Int): ITEMVIEW? {
//            return itemviews[position]
//        }
//    }
//}