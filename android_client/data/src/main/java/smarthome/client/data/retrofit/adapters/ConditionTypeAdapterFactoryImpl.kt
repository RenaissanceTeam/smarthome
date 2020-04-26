package smarthome.client.data.retrofit.adapters

import smarthome.client.data.api.typeadapter.ConditionTypeAdapter
import smarthome.client.entity.script.dependency.condition.Condition

class ConditionTypeAdapterFactoryImpl : RuntimeDataTypeAdapter<Condition>(Condition::class.java), ConditionTypeAdapter
