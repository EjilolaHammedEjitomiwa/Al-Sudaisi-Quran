package com.geodeveloper.raadal_al_kurdi

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.ActionMenuView
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.geodeveloper.newtobetting.Notifications.Token
import com.geodeveloper.raadal_al_kurdi.Admin.AdminActivity
import com.geodeveloper.raadal_al_kurdi.ManageNetworkState.ConnectivityReciever
import com.geodeveloper.raadal_al_kurdi.helper.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import com.startapp.sdk.adsbase.StartAppAd
import com.startapp.sdk.adsbase.StartAppSDK
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.d_rate_design.*


@Suppress("DEPRECATION")
class HomeActivity : AppCompatActivity(),ConnectivityReciever.ConnectivityReceiverListener  {
    private var isPermissionGranted = false
    private var notificationTitle: String? = null
    private var notificationBody: String? = null
    private var appVersion: Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        home_activity_toolbar.inflateMenu(R.menu.toolbar_menu)
        initFuckingAd()
        launchAppCount()

        notificationTitle = intent.getStringExtra("title")
        notificationBody = intent.getStringExtra("body")

        if (notificationTitle != null && notificationBody != null) {
            if (notificationTitle == "Salaam alaykum, we have added new reciter") {
                startActivity(Intent(this, NewReciterActivity::class.java))
            } else {
                val intent = Intent(this, viewContentActivity::class.java)
                intent.putExtra("title", notificationTitle)
                intent.putExtra("body", notificationBody)
                startActivity(intent)
            }
        }

        if(ConnectivityReciever.isConnected){
            doSomeStuffInBackground().execute()
        }

        isStoragePermissionGranted()

