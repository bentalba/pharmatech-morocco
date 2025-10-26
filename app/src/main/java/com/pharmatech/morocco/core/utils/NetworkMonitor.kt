package com.pharmatech.morocco.core.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import timber.log.Timber

class NetworkMonitor(private val context: Context) {

    val isConnected: Flow<Boolean> = callbackFlow {
        try {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

            if (connectivityManager == null) {
                Timber.e("ConnectivityManager is null - network monitoring unavailable")
                trySend(false)
                close()
                return@callbackFlow
            }

            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    trySend(true)
                    Timber.d("Network available")
                }

                override fun onLost(network: Network) {
                    trySend(false)
                    Timber.d("Network lost")
                }

                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    val connected = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    trySend(connected)
                }
            }

            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()

            try {
                connectivityManager.registerNetworkCallback(request, callback)
            } catch (e: SecurityException) {
                Timber.e(e, "SecurityException registering network callback - missing permission?")
                trySend(false)
                close()
                return@callbackFlow
            } catch (e: Exception) {
                Timber.e(e, "Error registering network callback")
                trySend(false)
                close()
                return@callbackFlow
            }

            // Send initial state
            val isCurrentlyConnected = try {
                connectivityManager.activeNetwork?.let { network ->
                    connectivityManager.getNetworkCapabilities(network)
                        ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
                } ?: false
            } catch (e: Exception) {
                Timber.e(e, "Error checking initial network state")
                false
            }
            trySend(isCurrentlyConnected)

            awaitClose {
                try {
                    connectivityManager.unregisterNetworkCallback(callback)
                } catch (e: Exception) {
                    Timber.e(e, "Error unregistering network callback")
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "Fatal error in network monitoring")
            trySend(false)
            close()
        }
    }.distinctUntilChanged()

    fun isCurrentlyConnected(): Boolean {
        return try {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

            if (connectivityManager == null) {
                Timber.e("ConnectivityManager is null")
                return false
            }

            connectivityManager.activeNetwork?.let { network ->
                connectivityManager.getNetworkCapabilities(network)
                    ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
            } ?: false
        } catch (e: SecurityException) {
            Timber.e(e, "SecurityException checking network - missing permission?")
            false
        } catch (e: Exception) {
            Timber.e(e, "Error checking network connectivity")
            false
        }
    }
}

