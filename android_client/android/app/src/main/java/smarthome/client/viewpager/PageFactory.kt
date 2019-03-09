package smarthome.client.viewpager

import androidx.fragment.app.Fragment

interface PageFactory {
    fun createPageFragment() : Fragment
}