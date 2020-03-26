package smarthome.client.entity.script.dependency

import android.os.Parcel
import android.os.Parcelable

class SimpleDependencyId(id: Long? = null) : DependencyId(id) {
    constructor(parcel: Parcel) : this(parcel.readValue(Long::class.java.classLoader) as? Long) {
    }
    
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
    }
    
    override fun describeContents(): Int {
        return 0
    }
    
    companion object CREATOR : Parcelable.Creator<SimpleDependencyId> {
        override fun createFromParcel(parcel: Parcel): SimpleDependencyId {
            return SimpleDependencyId(parcel)
        }
        
        override fun newArray(size: Int): Array<SimpleDependencyId?> {
            return arrayOfNulls(size)
        }
    }
    
}