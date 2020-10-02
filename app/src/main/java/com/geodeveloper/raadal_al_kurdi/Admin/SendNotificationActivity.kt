package com.geodeveloper.raadal_al_kurdi.Admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.widget.Toast
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.geodeveloper.newtobetting.Notifications.*
import com.geodeveloper.raadal_al_kurdi.APIservice
import com.geodeveloper.raadal_al_kurdi.Constants
import com.geodeveloper.raadal_al_kurdi.Model.UsersModel
import com.geodeveloper.raadal_al_kurdi.R
import com.geodeveloper.raadal_al_kurdi.helper.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kaopiz.kprogresshud.KProgressHUD
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_send_notification.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SendNotificationActivity : AppCompatActivity() {
    private var progress: KProgressHUD? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_notification)
        progress = KProgressHUD(this)

        send_notification_send.setOnClickListener {
            val title = send_notification_title.text.toString()
            val content = send_notification_content.text.toString()

            when{
                TextUtils.isEmpty(title) -> Toasty.info(this,"Title cannot be empty",Toast.LENGTH_LONG,true).show()
                TextUtils.isEmpty(content) -> Toasty.info(this,"content cannot be empty",Toast.LENGTH_LONG,true).show()
                else ->{
                    progress = KProgressHUD.create(this)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setLabel("Please wait")
                        .setCancellable(false)
                        .setAnimationSpeed(2)
                        .setDimAmount(0.5f)
                        .show()
                    try {
                        Utils.databaseRef().child(Constants.users).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(p0: DataSnapshot) {
                                if (p0.exists()) {
//                                    Utils.sendNotification(Utils.currentUserID()!!,title,content)
                                    for (snapshot in p0.children){
                                        Utils.sendNotification(snapshot.key.toString(),title,content)
                                    }
                                    Toasty.info(this@SendNotificationActivity,"sent successfully",Toasty.LENGTH_LONG).show()
                                    Handler().postDelayed(object : Runnable {
                                        override fun run() {
                                            if(progress!!.isShowing){
                                                progress!!.dismiss()
                                                finish()
                                                Animatoo.animateDiagonal(this@SendNotificationActivity)
                                            }
                                        }
                                    }, 7000)

                                }
                            }
                            override fun onCancelled(p0: DatabaseError) {
                            }
                        })
                    }catch (e:Exception){}
                }

            }
        }
    }


}
