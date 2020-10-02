package com.geodeveloper.raadal_al_kurdi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.geodeveloper.raadal_al_kurdi.Adapter.VideoRecitationAdapter
import com.geodeveloper.raadal_al_kurdi.ManageNetworkState.ConnectivityReciever.Companion.isConnected
import com.geodeveloper.raadal_al_kurdi.Model.VideoRecitatioModel
import com.geodeveloper.raadal_al_kurdi.helper.Utils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_video_recitation.*
import java.lang.IllegalStateException

class VideoRecitationActivity : AppCompatActivity() {
    private var itemList = ArrayList<VideoRecitatioModel>()
    private var adapter: VideoRecitationAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_recitation)

        adapter = VideoRecitationAdapter(this, itemList)
        video_recitation_recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = true
        video_recitation_recyclerView.layoutManager = layoutManager
        video_recitation_recyclerView.adapter = adapter

        if(isConnected){
            getVideoRecitations()
        }else{
            Toasty.info(this,getString(R.string.pleaseBeConnectedToInternet),Toasty.LENGTH_LONG).show()
        }
        videos_activity_backIcon.setOnClickListener {
            if(isConnected){
                Utils.activityOpenCount(this)
            }
            super.onBackPressed()
            Animatoo.animateSwipeRight(this)
        }
    }

    private fun getVideoRecitations() {
    Utils.databaseRef().child(Constants.videoRecitation).child(Constants.fromYoutube).addListenerForSingleValueEvent(object :ValueEventListener{
           override fun onDataChange(p0: DataSnapshot) {
               if(p0.exists()){
                   itemList.clear()
                   for (snapshot in p0.children){
                       val data = snapshot.getValue(VideoRecitatioModel::class.java)
                       itemList.add(data!!)
                   }
                 adapter!!.notifyDataSetChanged()
                   try {
                       video_list_loader.visibility = View.GONE
                   }catch (e:IllegalStateException){}
               }
           }
           override fun onCancelled(p0: DatabaseError) {
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