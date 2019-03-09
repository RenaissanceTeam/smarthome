package smarthome.client.viewpager.scripts

import androidx.fragment.app.Fragment
import smarthome.client.viewpager.PageFactory

class ScriptsPageFactory : PageFactory{
    override fun createPageFragment(): Fragment = ScriptsFragment()
}