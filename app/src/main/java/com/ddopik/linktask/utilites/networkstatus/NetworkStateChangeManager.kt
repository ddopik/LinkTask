package com.ddopik.attendonb.utilites.networkstatus

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.util.Log

import androidx.annotation.RequiresApi
import com.ddopik.linktask.utilites.Constants


@RequiresApi(api = Build.VERSION_CODES.M)
class NetworkStateChangeManager(private val application: Application) {
    private val TAG = NetworkStateChangeManager::class.java.simpleName

    private val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            .build()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            Log.e(TAG, "onReceive ---> is connected")
            CustomErrorUtils.viewSnackBarError(Constants.ErrorType.ONLINE_CONNECTED)
        }

        override fun onLosing(network: Network, maxMsToLive: Int) {
            super.onLosing(network, maxMsToLive)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            CustomErrorUtils.viewSnackBarError(Constants.ErrorType.ONLINE_DISCONNECTED)
        }

        override fun onUnavailable() {
            super.onUnavailable()
        }

        override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            val connected = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            //            RxEventBus.getInstance().post(new RxAppStatsEvent<>(RxAppStatsEvent.ErrorType.ONLINE_DISCONNECTED, connected));
        }

        override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
            super.onLinkPropertiesChanged(network, linkProperties)
        }
    }

    fun listen() {
        val connectivityManager = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } else {
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
        }
    }

    fun stopListening() {
        val connectivityManager = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}
