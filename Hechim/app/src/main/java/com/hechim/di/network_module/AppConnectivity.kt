package com.hechim.di.network_module

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class AppConnectivity @Inject constructor(
    @ApplicationContext context: Context
){
    var available = false

    private val connectivityManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        // network is available for use
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            available = true
        }

        // Network capabilities have changed for the network
        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            checkConnectivity()
        }

        // lost network connection
        override fun onLost(network: Network) {
            super.onLost(network)
            available = false
        }
    }

    private val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()

    init {
        connectivityManager.requestNetwork(networkRequest, networkCallback)
        checkConnectivity()
    }

    private fun checkConnectivity() {
        val activeNetwork = connectivityManager.activeNetwork
        val activeNetworkInfo : NetworkCapabilities? = connectivityManager.getNetworkCapabilities(activeNetwork)

        if(activeNetworkInfo == null) {
            available = false
            return
        }

        available = when {
            activeNetworkInfo.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetworkInfo.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
//            //for other device how are able to connect with Ethernet
//            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
//            //for check internet over Bluetooth
//            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    }




}