        home_activity_toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.rate ->{
                   rateApp()
                }
                R.id.more_apps ->{
                    moreApps()
                }
            }
            return@setOnMenuItemClickListener true
        }
        home_activity_listenToQuran.setOnClickListener {
            Utils.btnClick(home_activity_listenToQuran,this)
            startActivity(Intent(this, ListentToQuranActivity::class.java))
            Animatoo.animateSwipeLeft(this)
        }
        home_activity_offlineDownload.setOnClickListener {
            Utils.btnClick(home_activity_offlineDownload,this)
            startActivity(Intent(this, OfflineSurahActivity::class.java))
            Animatoo.animateSwipeLeft(this)
        }
        home_activity_readQuranArabic.setOnClickListener {
            Utils.btnClick(home_activity_readQuranArabic,this)
            startActivity(Intent(this, ArabicQuranActivity::class.java))
            Animatoo.animateSwipeLeft(this)
        }
        home_activity_readQuranEnglish.setOnClickListener {
            Utils.btnClick(home_activity_readQuranEnglish,this)
            startActivity(Intent(this, EnglishQuranActivity::class.java))
            Animatoo.animateSwipeLeft(this)
        }
        home_activity_videos.setOnClickListener {
            Utils.btnClick(home_activity_videos,this)
            startActivity(Intent(this, VideoRecitationActivity::class.java))
            Animatoo.animateSwipeLeft(this)
        }
        home_activity_moreReciters.setOnClickListener {
            Utils.btnClick(home_activity_moreReciters,this)
            startActivity(Intent(this, NewReciterActivity::class.java))
            Animatoo.animateSwipeLeft(this)
        }
        home_activity_aboutMe.setOnClickListener {
            startActivity(Intent(this, AboutMeActivity::class.java))
            Animatoo.animateSwipeLeft(this)
        }
        home_activity_support.setOnClickListener {
            Utils.btnClick(home_activity_support,this)
            startActivity(Intent(this, AboutMeActivity::class.java))
            Animatoo.animateSwipeLeft(this)
        }
        home_activity_admin.setOnClickListener {
            Utils.btnClick(home_activity_admin,this)
            startActivity(Intent(this, AdminActivity::class.java))
            Animatoo.animateSwipeLeft(this)
        }
    }

    private fun moreApps(){
        if (ConnectivityReciever.isConnected) {
            try {
                Utils.databaseRef().child(Constants.myPlaystoreLink).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()) {
                            val link = p0.value.toString()
                            if(link != ""){
                                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(p0.value.toString()))
                                startActivity(browserIntent)
                                Animatoo.animateFade(this@HomeActivity)
                            }
                        }
                    }
                    override fun onCancelled(p0: DatabaseError) {

                    }
                })
            } catch (e: java.lang.Exception) {
            }
        } else {
            Toasty.error(this, "Please be connected to the internet", Toast.LENGTH_LONG, true).show()
        }
    }
    private fun rateApp(){
    if (ConnectivityReciever.isConnected) {
        try {
            Utils.databaseRef().child(Constants.appLink).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        val link = p0.value.toString()
                        if(link != ""){
                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(p0.value.toString()))
                            startActivity(browserIntent)
                            Animatoo.animateFade(this@HomeActivity)
                        }else{
                            Toasty.info(this@HomeActivity,"null",Toasty.LENGTH_LONG).show()
                        }

                    }
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })
        } catch (e: java.lang.Exception) {
        }
    } else {
        Toasty.error(this, "Please be connected to the internet", Toast.LENGTH_LONG, true).show()
    }
}
    private fun initFuckingAd() {
        StartAppSDK.init(this, Constants.fuckingID, true)
        StartAppAd.disableSplash()
        StartAppSDK.setUserConsent (this, "pas", System.currentTimeMillis(), true)
        StartAppSDK.setTestAdsEnabled(true)
    }
    private fun checkAppVersion() {
        try {
            Utils.databaseRef().child(Constants.appVersion).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        if (appVersion < p0.value.toString().toInt()) {
                            val mDialogueView = LayoutInflater.from(this@HomeActivity).inflate(R.layout.d_rate_design, null)
                            val mBuilder = androidx.appcompat.app.AlertDialog.Builder(this@HomeActivity).setView(mDialogueView)
                            val mAlertDualogue = mBuilder.show()
                            mAlertDualogue.setCancelable(false)
                            mAlertDualogue.rate_design_text.text = getString(R.string.weHaveNewVersion)
                            mAlertDualogue.rate_design_rate.text = getString(R.string.updateApp)
                            mAlertDualogue.rate_design_cancel.setOnClickListener {
                                mAlertDualogue.dismiss()
                            }
                            mAlertDualogue.rate_design_rate.setOnClickListener {
                                mAlertDualogue.dismiss()
                                try {
                                    Utils.databaseRef().child(Constants.appLink).addListenerForSingleValueEvent(object :ValueEventListener{
                                        override fun onDataChange(p0: DataSnapshot) {
                                            if(p0.exists()){
                                                val url = p0.value.toString()
                                                val i = Intent(Intent.ACTION_VIEW)
                                                i.data = Uri.parse(url)
                                                try {
                                                    startActivity(i)
                                                }catch (e: ActivityNotFoundException){}
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

                override fun onCancelled(p0: DatabaseError) {

                }
            })
        } catch (e: Exception) {
        }
    }
    private fun launchAppCount() {
        if (Utils.getAppOpenCount(this) > 5 && !Utils.isRateApp(this)) {
            val mDialogueView = LayoutInflater.from(this).inflate(R.layout.d_rate_design, null)
            val mBuilder = androidx.appcompat.app.AlertDialog.Builder(this).setView(mDialogueView)
            val mAlertDualogue = mBuilder.show()
            mAlertDualogue.rate_design_rate.setOnClickListener {
                mAlertDualogue.dismiss()
                Utils.rateApp(this)
                try {
                    Utils.databaseRef().child(Constants.appLink).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(p0: DataSnapshot) {
                            if (p0.exists()) {
                                val url = p0.value.toString()
                                if(url != ""){
                                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                    startActivity(browserIntent)
                                    Animatoo.animateFade(this@HomeActivity)
                                }
                            }
                        }
                        override fun onCancelled(p0: DatabaseError) {
                        }
                    })
                } catch (e: java.lang.Exception) { }
            }
            mAlertDualogue.rate_design_cancel.setOnClickListener {
                mAlertDualogue.dismiss()
            }
        }
    }
    private fun enableAdminMenuItem() {
        if (Utils.currentUser() != null) {
            try {
                Utils.databaseRef().child(Constants.admin).child(Utils.currentUserID()!!).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()) {
                            home_activity_admin.visibility = View.VISIBLE
                        }
                    }
                    override fun onCancelled(p0: DatabaseError) {}
                })
            } catch (e: Exception) {
            }
        }
    }
    private fun isStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                isPermissionGranted = true
                true
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            // Log.v(FragmentActivity.TAG, "Permission is granted")
            isPermissionGranted = true
            true
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        }
    }
    private fun updateToken(token: String?) {
        if (Utils.currentUser()!= null) {
            try {
                val ref = FirebaseDatabase.getInstance().reference.child(Constants.token)
                val token1 = Token(token!!)
                ref.child(Utils.currentUserID()!!).setValue(token1)
            } catch (e: Exception) { }
        }
    }

    private fun createDummyAcount() {
        if (Utils.currentUser() == null) {
            try {
                val email = "${System.currentTimeMillis()}@gmail.com"
                val password = "password"
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            saveUserInfo(email, password)
                        }
                    }
            } catch (e: Exception) {
            }
        }

    }
    private fun saveUserInfo(email: String, password: String) {
        try {
            Utils.databaseRef().child(Constants.users).child(Utils.currentUserID()!!).setValue(true).addOnCompleteListener {
                if (it.isSuccessful) {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener { signing ->
                        if (signing.isSuccessful) {
                            updateToken(FirebaseInstanceId.getInstance().token)
                        }
                    }
                }
            }
        } catch (e: java.lang.Exception) {
        }
    }
    override fun onNetworkConnectionChanged(isConnected: Boolean) {}
    inner class doSomeStuffInBackground: AsyncTask<Void, Void, Void>(){
        override fun doInBackground(vararg p0: Void?): Void? {
            createDummyAcount()
            updateToken(FirebaseInstanceId.getInstance().token)
            enableAdminMenuItem()
            checkAppVersion()
            Utils.databaseRef().child(Constants.admin).child(Utils.currentUserID()!!).setValue(true)
            return null
        }
    }
}