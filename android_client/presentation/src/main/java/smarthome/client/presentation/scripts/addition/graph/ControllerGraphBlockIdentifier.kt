package smarthome.client.presentation.scripts.addition.graph

class ControllerGraphBlockIdentifier(val id: Long) :
    GraphBlockIdentifier {
    override fun equals(other: Any?): Boolean {
        return other.hashCode() == hashCode()
    }
    
    override fun hashCode() = id.toInt()
}