package smarthome.raspberry.home.presentation.main


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import smarthome.raspberry.entity.HomeInfo
import smarthome.raspberry.home.R


class MainActivity : AppCompatActivity(), MainView {
    private val presenter: MainPresenter by inject()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycle.addObserver(presenter)


//        inputController.init()
    }
    
    override fun setAuthStatus(toString: String) {
        auth_status.text = toString
    }
    
    override fun setHomeInfo(homeInfo: HomeInfo) {
        home.text = homeInfo.homeId
        user.text = homeInfo.userId
    }
}
