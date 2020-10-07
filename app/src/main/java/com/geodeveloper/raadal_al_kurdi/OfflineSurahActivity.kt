package com.geodeveloper.raadal_al_kurdi

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.geodeveloper.raadal_al_kurdi.Adapter.SurahListAdapter
import com.geodeveloper.raadal_al_kurdi.ManageNetworkState.ConnectivityReciever.Companion.isConnected
import com.geodeveloper.raadal_al_kurdi.Model.SurahListModel
import com.geodeveloper.raadal_al_kurdi.helper.Utils

import kotlinx.android.synthetic.main.activity_offline_surah.*
import java.io.File

class OfflineSurahActivity : AppCompatActivity() {
    private var imageList = ArrayList<SurahListModel>()
    private var adapter: SurahListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offline_surah)

        adapter = SurahListAdapter(this, imageList,true)
        offline_activity_recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        offline_activity_recyclerView.layoutManager = layoutManager
        offline_activity_recyclerView.adapter = adapter

        getOfflineDownloadItems()

        offline_download_back.setOnClickListener {
            if(isConnected){
                Utils.activityOpenCount(this)
            }
            finish()
            Animatoo.animateSwipeRight(this)
        }
    }
    override fun onBackPressed() {
        if(isConnected){
            Utils.activityOpenCount(this)
        }
        super.onBackPressed()
        Animatoo.animateSwipeRight(this)
    }
    private fun getOfflineDownloadItems() {
        val folder_main = Constants.mainStorageFolder
        var surah: SurahListModel
        val downloadFolder = File(Environment.getExternalStorageDirectory(), folder_main)
        if (downloadFolder.exists()) {
            imageList.clear()
            val files = downloadFolder.listFiles()
            for (element in files!!) {
                val file: File = element
                surah =  SurahListModel()
                surah.title = file.name
                surah.uri = (Uri.fromFile(file))
                imageList.add(surah)
            }
            adapter!!.notifyDataSetChanged()
            if(imageList.isEmpty()){
                download_empty.visibility = View.VISIBLE
            }
        }
    }
}
