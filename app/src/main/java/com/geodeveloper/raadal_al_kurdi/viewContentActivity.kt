package com.geodeveloper.raadal_al_kurdi

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.geodeveloper.raadal_al_kurdi.ManageNetworkState.ConnectivityReciever.Companion.isConnected
import com.geodeveloper.raadal_al_kurdi.helper.Utils
import io.github.armcha.autolink.MODE_HASHTAG
import io.github.armcha.autolink.MODE_URL
import kotlinx.android.synthetic.main.view_content.*

class viewContentActivity : AppCompatActivity() {
    private var title: String? = null
    private var content: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_content)

        title = intent.getStringExtra("title")!!
        content = intent.getStringExtra("body")!!

        view_content_desc.addAutoLinkMode(MODE_HASHTAG, MODE_URL)
        view_content_desc.urlModeColor = resources.getColor(R.color.nice_blue)
        view_content_desc.pressedTextColor = resources.getColor(R.color.successColor)
        view_content_desc.onAutoLinkClick {
            val intent = Intent(this, WebActivity::class.java)
            intent.putExtra("url", it.originalText)
            startActivity(intent)
        }
        view_content_title.text = title
        view_content_desc.text = content

        view_content_backIcon.setOnClickListener {
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
}
