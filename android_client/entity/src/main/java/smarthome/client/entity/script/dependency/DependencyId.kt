package smarthome.client.entity.script.dependency

import android.os.Parcelable
import smarthome.client.entity.Id

abstract class DependencyId(id: Long? = null) : Parcelable, Id(id)
