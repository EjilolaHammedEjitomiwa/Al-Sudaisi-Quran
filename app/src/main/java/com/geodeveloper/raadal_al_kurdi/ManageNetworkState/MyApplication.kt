package com.geodeveloper.raadal_al_kurdi.ManageNetworkState

import android.app.Application
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
    fun setConnectivityListener(listener: ConnectivityReciever.ConnectivityReceiverListener?) {
        ConnectivityReciever.connectivityReceiverListener = listener
    }
    companion object {
        @get:Synchronized
        var instance: MyApplication? = null
            private set
    }
}
