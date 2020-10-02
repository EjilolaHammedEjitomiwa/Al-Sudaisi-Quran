package com.geodeveloper.raadal_al_kurdi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.geodeveloper.raadal_al_kurdi.Adapter.ArabicQuranAdapter
import com.geodeveloper.raadal_al_kurdi.ManageNetworkState.ConnectivityReciever.Companion.isConnected
import com.geodeveloper.raadal_al_kurdi.helper.Utils
import com.geodeveloper.theholyquran.models.englishquran.QuranModel
import com.geodeveloper.theholyquran.service.QuranService
import com.geodeveloper.theholyquran.service.ServiceBuilder
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_arabic_quran.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.IllegalStateException

class ArabicQuranActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arabic_quran)
        if(isConnected){
            getQuranList()
        }else{
            Toasty.error(this,getString(R.string.pleaseBeConnectedToInternet),Toast.LENGTH_LONG).show()
        }

        arabic_quran_backIcon.setOnClickListener {
            if(isConnected){
                Utils.activityOpenCount(this)
            }
            finish()
            Animatoo.animateSwipeRight(this)

        }
    }

    private fun getQuranList() {
        try {
            arabic_quran_activity_progress.visibility = View.VISIBLE
        } catch (e: IllegalStateException) { }
        val quranService = ServiceBuilder.buildService(QuranService::class.java)
        val requestCall = quranService.getArabicQuran()
        requestCall.enqueue(object : Callback<QuranModel> {
            override fun onResponse(call: Call<QuranModel>, response: Response<QuranModel>) {
                if (response.isSuccessful) {
                    val quranLists = response.body()!!
                    arabic_quran_activity_recyclerView.adapter = ArabicQuranAdapter(this@ArabicQuranActivity,quranLists)
                }
                try {
                    arabic_quran_activity_progress.visibility = View.GONE
                } catch (e: IllegalStateException) { }
            }
            override fun onFailure(call: Call<QuranModel>, t: Throwable) {
                Toasty.error(this@ArabicQuranActivity,"error occur",Toast.LENGTH_LONG).show()
                try {
                    arabic_quran_activity_progress.visibility = View.GONE
                } catch (e: IllegalStateException) { }
            }
        })
    }
    override fun onBackPressed() {
        if(isConnected){
            Utils.activityOpenCount(this)
        }
        super.onBackPressed()
        Animatoo.animateSwipeRight(this)
    }
}