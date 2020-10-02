package com.geodeveloper.raadal_al_kurdi

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.URLUtil
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kaopiz.kprogresshud.KProgressHUD
import es.dmoral.toasty.Toasty
import im.delight.android.webview.AdvancedWebView
import kotlinx.android.synthetic.main.activity_web.*


class WebActivity : AppCompatActivity(), AdvancedWebView.Listener{
    private var progress: KProgressHUD? = null
    private var url: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        progress = KProgressHUD(this)
        progress = KProgressHUD.create(this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Loading.....")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
            .show()

        url = intent.getStringExtra("url")!!
        web_activity_webView.setListener(this, this)
        web_activity_webView.setMixedContentAllowed(true)
        web_activity_webView.setCookiesEnabled(true)
        web_activity_webView.setThirdPartyCookiesEnabled(true)

        this.web_activity_webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                if (url.startsWith("http:") || url.startsWith("https:")) {
                  return false
                }
                when {
                    URLUtil.isNetworkUrl(url) -> { return false }
                    url.startsWith("intent") -> {
                        val browserIntent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        startActivity(browserIntent)
                        finish()
                    }
                    else -> {
                        try {
                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            startActivity(browserIntent)
                            finish()
                        } catch(e: ActivityNotFoundException) {
                            Toast.makeText(this@WebActivity, "Appropriate app not found", Toast.LENGTH_LONG).show()
                            Log.e("AndroidRide",e.toString())
                        }
                    }
                }

                return true
            }
        }
        web_activity_webView.loadUrl(url)

        web_activity_backIcon.setOnClickListener {
            finish()
            Animatoo.animateFade(this)
        }

        web_activity_refresh.setOnClickListener {
            web_activity_webView.loadUrl(url)
        }
    }

    override fun onExternalPageRequest(url: String?) {
    }

    override fun onPageFinished(url: String?) {
        if (progress!!.isShowing) {
            progress!!.dismiss()
        }
    }

    override fun onPageError(errorCode: Int, description: String?, failingUrl: String?) {
        if (progress!!.isShowing) {
            progress!!.dismiss()
        }
        Toasty.error(this, description!!, Toast.LENGTH_LONG).show()
    }

    override fun onDownloadRequested(url: String?, suggestedFilename: String?, mimeType: String?, contentLength: Long, contentDisposition: String?, userAgent: String?) {
    }

    override fun onPageStarted(url: String?, favicon: Bitmap?) {
        if (!progress!!.isShowing) {
            progress = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Loading.....")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show()
        }
    }

    @SuppressLint("NewApi")
    override fun onResume() {
        super.onResume()
        web_activity_webView.onResume()
    }

    @SuppressLint("NewApi")
    override fun onPause() {
        web_activity_webView.onPause()
        super.onPause()
    }

    override fun onBackPressed() {
        if (progress!!.isShowing) {
            progress!!.dismiss()
        }
        if (!web_activity_webView.onBackPressed()) {
            return
        }
        super.onBackPressed()
    }
    override fun onDestroy() {
        super.onDestroy()
        web_activity_webView.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        web_activity_webView.onActivityResult(requestCode, resultCode, data)
    }
}
