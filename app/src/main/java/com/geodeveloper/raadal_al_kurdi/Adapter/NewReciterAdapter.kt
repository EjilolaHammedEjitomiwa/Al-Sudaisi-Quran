package com.geodeveloper.raadal_al_kurdi.Adapter

import android.content.Context
import android.content.Intent
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.developer.kalert.KAlertDialog
import com.geodeveloper.raadal_al_kurdi.Constants
import com.geodeveloper.raadal_al_kurdi.Model.NewReciterModel
import com.geodeveloper.raadal_al_kurdi.Model.UsersModel
import com.geodeveloper.raadal_al_kurdi.R
import com.geodeveloper.raadal_al_kurdi.WebActivity
import com.geodeveloper.raadal_al_kurdi.helper.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import es.dmoral.toasty.Toasty

class NewReciterAdapter(val context: Context, val surahList: ArrayList<NewReciterModel>) : RecyclerView.Adapter<NewReciterAdapter.ViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.d_new_reciter_design, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return surahList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = surahList[position]
        try {
            Glide.with(context).load(item.image_url).into(holder.image)
            holder.name.text = item.name
        }catch (e:Exception){}

        holder.image.setOnClickListener {
            if(item.link != ""){
                val intent = Intent(context, WebActivity::class.java)
                intent.putExtra(Constants.url,item.link)
                context.startActivity(intent)
            }
        }

        holder.image.setOnLongClickListener {
            if(Utils.currentUser() != null){
                Utils.databaseRef().child(Constants.admin).child(Utils.currentUserID()!!).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()) {
                            KAlertDialog(context, KAlertDialog.WARNING_TYPE)
                                .setTitleText("Warning")
                                .setContentText("Are you sure you want to remove this reciter")
                                .setConfirmText("Yes remove")
                                .setCancelText("Cancel")
                                .confirmButtonColor(R.color.colorPrimary)
                                .cancelButtonColor(R.color.colorPrimary)
                                .setConfirmClickListener {
                                    it.dismiss()
                                    removeReciter(item.id)
                                }.setCancelClickListener {
                                    it.dismiss()
                                }
                                .show()

                        }
                    }

                    override fun onCancelled(p0: DatabaseError) {}
                })
            }
            return@setOnLongClickListener true
        }
    }

    private fun removeReciter(id: String) {
        try {
            Utils.databaseRef().child(Constants.newReciter).child(id).removeValue().addOnCompleteListener {
                if(it.isSuccessful){
                    Toasty.success(context,"Removed successfully",Toast.LENGTH_LONG).show()
                }
            }
        }catch (e:java.lang.Exception){}

    }

    inner class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.new_reciter_design_image)
        val name: TextView = itemView.findViewById(R.id.new_reciter_design_title)
    }
}