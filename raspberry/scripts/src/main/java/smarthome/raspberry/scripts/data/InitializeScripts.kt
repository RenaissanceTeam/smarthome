package smarthome.raspberry.scripts.data

import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component

@Component
class InitializeScripts : InitializingBean {

    override fun afterPropertiesSet() {
        // register all enabled script protocols ?
    }
}