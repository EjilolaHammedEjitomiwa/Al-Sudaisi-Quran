package com.geodeveloper.theholyquran.models.englishquran

import com.google.gson.annotations.SerializedName

class Data(@SerializedName("surahs") val surah: ArrayList<SurahModel>? = null) {
}