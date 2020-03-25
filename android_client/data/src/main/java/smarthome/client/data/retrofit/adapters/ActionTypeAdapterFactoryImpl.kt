package smarthome.client.data.retrofit.adapters

import smarthome.client.data.api.typeadapter.ActionDataTypeAdapter
import smarthome.client.entity.script.dependency.action.ActionData

class ActionTypeAdapterFactoryImpl : RuntimeDataTypeAdapter<ActionData>(ActionData::class.java), ActionDataTypeAdapter