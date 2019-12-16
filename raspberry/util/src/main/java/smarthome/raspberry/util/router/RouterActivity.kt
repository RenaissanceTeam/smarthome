package smarthome.raspberry.util.router

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module
import kotlin.reflect.KClass


open class RouterActivity : Router, AppCompatActivity() {
    
    private val routerModule = module(override = true) {
        single<Router> { this@RouterActivity }
    }
    
    init {
        loadKoinModules(routerModule)
    }
    
    override fun onDestroy() {
        super.onDestroy()
        
        unloadKoinModules(routerModule)
    }
    
    override fun <T : Any> startFlow(to: KClass<T>, flags: Int) {
        startActivity(Intent(this, to.java).setFlags(flags))
    }
}