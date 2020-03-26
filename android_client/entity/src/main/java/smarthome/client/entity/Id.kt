package smarthome.client.entity

abstract class Id(open val id: Long? = null) {
    
    override fun hashCode(): Int {
        return id?.toInt() ?: super.hashCode()
    }
    
    override fun equals(other: Any?): Boolean {
        return (other as? Id)?.let { otherId ->
            this.id?.let { this.id == otherId.id }
        } ?: super.equals(other)
    }
}