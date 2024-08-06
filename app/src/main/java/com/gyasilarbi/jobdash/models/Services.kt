package com.gyasilarbi.jobdash.models

import android.os.Parcel
import android.os.Parcelable

data class Service(
    val serviceId: String? = null,
    val title: String? = null,
    val category: String? = null,
    val description: String? = null,
    val amount: String? = null,
    val location: String? = null,
    val duration: String? = null,
    val imageUrl: String? = null,
    val userId: String? = null,
    val status: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(serviceId)
        parcel.writeString(title)
        parcel.writeString(category)
        parcel.writeString(description)
        parcel.writeString(amount)
        parcel.writeString(location)
        parcel.writeString(duration)
        parcel.writeString(imageUrl)
        parcel.writeString(userId)
        parcel.writeInt(status)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<Service> {
        override fun createFromParcel(parcel: Parcel) = Service(parcel)
        override fun newArray(size: Int) = arrayOfNulls<Service?>(size)
    }
}
