package smarthome.client.presentation.util.drag

interface DraggableHostHolder {
    fun register(host: DraggableHost)
    fun unregister(host: DraggableHost)
    fun get(): List<DraggableHost>
}