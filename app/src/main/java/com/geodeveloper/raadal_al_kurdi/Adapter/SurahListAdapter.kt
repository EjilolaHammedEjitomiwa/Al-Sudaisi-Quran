package com.geodeveloper.raadal_al_kurdi.Adapter

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.developer.kalert.KAlertDialog

import com.geodeveloper.raadal_al_kurdi.Constants
import com.geodeveloper.raadal_al_kurdi.Model.SurahListModel
import com.geodeveloper.raadal_al_kurdi.PlayerActivity
import com.geodeveloper.raadal_al_kurdi.R
import com.geodeveloper.raadal_al_kurdi.helper.Utils
import com.ixuea.android.downloader.DownloadService
import com.ixuea.android.downloader.callback.DownloadListener
import com.ixuea.android.downloader.domain.DownloadInfo
import com.ixuea.android.downloader.exception.DownloadException
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import es.dmoral.toasty.Toasty
import java.io.File


@Suppress("DEPRECATION")
class SurahListAdapter(val context: Activity, val surahList: ArrayList<SurahListModel>, val isOffline: Boolean) : RecyclerView.Adapter<SurahListAdapter.ViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.d_sural_list_design, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return surahList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = surahList[position]
        try {
            holder.title.text = item.title
        } catch (e: Exception) { }

        if (isOffline) {
            holder.donwloadIcon.visibility =View.GONE
            holder.deleteIcon.visibility = View.VISIBLE
        }else{
            holder.deleteIcon.visibility = View.GONE
            holder.donwloadIcon.visibility =View.VISIBLE
        }
        holder.container.setOnClickListener {
            Utils.btnClick(holder.itemView,context)
            var value = false
            if (isOffline) {
                value = true
            }
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra("position", position)
            intent.putExtra("is_offline", value)
            context.startActivity(intent)
        }
        holder.donwloadIcon.setOnClickListener {
            startDownload(item.title, item.url, holder.progressBar)
        }
        holder.deleteIcon.setOnClickListener {
            KAlertDialog(context, KAlertDialog.WARNING_TYPE)
                    .setTitleText("Warning")
                    .setContentText("Are you sure you want to remove this surah from your download list")
                    .setConfirmText("Yes remove")
                    .setCancelText("Cancel")
                    .confirmButtonColor(R.color.colorPrimary)
                    .cancelButtonColor(R.color.colorPrimary)
                    .setConfirmClickListener {
                        it.dismiss()
                        removeSurahFromOffline(item.uri,position)
                    }.setCancelClickListener {
                        it.dismiss()
                    }
                    .show()
        }
    }

    private fun removeSurahFromOffline(uri: Uri, position: Int) {
        val file = File(uri.path!!)
        try {
            file.delete()
            Toasty.success(context, "Successfully removed", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
        }
    }

    private fun startDownload(title: String, url: String, progressBar: CircularProgressBar) {
        val downloadManager = DownloadService.getDownloadManager(context)
        val folder_main = Constants.mainStorageFolder
        val f = File(Environment.getExternalStorageDirectory(), folder_main)
        if (!f.exists()) {
            f.mkdirs()
        }
        val outPath = File("$f/$title")
        try {
            val downloadInfo = DownloadInfo.Builder().setUrl(url).setPath(outPath.toString()).build()
            downloadInfo!!.downloadListener = object : DownloadListener {
                override fun onStart() {
                    Toasty.info(context, "Download start", Toast.LENGTH_LONG, true).show()
                }
                override fun onDownloadFailed(e: DownloadException?) {
                    Toasty.error(context, "Error: $e", Toast.LENGTH_LONG, true).show()
                    progressBar.visibility = View.GONE
                    outPath.delete()
                }
                override fun onDownloadSuccess() {
                    progressBar.visibility = View.GONE
                    Toasty.success(context, "Download complete", Toast.LENGTH_LONG, true).show()
                    Utils.showInterstitialAds(context)
                    notifyDataSetChanged()
                }
                override fun onDownloading(downloadProgress: Long, size: Long) {
                    progressBar.visibility = View.VISIBLE
                    progressBar.apply {
                        progress = downloadProgress.toFloat()
                        progressMax = size.toFloat()
                    }
                }
                override fun onPaused() {
                    Toasty.info(context, "Download paused", Toast.LENGTH_LONG, true).show()
                }

                override fun onRemoved() {
                    Toasty.info(context, "Download removed", Toast.LENGTH_LONG, true).show()
                }

                override fun onWaited() {
                    Toasty.info(context, "Download waited", Toast.LENGTH_LONG, true).show()
                }
            }
            downloadManager.download(downloadInfo)
        } catch (e: java.lang.Exception) {
        }
    }
    inner class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        val container: LinearLayout = itemView.findViewById(R.id.surah_design_container)
        val title: TextView = itemView.findViewById(R.id.surah_design_title)
        val donwloadIcon: ImageView = itemView.findViewById(R.id.surah_design_DownloadIcon)
        val deleteIcon: ImageView = itemView.findViewById(R.id.surah_design_deleteIcon)
        val progressBar: CircularProgressBar = itemView.findViewById(R.id.surah_design_progressBar)
        val downloadedIcon:ImageView = itemView.findViewById(R.id.surah_design_DownloadedIcon)
    }
}