package com.geodeveloper.raadal_al_kurdi.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.geodeveloper.raadal_al_kurdi.Constants
import com.geodeveloper.raadal_al_kurdi.Model.VideoRecitatioModel
import com.geodeveloper.raadal_al_kurdi.R
import com.geodeveloper.raadal_al_kurdi.VideoPlayerActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class VideoRecitationAdapter(val context: Context, val surahList: ArrayList<VideoRecitatioModel>) : RecyclerView.Adapter<VideoRecitationAdapter.ViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.video_recittation_design, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return surahList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = surahList[position]
        try {
            Glide.with(context).load(item.thumbnail).into(holder.thumbnail)
            holder.title.text = item.title
        }catch (e:Exception){}
        updateViewsCounts(item.id,holder.viewsCount )
        holder.itemView.setOnClickListener {
            val intent = Intent(context,VideoPlayerActivity::class.java)
            intent.putExtra("title", item.title)
            intent.putExtra("url", item.link)
            intent.putExtra("id",item.id)
            context.startActivity(intent)
        }
    }

    private fun updateViewsCounts(id: String?, viewsCount: TextView) {
        com.geodeveloper.raadal_al_kurdi.helper.Utils.databaseRef().child(Constants.videoRecitation).child(Constants.fromYoutube).child(id!!).child(Constants.views).addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    viewsCount.text ="${p0.childrenCount} views"
                }else{
                    viewsCount.text = "0 views"
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

    }
    inner class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        val thumbnail: ImageView = itemView.findViewById(R.id.video_recitation_design_thumbnail)
        val title: TextView = itemView.findViewById(R.id.video_recitation_design_title)
        val viewsCount: TextView = itemView.findViewById(R.id.video_recitation_design_viewsCount)

    }
}