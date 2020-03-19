package smarthome.client.presentation.util.drag

class DraggableHostHolderImpl : DraggableHostHolder {
    private val hosts = mutableListOf<DraggableHost>()
    
    override fun register(host: DraggableHost) {
        hosts.add(host)
    }
    
    override fun unregister(host: DraggableHost) {
        hosts.remove(host)
    }
    
    override fun get(): List<DraggableHost> {
        return hosts
    }
}