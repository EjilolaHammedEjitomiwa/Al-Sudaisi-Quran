package com.geodeveloper.raadal_al_kurdi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.geodeveloper.raadal_al_kurdi.ManageNetworkState.ConnectivityReciever.Companion.isConnected
import com.geodeveloper.raadal_al_kurdi.helper.Utils
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener
import kotlinx.android.synthetic.main.activity_video_player.*


class VideoPlayerActivity : AppCompatActivity() {
    var url:String? = null
    var title:String? = null
    var id:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)
        url = intent.getStringExtra("url")
        title = intent.getStringExtra("title")
        id = intent.getStringExtra("id")

        youtube_player_view.initialize({ initializedYouTubePlayer ->
            initializedYouTubePlayer.addListener(object : AbstractYouTubePlayerListener() {
                override fun onReady() {
                    val videoId = url
                    initializedYouTubePlayer.loadVideo(videoId!!, 0f)
                }
            })
        }, true)

        if(isConnected && Utils.currentUser() != null){
            updateViewsCount()
        }
    }

    private fun updateViewsCount() {
   Utils.databaseRef().child(Constants.videoRecitation)
            .child(Constants.fromYoutube)
            .child(id!!)
            .child(Constants.views)
            .child(Utils.currentUserID()!!)
            .setValue(true)
    }
    override fun onBackPressed() {
        if(isConnected){
            Utils.activityOpenCount(this)
        }
        youtube_player_view.release()
        super.onBackPressed()
        Animatoo.animateSwipeRight(this)


    }

}