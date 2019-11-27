package com.ddopik.attendonb.app

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import androidx.multidex.MultiDexApplication
import com.androidnetworking.AndroidNetworking
import com.ddopik.linktask.network.BasicAuthInterceptor
import com.ddopik.linktask.utilites.networkstatus.NetworkChangeBroadcastReceiver
import com.google.android.gms.security.ProviderInstaller
import com.ddopik.attendonb.utilites.networkstatus.NetworkStateChangeManager
import okhttp3.OkHttpClient
import java.io.File
import javax.net.ssl.SSLContext


class LinkTaskApp : MultiDexApplication() {


    private var networkStateChangeManager: NetworkStateChangeManager? = null
    private var networkChangeBroadcastReceiver: NetworkChangeBroadcastReceiver? = null

    companion object {

        var app: LinkTaskApp? = null

    }

    override fun onCreate() {
        super.onCreate()


//        Utilities().printHashKey(app)
        /**
         * Required for Network Authority Access prior Api 19
         * */
        ProviderInstaller.installIfNeeded(applicationContext);
        try {
            val sslContext = SSLContext.getInstance("TLSv1.2")
            sslContext.init(null, null, null)
            sslContext.createSSLEngine();
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        app = this
        LinkTaskApp.app = app as LinkTaskApp

//        initFastAndroidNetworking(null,baseContext)
        //        initRealm(); //--> [1]order is must
        //        setRealmDefaultConfiguration(); //--> [2]order is must
        //        intializeSteatho();
        //        deleteCache(app);   ///for developing        ##################
        //        initializeDepInj(); ///intializing Dagger Dependancy

        if (Build.VERSION.SDK_INT == 19) {
            try {
                ProviderInstaller.installIfNeeded(this)
            } catch (ignored: Exception) {
            }

        }

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






    fun initFastAndroidNetworking(userToken: String?, context: Context) {

        /**
         * initializing block to add authentication to your Header Request
         */
        if (userToken != null) {
            val basicAuthInterceptor = BasicAuthInterceptor(context)
            basicAuthInterceptor.setUserToken(userToken)
            val okHttpClient = OkHttpClient().newBuilder()
                .addNetworkInterceptor(basicAuthInterceptor)
                .build()
            AndroidNetworking.initialize(context, okHttpClient)
        } else {
            /**
             * default initialization
             */
            AndroidNetworking.initialize(context)
        }
    }

}
