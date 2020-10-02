package com.geodeveloper.theholyquran.models.englishquran

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class SurahModel(val number: Int? = null,
                 val name: String? = null,
                 val englishName: String? = null,
                 val englishNameTranslation: String? = null,
                 val revelationType: String? = null,
                 @SerializedName("ayahs") val ayah: ArrayList<AyahModel>? = null) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()
           ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(number)
        parcel.writeString(name)
        parcel.writeString(englishName)
        parcel.writeString(englishNameTranslation)
        parcel.writeString(revelationType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SurahModel> {
        override fun createFromParcel(parcel: Parcel): SurahModel {
            return SurahModel(parcel)
        }

        override fun newArray(size: Int): Array<SurahModel?> {
            return arrayOfNulls(size)
        }
    }
}