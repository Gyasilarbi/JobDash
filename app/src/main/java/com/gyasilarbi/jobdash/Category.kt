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

val getCategory = mutableListOf(
    Category("Barber","GyasiLarbi", R.drawable.barber, "A barber is a professional who provides grooming services, primarily focused on cutting, trimming, styling, and shaving hair for male clients. Barbers may also offer additional services such as facial treatments, hair coloring, and beard maintenance. The role requires a combination of technical skill, creativity, and excellent customer service.", "GH₵20.00"),
    Category("Gardener","GyasiLarbi", R.drawable.garderner, "A gardener is responsible for the cultivation and maintenance of various types of plants and landscapes. This includes tasks such as planting, pruning, weeding, and general upkeep of gardens, lawns, and other outdoor spaces. Gardeners may work in residential, commercial, or public settings, ensuring that these areas remain attractive and healthy.", "GH₵23.00"),
    Category("Plumber","GyasiLarbi", R.drawable.plumber, "A plumber installs, repairs, and maintains plumbing systems and fixtures in residential, commercial, and industrial settings. This includes systems for water supply, drainage, heating, and sanitation. Plumbers ensure that these systems operate efficiently and comply with local codes and safety standards.", "GH₵20.16"),
    Category("Barber","GyasiLarbi", R.drawable.barber, "A barber is a professional who provides grooming services, primarily focused on cutting, trimming, styling, and shaving hair for male clients. Barbers may also offer additional services such as facial treatments, hair coloring, and beard maintenance. The role requires a combination of technical skill, creativity, and excellent customer service.", "GH₵20.00"),
    Category("Gardener","GyasiLarbi", R.drawable.garderner, "A gardener is responsible for the cultivation and maintenance of various types of plants and landscapes. This includes tasks such as planting, pruning, weeding, and general upkeep of gardens, lawns, and other outdoor spaces. Gardeners may work in residential, commercial, or public settings, ensuring that these areas remain attractive and healthy.", "GH₵23.00"),
    Category("Plumber","GyasiLarbi", R.drawable.plumber, "A plumber installs, repairs, and maintains plumbing systems and fixtures in residential, commercial, and industrial settings. This includes systems for water supply, drainage, heating, and sanitation. Plumbers ensure that these systems operate efficiently and comply with local codes and safety standards.", "GH₵20.16"),
    Category("Barber","GyasiLarbi", R.drawable.barber, "A barber is a professional who provides grooming services, primarily focused on cutting, trimming, styling, and shaving hair for male clients. Barbers may also offer additional services such as facial treatments, hair coloring, and beard maintenance. The role requires a combination of technical skill, creativity, and excellent customer service.", "GH₵20.00"),
    Category("Gardener","GyasiLarbi", R.drawable.garderner, "A gardener is responsible for the cultivation and maintenance of various types of plants and landscapes. This includes tasks such as planting, pruning, weeding, and general upkeep of gardens, lawns, and other outdoor spaces. Gardeners may work in residential, commercial, or public settings, ensuring that these areas remain attractive and healthy.", "GH₵23.00"),
    Category("Plumber","GyasiLarbi", R.drawable.plumber, "A plumber installs, repairs, and maintains plumbing systems and fixtures in residential, commercial, and industrial settings. This includes systems for water supply, drainage, heating, and sanitation. Plumbers ensure that these systems operate efficiently and comply with local codes and safety standards.", "GH₵20.16"),
    Category("Barber","GyasiLarbi", R.drawable.barber, "A barber is a professional who provides grooming services, primarily focused on cutting, trimming, styling, and shaving hair for male clients. Barbers may also offer additional services such as facial treatments, hair coloring, and beard maintenance. The role requires a combination of technical skill, creativity, and excellent customer service.", "GH₵20.00"),
    Category("Gardener","GyasiLarbi", R.drawable.garderner, "A gardener is responsible for the cultivation and maintenance of various types of plants and landscapes. This includes tasks such as planting, pruning, weeding, and general upkeep of gardens, lawns, and other outdoor spaces. Gardeners may work in residential, commercial, or public settings, ensuring that these areas remain attractive and healthy.", "GH₵23.00"),
    Category("Plumber","GyasiLarbi", R.drawable.plumber, "3.9", "GH₵20.16"),
    Category("Barber","GyasiLarbi", R.drawable.barber, "A barber is a professional who provides grooming services, primarily focused on cutting, trimming, styling, and shaving hair for male clients. Barbers may also offer additional services such as facial treatments, hair coloring, and beard maintenance. The role requires a combination of technical skill, creativity, and excellent customer service.", "GH₵20.00"),
    Category("Gardener","GyasiLarbi", R.drawable.garderner, "A gardener is responsible for the cultivation and maintenance of various types of plants and landscapes. This includes tasks such as planting, pruning, weeding, and general upkeep of gardens, lawns, and other outdoor spaces. Gardeners may work in residential, commercial, or public settings, ensuring that these areas remain attractive and healthy.", "GH₵23.00"),
    Category("Plumber","GyasiLarbi", R.drawable.plumber, "3.9", "GH₵20.16"),
    Category("Barber","GyasiLarbi", R.drawable.barber, "A barber is a professional who provides grooming services, primarily focused on cutting, trimming, styling, and shaving hair for male clients. Barbers may also offer additional services such as facial treatments, hair coloring, and beard maintenance. The role requires a combination of technical skill, creativity, and excellent customer service.", "GH₵20.00"),
    Category("Gardener","GyasiLarbi", R.drawable.garderner, "A gardener is responsible for the cultivation and maintenance of various types of plants and landscapes. This includes tasks such as planting, pruning, weeding, and general upkeep of gardens, lawns, and other outdoor spaces. Gardeners may work in residential, commercial, or public settings, ensuring that these areas remain attractive and healthy.", "GH₵23.00"),
    Category("Plumber","GyasiLarbi", R.drawable.plumber, "3.9", "GH₵20.16"),
    Category("Barber","GyasiLarbi", R.drawable.barber, "A barber is a professional who provides grooming services, primarily focused on cutting, trimming, styling, and shaving hair for male clients. Barbers may also offer additional services such as facial treatments, hair coloring, and beard maintenance. The role requires a combination of technical skill, creativity, and excellent customer service.", "GH₵20.00"),
    Category("Gardener","GyasiLarbi", R.drawable.garderner, "A gardener is responsible for the cultivation and maintenance of various types of plants and landscapes. This includes tasks such as planting, pruning, weeding, and general upkeep of gardens, lawns, and other outdoor spaces. Gardeners may work in residential, commercial, or public settings, ensuring that these areas remain attractive and healthy.", "GH₵23.00"),
    Category("Plumber","GyasiLarbi", R.drawable.plumber, "3.9", "GH₵20.16"),
    Category("Barber","GyasiLarbi", R.drawable.barber, "A barber is a professional who provides grooming services, primarily focused on cutting, trimming, styling, and shaving hair for male clients. Barbers may also offer additional services such as facial treatments, hair coloring, and beard maintenance. The role requires a combination of technical skill, creativity, and excellent customer service.", "GH₵20.00"),
    Category("Gardener","GyasiLarbi", R.drawable.garderner, "A gardener is responsible for the cultivation and maintenance of various types of plants and landscapes. This includes tasks such as planting, pruning, weeding, and general upkeep of gardens, lawns, and other outdoor spaces. Gardeners may work in residential, commercial, or public settings, ensuring that these areas remain attractive and healthy.", "GH₵23.00"),
    Category("Plumber","GyasiLarbi", R.drawable.plumber, "3.9", "GH₵20.16"),
    )