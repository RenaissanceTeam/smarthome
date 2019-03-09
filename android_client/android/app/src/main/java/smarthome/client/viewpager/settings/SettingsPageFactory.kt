package smarthome.client.viewpager.settings

import androidx.fragment.app.Fragment
import smarthome.client.viewpager.PageFactory
import smarthome.client.viewpager.addition.AdditionFragment

class SettingsPageFactory : PageFactory{
    override fun createPageFragment(): Fragment = SettingsFragment()
}