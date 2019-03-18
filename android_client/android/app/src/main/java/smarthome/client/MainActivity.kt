package smarthome.client

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import smarthome.client.viewpager.Pages
import smarthome.client.viewpager.ViewpagerAdapter

class MainActivity : FragmentActivity() {

    private val viewModel
            by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }

    private val bottomNavigation by lazy { findViewById<BottomNavigationView>(R.id.bottom_navigation) }
    private val viewpager by lazy { findViewById<ViewPager>(R.id.viewpager) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.needAuth.observe(this, Observer { needAuth -> if (needAuth) launchAuthActivity() })
        viewModel.page.observe(this, Observer { viewpager.currentItem = it })

        viewpager.adapter = ViewpagerAdapter(supportFragmentManager)
        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(a: Int) = Unit
            override fun onPageScrolled(a: Int, b: Float, c: Int) = Unit
            override fun onPageSelected(position: Int) {
                bottomNavigation.selectedItemId = Pages.values()[position].menuItemId
            }
        })

        bottomNavigation.setOnNavigationItemSelectedListener { viewModel.onBottomNavigationClick(it) }
    }

    override fun onStart() {
        super.onStart()
        viewModel.authCheck()
    }

    private fun launchAuthActivity() {
        startActivityForResult(viewModel.authUiWrapper.getAuthIntent(), RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                viewModel.onAuthSuccessful()
            } else {
                viewModel.onAuthFailed()
            }
        }
    }
}