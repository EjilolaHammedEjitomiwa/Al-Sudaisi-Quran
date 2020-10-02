package com.geodeveloper.raadal_al_kurdi.Admin

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.geodeveloper.raadal_al_kurdi.Constants
import com.geodeveloper.raadal_al_kurdi.R
import com.geodeveloper.raadal_al_kurdi.helper.Utils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_admin.*
import kotlinx.android.synthetic.main.add_video_recitation_design.*

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        admin_addNewReciter.setOnClickListener {
            startActivity(Intent(this,AddNewReciter::class.java))
        }
        admin_sendNotificationToUsers.setOnClickListener {
            startActivity(Intent(this,SendNotificationActivity::class.java))
        }
        admin_addVideo.setOnClickListener {
            val mDialogueView = LayoutInflater.from(this).inflate(R.layout.add_video_recitation_design, null)
            val mBuilder = AlertDialog.Builder(this).setView(mDialogueView)
            val mAlertDualogue = mBuilder.show()
            mAlertDualogue.add_video_recitation_addBtn.setOnClickListener {
                val title = mAlertDualogue.add_video_recitation_title.text.toString()
                val link = mAlertDualogue.add_video_recitation_link.text.toString()
                val thumbnail = mAlertDualogue.add_video_recitation_thumbnail.text.toString()
                if(title.isEmpty() || link.isEmpty() || thumbnail.isEmpty()){
                    Toasty.info(this,"empty fields",Toasty.LENGTH_LONG).show()
                }else{
                    val map = HashMap<String,String>()
                    val id = Utils.databaseRef().push().key.toString()
                    map["id"] = id
                    map["title"] = title
                    map["link"] = link
                    map["thumbnail"] = thumbnail
                    map["date"] = System.currentTimeMillis().toString()
                    Utils.databaseRef().child(Constants.videoRecitation).child(Constants.fromYoutube).child(id).setValue(map).addOnCompleteListener {
                        if(it.isSuccessful){
                            mAlertDualogue.dismiss()
                        }
                    }
                }
            }
        }

        Utils.databaseRef().child(Constants.users).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                admin_totalUser.text = "Total user = ${p0.childrenCount}"
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}
