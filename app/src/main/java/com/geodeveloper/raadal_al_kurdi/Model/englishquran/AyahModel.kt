package com.geodeveloper.theholyquran.models.englishquran

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class AyahModel(
        @SerializedName("number")
        val number: Int? = null,
        @SerializedName("text")
        val text: String? = null,
        @SerializedName("numberInSurah")
        val numberInSurah: Int? = null,
        @SerializedName("juz")
        val juz: Int? = null,
        @SerializedName("manzil")
        val manzil: Int? = null,
        @SerializedName("page")
        val page: Int? = null,
        @SerializedName("ruku")
        val ruku: Int? = null,
        @SerializedName("hizbQuarter")
        val hizbQuarter: Int? = null,
        @SerializedName("audio")
        val audio: String? = null,
        @SerializedName("surah")
        val surah: SurahModel? = null

) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString()


    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(number)
        parcel.writeString(text)
        parcel.writeValue(numberInSurah)
        parcel.writeValue(juz)
        parcel.writeValue(manzil)
        parcel.writeValue(page)
        parcel.writeValue(ruku)
        parcel.writeValue(hizbQuarter)
        parcel.writeString(audio)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AyahModel> {
        override fun createFromParcel(parcel: Parcel): AyahModel {
            return AyahModel(parcel)
        }

        override fun newArray(size: Int): Array<AyahModel?> {
            return arrayOfNulls(size)
        }
    }
}