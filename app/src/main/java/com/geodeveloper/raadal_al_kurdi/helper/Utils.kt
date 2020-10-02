package com.geodeveloper.raadal_al_kurdi.helper

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import androidx.core.content.edit
import com.geodeveloper.newtobetting.Notifications.*
import com.geodeveloper.raadal_al_kurdi.APIservice
import com.geodeveloper.raadal_al_kurdi.Constants
import com.geodeveloper.raadal_al_kurdi.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.startapp.sdk.adsbase.StartAppAd
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
object Utils {
    fun databaseRef(): DatabaseReference {
        return FirebaseDatabase.getInstance().reference
    }
    fun currentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }
    fun currentUserID(): String? {
        return FirebaseAuth.getInstance().currentUser!!.uid
    }
    fun getAppOpenCount(context: Context): Int {
        val myPref = context.getSharedPreferences(Constants.pref, Context.MODE_PRIVATE)
        var count = myPref.getInt(Constants.appOpenCount, 0)
        count++
        myPref.edit().apply {
            putInt(Constants.appOpenCount, count)
            apply()
            return count
        }
    }
    fun isRateApp(context: Context):Boolean{
        val myPref = context.getSharedPreferences(Constants.pref, Context.MODE_PRIVATE)
        return myPref.getBoolean(Constants.rated, false)
    }
    fun rateApp(context: Context){
       context.getSharedPreferences(Constants.pref, Context.MODE_PRIVATE).edit {
            putBoolean(Constants.rated,true)
            apply()
        }
    }
    fun activityOpenCount(context: Activity) {
        val myPref = context.getSharedPreferences(Constants.activityOpenPref, Context.MODE_PRIVATE)
        var activityOpen = myPref.getInt(Constants.activityOpen, 0)
        activityOpen++
        if (activityOpen >= Constants.appOpenCountToShowIntersitial) {
            StartAppAd.showAd(context)
            myPref.edit().apply {
                putInt(Constants.activityOpen, 0)
                apply()
            }
        } else {
            myPref.edit().apply {
                putInt(Constants.activityOpen, activityOpen)
                apply()
            }
        }

    }
    fun showInterstitialAds(context: Context) {
        StartAppAd.showAd(context)
    }
    fun sendNotification(recieverId: String, title: String, message: String) {
        val apiServie = NotificationClient.Client.getClient("https://fcm.googleapis.com/")!!.create(
            APIservice::class.java)
        try {
            databaseRef().child(Constants.token).orderByKey().equalTo(recieverId).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    for (snapshot in p0.children) {
                        val token = snapshot.getValue(Token::class.java)
                        val data = NotificationData(currentUserID()!!, R.mipmap.ic_launcher, message, title, recieverId)
                        val sender = Sender(data, token!!.token)
                        apiServie.sendNotification(sender).enqueue(object : Callback<MyResponse> {
                            override fun onResponse(call: Call<MyResponse>, response: Response<MyResponse>) {
                                if (response.code() == 200) {
                                    if (response.body()!!.success != 1) {
                                        //  Toast.makeText(this@AddAboutUs, "Failed", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }

                            override fun onFailure(call: Call<MyResponse>, t: Throwable) {
                            }
                        })
                    }
                }

                override fun onCancelled(p0: DatabaseError) {
                }
            })
        } catch (e: java.lang.Exception) {
        }
    }
    fun btnClick(view: View, activity: Activity) {
        val myAnim = AnimationUtils.loadAnimation(activity, R.anim.bounce)
        val interpolator = MyBounceInterpolator(0.2, 20.0)
        myAnim.interpolator = interpolator
        view.startAnimation(myAnim)
    }
    internal class MyBounceInterpolator(amplitude: Double, frequency: Double) : Interpolator {
        private var mAmplitude = 1.0
        private var mFrequency = 10.0
        override fun getInterpolation(time: Float): Float {
            return (-1 * Math.pow(Math.E, -time / mAmplitude) *
                    Math.cos(mFrequency * time) + 1).toFloat()
        }

        init {
            mAmplitude = amplitude
            mFrequency = frequency
        }
    }
}