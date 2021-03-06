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
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.geodeveloper.newtobetting.Notifications.Token
import com.geodeveloper.raadal_al_kurdi.Adapter.SurahListAdapter
import com.geodeveloper.raadal_al_kurdi.Admin.AdminActivity
import com.geodeveloper.raadal_al_kurdi.ManageNetworkState.ConnectivityReciever
import com.geodeveloper.raadal_al_kurdi.ManageNetworkState.ConnectivityReciever.Companion.isConnected
import com.geodeveloper.raadal_al_kurdi.Model.SurahListModel
import com.geodeveloper.raadal_al_kurdi.helper.Utils
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import com.kaopiz.kprogresshud.KProgressHUD
import com.startapp.sdk.adsbase.StartAppAd
import com.startapp.sdk.adsbase.StartAppSDK
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.d_rate_design.*

@Suppress("DEPRECATION", "CAST_NEVER_SUCCEEDS")
class ListentToQuranActivity : AppCompatActivity(){
    private var itemList = ArrayList<SurahListModel>()
    private var adapter: SurahListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = SurahListAdapter(this, itemList, false)
        main_recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        main_recyclerView.layoutManager = layoutManager
        main_recyclerView.adapter = adapter
        try {
            itemList.clear()
            itemList.add(SurahListModel("Surah 001 - Al-Fatiha (The Opening)", "http://freequranmp3.com/sudais/001-al-fatihah.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 002 - Al-Baqarah (The Cow)", "http://freequranmp3.com/sudais/002-al-baqarah.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 003 - Al-'Imran (The Family of Imran)", "http://freequranmp3.com/sudais/003-al-imran.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 004 - An-Nisa' (The Women)", "http://freequranmp3.com/sudais/004-an-nisa.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 005 - Al-Ma'idah (The Table spread with Food)", "http://freequranmp3.com/sudais/005-al-maidah.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 006 - Al-An'am (The Cattle)", "http://freequranmp3.com/sudais/006-al-anam.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 007 - Al-A'raf (The Heights)", "http://freequranmp3.com/sudais/007-al-araf.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 008 - Al-Anfal (The Spoils of War)", "http://freequranmp3.com/sudais/008-al-anfal.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 009 - At-Taubah (The Repentance)", "http://freequranmp3.com/sudais/009-at-taubah.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 010 - Yunus (Jonah)", "http://freequranmp3.com/sudais/010-yunus.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 011 - Hud", "http://freequranmp3.com/sudais/011-hud.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 012 - Yusuf (Joseph)", "http://freequranmp3.com/sudais/012-yusuf.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 013 - Ar-Ra'd (The Thunder)", "http://freequranmp3.com/sudais/013-ar-rad.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 014 - Ibrahim (Abraham)", "http://freequranmp3.com/sudais/014-ibrahim.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 015 - Al-Hijr (The Rocky Tract)", "http://freequranmp3.com/sudais/015-al-hijr.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 016 - An-Nahl (The Bees)", "http://freequranmp3.com/sudais/016-an-nahl.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 017 - Al-Isra' (The Journey by Night)", "http://freequranmp3.com/sudais/017-al-isra.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 018 - Al-Kahf (The Cave)", "http://freequranmp3.com/sudais/018-al-kahf.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 019 - Maryam (Mary)", "http://freequranmp3.com/sudais/019-maryam.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 020 - Ta-Ha", "http://freequranmp3.com/sudais/020-ta-ha.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 021 - Al-Anbiya' (The Prophets)", "http://freequranmp3.com/sudais/021-al-anbiya.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 022 - Al-Hajj (The Pilgrimage)", "http://freequranmp3.com/sudais/022-al-hajj.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 023 - Al-Mu'minun (The Believers)", "http://freequranmp3.com/sudais/023-al-muminun.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 024 - An-Nur (The Light)", "http://freequranmp3.com/sudais/024-an-nur.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 025 - Al-Furqan (The Criterion)", "http://freequranmp3.com/sudais/025-al-furqan.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 026 - Ash-Shu'ara' (The Poets)", "http://freequranmp3.com/sudais/026-ash-shuara.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 027 - An-Naml (The Ants)", "http://freequranmp3.com/sudais/027-an-naml.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 028 - Al-Qasas (The Narration)", "http://freequranmp3.com/sudais/028-al-qasas.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 029 - Al-'Ankabut (The Spider)", "http://freequranmp3.com/sudais/029-al-ankabut.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 030 - Ar-Rum (The Romans)", "http://freequranmp3.com/sudais/030-ar-rum.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 031 - Luqman", "http://freequranmp3.com/sudais/031-luqman.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 032 - As-Sajdah (The Prostration)", "http://freequranmp3.com/sudais/032-as-sajdah.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 033 - Al-Ahzab (The Confederates)", "http://freequranmp3.com/sudais/033-al-ahzab.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 034 - Saba' (Sheba)", "http://freequranmp3.com/sudais/034-saba.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 035 - Fatir (The Originator of Creation)", "http://freequranmp3.com/sudais/035-fatir.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 036 - Ya-Sin", "http://freequranmp3.com/sudais/036-ya-sin.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 037 - As-Saffat (Those Ranged in Ranks)", "http://freequranmp3.com/sudais/037-as-saffat.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 038 - Sad", "http://freequranmp3.com/sudais/038-sad.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 039 - Az-Zumar (The Groups)", "http://freequranmp3.com/sudais/039-az-zumar.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 040 - Ghafir (The Forgiver)", "http://freequranmp3.com/sudais/040-ghafir.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 041 - Fussilat (They are explained in detail)", "http://freequranmp3.com/sudais/041-fussilat.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 042 - Ash-Shura (The Consultation)", "http://freequranmp3.com/sudais/042-ash-shura.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 043 - Az-Zukhruf (The Gold Adornments)", "http://freequranmp3.com/sudais/043-az-zukhruf.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 044 - Ad-Dukhan (The Smoke)", "http://freequranmp3.com/sudais/044-ad-dukhan.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 045 - Al-Jathiyah (The Kneeling)", "http://freequranmp3.com/sudais/045-al-jathiyah.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 046 - Al-Ahqaf (The Curved Sand-hills)", "http://freequranmp3.com/sudais/046-al-ahqaf.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 047 - Muhammad", "http://freequranmp3.com/sudais/047-muhammad.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 048 - Al-Fath (The Victory)", "http://freequranmp3.com/sudais/048-al-fath.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 049 - Al-Hujurat (The Dwellings)", "http://freequranmp3.com/sudais/049-al-hujurat.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 050 - Qaf", "http://freequranmp3.com/sudais/050-qaf.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 051 - Adh-Dhariyat (The Winds that Scatter)", "http://freequranmp3.com/sudais/051-adh-dhariyat.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 052 - At-Tur (The Mount)", "http://freequranmp3.com/sudais/052-at-tur.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 053 - An-Najm (The Star)", "http://freequranmp3.com/sudais/053-an-najm.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 054 - Al-Qamar (The Moon)", "http://freequranmp3.com/sudais/054-al-qamar.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 055 - Ar-Rahman (The Most Gracious)", "http://freequranmp3.com/sudais/055-ar-rahman.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 056 - Al-Waqi'ah (The Event)", "http://freequranmp3.com/sudais/056-al-waqiah.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 057 - Al-Hadid (The Iron)", "http://freequranmp3.com/sudais/057-al-hadid.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 058 - Al-Mujadilah (The Woman who disputes)", "http://freequranmp3.com/sudais/058-al-mujadilah.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 059 - Al-Hashr (The Gathering)", "http://freequranmp3.com/sudais/059-al-hashr.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 060 - Al-Mumtahanah (The Woman to be examined)", "http://freequranmp3.com/sudais/060-al-mumtahanah.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 061 - As-Saff (The Row)", "http://freequranmp3.com/sudais/061-as-saff.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 062 - Al-Jumu'ah (Friday)", "http://freequranmp3.com/sudais/062-al-jumuah.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 063 - Al-Munafiqun (The Hypocrites)", "http://freequranmp3.com/sudais/063-al-munafiqun.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 064 - At-Taghabun (Mutual Loss and Gain)", "http://freequranmp3.com/sudais/064-at-taghabun.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 065 - At-Talaq (The Divorce)", "http://freequranmp3.com/sudais/065-at-talaq.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 066 - At-Tahrim (The Prohibition)", "http://freequranmp3.com/sudais/066-at-tahrim.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 067 - Al-Mulk (Dominion)", "http://freequranmp3.com/sudais/067-al-mulk.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 068 - Al-Qalam (The Pen)", "http://freequranmp3.com/sudais/068-al-qalam.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 069 - Al-Haqqah (The Inevitable)", "http://freequranmp3.com/sudais/069-al-haqqah.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 070 - Al-Ma'arij (The Ways of Ascent)", "http://freequranmp3.com/sudais/070-al-maarij.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 071 - Nuh (Noah)", "http://freequranmp3.com/sudais/071-nuh.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 072 - Al-Jinn (The Jinn)", "http://freequranmp3.com/sudais/072-al-jinn.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 073 - Al-Muzammil (The One wrapped in Garments)", "http://freequranmp3.com/sudais/073-al-muzammil.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 074 - Al-Muddaththir (The One Enveloped)", "http://freequranmp3.com/sudais/074-al-muddaththir.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 075 - Al-Qiyamah (The Resurrection)", "http://freequranmp3.com/sudais/075-al-qiyamah.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 076 - Al-Insan (Man)", "http://freequranmp3.com/sudais/076-al-insan.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 077 - Al-Mursalat (Those sent forth)", "http://freequranmp3.com/sudais/077-al-mursalat.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 078 - An-Naba' (The Great News)", "http://freequranmp3.com/sudais/078-an-naba.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 079 - An-Nazi'at (Those Who Pull Out)", "http://freequranmp3.com/sudais/079-an-naziat.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 080 - 'Abasa (He Frowned)", "http://freequranmp3.com/sudais/080-abasa.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 081 - At-Takwir (Winding Round And Losing Its Light)", "http://freequranmp3.com/sudais/081-at-takwir.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 082 - Al-Infitar (The Cleaving)", "http://freequranmp3.com/sudais/082-al-infitar.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 083 - Al-Mutaffifin (Those Who Deal In Fraud)", "http://freequranmp3.com/sudais/083-al-mutaffifin.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 084 - Al-Inshiqaq (The Splitting Asunder)", "http://freequranmp3.com/sudais/084-al-inshiqaq.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 085 - Al-Buruj (The Big Stars)", "http://freequranmp3.com/sudais/085-al-buruj.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 086 - At-Tariq (The Night Comer)", "http://freequranmp3.com/sudais/086-at-tariq.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 087 - Al-A'la (The Most High)", "http://freequranmp3.com/sudais/087-al-ala.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 088 - Al-Ghashiyah (The Overwhelming)", "http://freequranmp3.com/sudais/088-al-ghashiyah.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 089 - Al-Fajr (The Break of Day)", "http://freequranmp3.com/sudais/089-al-fajr.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 090 - Al-Balad (The City)", "http://freequranmp3.com/sudais/090-al-balad.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 091 - Ash-Shams (The Sun)", "http://freequranmp3.com/sudais/091-ash-shams.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 092 - Al-Lail (The Night)", "http://freequranmp3.com/sudais/092-al-lail.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 093 - Ad-Duha (The Forenoon - After Sunrise)", "http://freequranmp3.com/sudais/093-ad-duha.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 094 - Ash-Sharh (The Opening Forth)", "http://freequranmp3.com/sudais/094-ash-sharh.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 095 - At-Tin (The Fig)", "http://freequranmp3.com/sudais/095-at-tin.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 096 - Al-'Alaq (The Clot)", "http://freequranmp3.com/sudais/096-al-alaq.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 097 - Al-Qadr (The Night of Decree)", "http://freequranmp3.com/sudais/097-al-qadr.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 098 - Al-Baiyyinah (The Clear Evidence)", "http://freequranmp3.com/sudais/098-al-baiyyinah.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 099 - Az-Zalzalah (The Earthquake)", "http://freequranmp3.com/sudais/099-az-zalzalah.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 100 - Al-'Adiyat (Those That Run)", "http://freequranmp3.com/sudais/100-al-adiyat.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 101 - Al-Qari'ah (The Striking Hour)", "http://freequranmp3.com/sudais/101-al-qariah.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 102 - At-Takathur (The Piling Up - The Emulous Desire)", "http://freequranmp3.com/sudais/102-at-takathur.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 103 - Al-'Asr (The Time)", "http://freequranmp3.com/sudais/103-al-asr.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 104 - Al-Humazah (The Slanderer)", "http://freequranmp3.com/sudais/104-al-humazah.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 105 - Al-Fil (The Elephant)", "http://freequranmp3.com/sudais/105-al-fil.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 106 - Quraish", "http://freequranmp3.com/sudais/106-quraish.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 107 - Al-Ma'un (The Small Kindness)", "http://freequranmp3.com/sudais/106-quraish.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 108 - Al-Kauthar (A River In Paradise)", "http://freequranmp3.com/sudais/108-al-kauthar.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 109 - Al-Kafirun (The Disbelievers)", "http://freequranmp3.com/sudais/109-al-kafirun.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 110 - An-Nasr (The Help)", "http://freequranmp3.com/sudais/110-an-nasr.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 111 - Al-Masad (The Palm Fiber)", "http://freequranmp3.com/sudais/111-al-masad.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 112 - Al-Ikhlas (The Purity)", "http://freequranmp3.com/sudais/112-al-ikhlas.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 113 - Al-Falaq (The Daybreak)", "http://freequranmp3.com/sudais/113-al-falaq.mp3", "".toUri()))
            itemList.add(SurahListModel("Surah 114 - An-Nas (Mankind)", "http://freequranmp3.com/sudais/114-an-nas.mp3", "".toUri()))
            adapter!!.notifyDataSetChanged()
        }catch (e:Exception){}
        main_activity_back.setOnClickListener {
            if(isConnected){
                Utils.activityOpenCount(this)
            }
            super.onBackPressed()
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
}