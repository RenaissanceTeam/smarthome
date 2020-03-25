package smarthome.client.data.retrofit.adapters

import smarthome.client.data.api.typeadapter.ConditionDataTypeAdapter
import smarthome.client.entity.script.dependency.condition.ConditionData

class ConditionTypeAdapterFactoryImpl : RuntimeDataTypeAdapter<ConditionData>(ConditionData::class.java), ConditionDataTypeAdapter
