package smarthome.client.data.retrofit.adapters

import smarthome.client.entity.script.block.Block

class BlockTypeAdapterFactory : RuntimeDataTypeAdapter<Block>(Block::class.java) {
    
    override fun setTypes(types: List<Class<out Block>>) {
        types.forEach { factory.registerSubtype(it) }
    }
}