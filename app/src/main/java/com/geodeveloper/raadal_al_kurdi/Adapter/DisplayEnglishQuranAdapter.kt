package com.geodeveloper.raadal_al_kurdi.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.geodeveloper.raadal_al_kurdi.R
import com.geodeveloper.theholyquran.models.englishquran.AyahModel

class DisplayEnglishQuranAdapter (val context: Context, val itemList: ArrayList<AyahModel>) : RecyclerView.Adapter<DisplayEnglishQuranAdapter.ViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.d_quran_english_design, parent, false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val quran = itemList[position]
        holder.text.text = quran.text
        holder.number.text = quran.numberInSurah.toString()
    }
    inner class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        val number: TextView = itemView.findViewById(R.id.english_quran_design_number)
        val text:TextView = itemView.findViewById(R.id.english_quran_design_text)
    }
}