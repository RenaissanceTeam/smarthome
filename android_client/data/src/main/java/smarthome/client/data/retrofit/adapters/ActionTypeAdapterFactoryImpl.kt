package smarthome.client.data.retrofit.adapters

import smarthome.client.data.api.typeadapter.ActionDataTypeAdapter
import smarthome.client.entity.script.dependency.action.Action

class ActionTypeAdapterFactoryImpl : RuntimeDataTypeAdapter<Action>(Action::class.java), ActionDataTypeAdapter