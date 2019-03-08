package smarthome.client.viewpager

import androidx.fragment.app.Fragment
import smarthome.client.R
import smarthome.client.viewpager.addition.AdditionPageFactory
import smarthome.client.viewpager.dashboard.DashboardPageFactory
import smarthome.client.viewpager.scripts.ScriptsPageFactory
import smarthome.client.viewpager.settings.SettingsPageFactory

enum class Pages(private val factory: PageFactory, val menuItemId: Int) {
    DASHBOARD(DashboardPageFactory(), R.id.dashboard),
    SCRIPTS(ScriptsPageFactory(), R.id.scripts),
    ADDITION(AdditionPageFactory(), R.id.addition),
    SETTINGS(SettingsPageFactory(), R.id.settings);

    fun getFragment(): Fragment = factory.createPageFragment()
}