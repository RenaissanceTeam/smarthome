package smarthome.client.data.retrofit.adapters

import smarthome.client.data.api.typeadapter.BlockTypeAdapter
import smarthome.client.entity.script.block.Block

class BlockTypeAdapterFactoryImpl : RuntimeDataTypeAdapter<Block>(Block::class.java), BlockTypeAdapter

