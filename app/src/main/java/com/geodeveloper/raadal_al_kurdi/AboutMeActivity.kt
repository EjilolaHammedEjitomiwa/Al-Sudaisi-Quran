package com.geodeveloper.raadal_al_kurdi

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.geodeveloper.raadal_al_kurdi.ManageNetworkState.ConnectivityReciever.Companion.isConnected
import com.geodeveloper.raadal_al_kurdi.helper.Utils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_about_me.*

class AboutMeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_me)
        about_me_supportBtn.setOnClickListener {
            if(isConnected){
                support()
            }else{
                Toasty.error(this,getString(R.string.pleaseBeConnectedToInternet),Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun support(){
        try {
            Utils.databaseRef().child(Constants.supportMe).addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(p0: DataSnapshot) {
                    if(p0.exists()){
                        val url = p0.value.toString()
                        if(url != ""){
                            val i = Intent(Intent.ACTION_VIEW)
                            i.data = Uri.parse(url)
                            startActivity(i)
                        }
                    }
                }
                override fun onCancelled(p0: DatabaseError) {

                }
            })
        }catch (e:Exception){}
    }
    override fun onBackPressed() {
        if(isConnected){
            Utils.activityOpenCount(this)
        }
        super.onBackPressed()
        Animatoo.animateSwipeRight(this)
    }
}
