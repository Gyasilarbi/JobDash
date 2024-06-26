package com.gyasilarbi.jobdash

import android.os.Parcel
import android.os.Parcelable

data class Category(
    val name: String?,
    val owner: String?,
    val logoResourceId: Int,
    val description: String?,
    val price: String?,
) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(owner)
        parcel.writeInt(logoResourceId)
        parcel.writeString(description)
        parcel.writeString(price)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object  CREATOR : Parcelable.Creator<Category> {
        override fun createFromParcel(parcel: Parcel): Category {
            return Category(parcel)
        }

        override fun newArray(size: Int): Array<Category?> {
            return arrayOfNulls(size)
        }
    }
}

val getCategory = listOf(
    Category("Barber","GyasiLarbi", R.drawable.barber, "4.5", "GH₵20.00"),
    Category("Gardener","GyasiLarbi", R.drawable.garderner, "4.5", "GH₵23.00"),
    Category("Plumber","GyasiLarbi", R.drawable.plumber, "3.9", "GH₵20.16"),
    Category("Barber","GyasiLarbi", R.drawable.barber, "4.5", "GH₵20.00"),
    Category("Gardener","GyasiLarbi", R.drawable.garderner, "4.5", "GH₵23.00"),
    Category("Plumber","GyasiLarbi", R.drawable.plumber, "3.9", "GH₵20.16"),
    Category("Barber","GyasiLarbi", R.drawable.barber, "4.5", "GH₵20.00"),
    Category("Gardener","GyasiLarbi", R.drawable.garderner, "4.5", "GH₵23.00"),
    Category("Plumber","GyasiLarbi", R.drawable.plumber, "3.9", "GH₵20.16"),
    Category("Barber","GyasiLarbi", R.drawable.barber, "4.5", "GH₵20.00"),
    Category("Gardener","GyasiLarbi", R.drawable.garderner, "4.5", "GH₵23.00"),
    Category("Plumber","GyasiLarbi", R.drawable.plumber, "3.9", "GH₵20.16"),
    Category("Barber","GyasiLarbi", R.drawable.barber, "4.5", "GH₵20.00"),
    Category("Gardener","GyasiLarbi", R.drawable.garderner, "4.5", "GH₵23.00"),
    Category("Plumber","GyasiLarbi", R.drawable.plumber, "3.9", "GH₵20.16"),
    Category("Barber","GyasiLarbi", R.drawable.barber, "4.5", "GH₵20.00"),
    Category("Gardener","GyasiLarbi", R.drawable.garderner, "4.5", "GH₵23.00"),
    Category("Plumber","GyasiLarbi", R.drawable.plumber, "3.9", "GH₵20.16"),
    Category("Barber","GyasiLarbi", R.drawable.barber, "4.5", "GH₵20.00"),
    Category("Gardener","GyasiLarbi", R.drawable.garderner, "4.5", "GH₵23.00"),
    Category("Plumber","GyasiLarbi", R.drawable.plumber, "3.9", "GH₵20.16"),
    Category("Barber","GyasiLarbi", R.drawable.barber, "4.5", "GH₵20.00"),
    Category("Gardener","GyasiLarbi", R.drawable.garderner, "4.5", "GH₵23.00"),
    Category("Plumber","GyasiLarbi", R.drawable.plumber, "3.9", "GH₵20.16"),
    )