package smarthome.client.viewpager.addition

import androidx.fragment.app.Fragment
import smarthome.client.viewpager.PageFactory

class AdditionPageFactory : PageFactory{
    override fun createPageFragment(): Fragment = AdditionFragment()
}