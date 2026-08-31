package com.groomora.app

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.groomora.core.notifications.AndroidPushNotificationManager
import com.groomora.core.crash.AndroidCrashReporter
import com.groomora.core.configuration.FirebaseConfigRepository
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private var networkCallback: ConnectivityManager.NetworkCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize Platform Specific Services
        val crashReporter = AndroidCrashReporter()
        DependencyContainer.crashReporter = crashReporter
        
        val configRepository = FirebaseConfigRepository()
        DependencyContainer.configRepository = configRepository
        
        val pushManager = AndroidPushNotificationManager(this)
        DependencyContainer.pushNotificationManager = pushManager
        
        // ... rest of init
        crashReporter.logBreadcrumb("App Started")
        
        MainScope().launch {
            kotlinx.coroutines.delay(1000)
            configRepository.fetchConfig()
        }

        pushManager.initialize()
        pushManager.requestPermission()

        registerNetworkMonitoring()
        setContent { App() }
    }

    private fun registerNetworkMonitoring() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager ?: return

        // Check initial connectivity status
        val activeNetwork = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        val isConnected = capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        DependencyContainer.networkConnectivityManager.setConnected(isConnected)

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                runOnUiThread {
                    DependencyContainer.networkConnectivityManager.setConnected(true)
                }
            }

            override fun onLost(network: Network) {
                val currentCaps = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                val stillConnected = currentCaps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
                runOnUiThread {
                    DependencyContainer.networkConnectivityManager.setConnected(stillConnected)
                }
            }

            override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                val hasInternet = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                runOnUiThread {
                    DependencyContainer.networkConnectivityManager.setConnected(hasInternet)
                }
            }
        }

        try {
            connectivityManager.registerNetworkCallback(request, networkCallback!!)
        } catch (_: Exception) {}
    }

    override fun onDestroy() {
        super.onDestroy()
        networkCallback?.let {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            try {
                connectivityManager?.unregisterNetworkCallback(it)
            } catch (_: Exception) {}
        }
    }
}
