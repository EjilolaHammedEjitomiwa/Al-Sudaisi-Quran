package com.geodeveloper.theholyquran.service

import com.geodeveloper.theholyquran.models.englishquran.QuranModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface QuranService {
    //get english quran
    @GET("quran/en.sahih")
    fun  getEnglishQuran(): Call<QuranModel>
    //get arabic quran
    @GET("quran/ar.abdullahbasfar")
    fun  getArabicQuran(): Call<QuranModel>

}