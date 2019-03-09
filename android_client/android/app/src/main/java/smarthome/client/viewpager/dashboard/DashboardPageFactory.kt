package smarthome.client.viewpager.dashboard

import androidx.fragment.app.Fragment
import smarthome.client.viewpager.PageFactory

class DashboardPageFactory : PageFactory{
    override fun createPageFragment(): Fragment = DashboardFragment()
}