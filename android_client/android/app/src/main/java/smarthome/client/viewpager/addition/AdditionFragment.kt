package smarthome.client.viewpager.addition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import smarthome.client.R

class AdditionFragment : Fragment() {

    private val viewModel
            by lazy { ViewModelProviders.of(this).get(AdditionViewModel::class.java) }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.device_addition, container, false)
    }
}