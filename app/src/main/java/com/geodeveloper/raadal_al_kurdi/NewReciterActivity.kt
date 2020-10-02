package com.geodeveloper.raadal_al_kurdi

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.geodeveloper.raadal_al_kurdi.Adapter.NewReciterAdapter
import com.geodeveloper.raadal_al_kurdi.ManageNetworkState.ConnectivityReciever.Companion.isConnected
import com.geodeveloper.raadal_al_kurdi.Model.NewReciterModel
import com.geodeveloper.raadal_al_kurdi.helper.Utils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_new_reciter.*
import java.lang.IllegalStateException

class NewReciterActivity : AppCompatActivity(){
    private var itemList = ArrayList<NewReciterModel>()
    private var adapter: NewReciterAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_reciter)

        adapter = NewReciterAdapter(this, itemList)
        new_reciter_activity_recyclerView.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(this, 3)
        new_reciter_activity_recyclerView.layoutManager = layoutManager
        new_reciter_activity_recyclerView.adapter = adapter

        if(isConnected){
            getItemList()
        }else{
           Toasty.error(this,getString(R.string.pleaseBeConnectedToInternet),Toasty.LENGTH_LONG).show()
        }

        new_reciter_activity_backIcon.setOnClickListener {
            if(isConnected){
                Utils.activityOpenCount(this)
            }
            super.onBackPressed()
            Animatoo.animateSwipeRight(this)
        }
    }
    private fun getItemList() {
        try {
            Utils.databaseRef().child(Constants.newReciter).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        itemList.clear()
                        for (snapshot in p0.children) {
                            val item = snapshot.getValue(NewReciterModel::class.java)
                            itemList.add(item!!)
                            itemList.reverse()
                        }
                        adapter!!.notifyDataSetChanged()
                        try {
                            loader.visibility = View.GONE
                        }catch (e:IllegalStateException){}
                    }
                }
                override fun onCancelled(p0: DatabaseError) {
                }
            })
        } catch (e: Exception) {
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
