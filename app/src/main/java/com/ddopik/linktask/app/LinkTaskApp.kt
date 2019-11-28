package com.ddopik.attendonb.app

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import androidx.multidex.MultiDexApplication
import com.androidnetworking.AndroidNetworking
import com.ddopik.attendonb.network.BaseNetWorkApi
import com.ddopik.attendonb.utilites.networkstatus.NetworkStateChangeManager
import com.ddopik.linktask.appUtilites.networkstatus.NetworkChangeBroadcastReceiver
import java.io.File


class LinkTaskApp : MultiDexApplication() {


    private var networkStateChangeManager: NetworkStateChangeManager? = null
    private var networkChangeBroadcastReceiver: NetworkChangeBroadcastReceiver? = null

    companion object {

        var app: LinkTaskApp? = null

    }

    override fun onCreate() {
        super.onCreate()
        app = this


        AndroidNetworking.initialize(applicationContext, BaseNetWorkApi.initNetWorkCirtefecate())


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            networkStateChangeManager = NetworkStateChangeManager(this)
            networkStateChangeManager?.listen()
        } else {
            networkChangeBroadcastReceiver = NetworkChangeBroadcastReceiver()
            val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            registerReceiver(networkChangeBroadcastReceiver, filter)
        }


    }


    fun getApp(): LinkTaskApp {
        if (app != null) {
            return app as LinkTaskApp
        }
        throw NullPointerException("u should init AppContext  first")
    }


    /**
     * delete App Cache and NetWork Cache
     */
    fun deleteCache(context: Context) {
        try {
            val dir = context.cacheDir
            deleteDir(dir)
        } catch (e: Exception) {
        }

    }

    fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
            return dir.delete()
        } else return if (dir != null && dir.isFile) {
            dir.delete()
        } else {
            false
        }

    }


}
