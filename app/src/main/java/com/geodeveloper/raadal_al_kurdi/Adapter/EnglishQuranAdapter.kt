package com.geodeveloper.raadal_al_kurdi.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.geodeveloper.raadal_al_kurdi.R
import com.geodeveloper.raadal_al_kurdi.ReadEnglishQuranActivity
import com.geodeveloper.theholyquran.models.englishquran.QuranModel


class EnglishQuranAdapter (val context: Context, val quranList: QuranModel) : RecyclerView.Adapter<EnglishQuranAdapter.ViewHolder?>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.d_quran_design, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return quranList.data!!.surah!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val quran = quranList.data!!.surah!![position]
        holder.name.text = quran.englishName
        holder.meaning.text = quran.englishNameTranslation

        holder.itemView.setOnClickListener {
            val ayah = quran.ayah!!
            val ayahLength = ayah.size-1
            val intent = Intent(context, ReadEnglishQuranActivity::class.java)
            intent.putExtra("surah",ayah)
            intent.putExtra("englishName",quran.englishName)
            intent.putExtra("arabicName",quran.name)
            intent.putExtra("start_page",ayah[0].page)
            intent.putExtra("end_page",ayah[ayahLength].page)
            context.startActivity(intent)
            Animatoo.animateSwipeLeft(context)
        }
    }

    inner class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.quran_design_name)
        val meaning: TextView = itemView.findViewById(R.id.quran_design_englishMeaning)

    }
}