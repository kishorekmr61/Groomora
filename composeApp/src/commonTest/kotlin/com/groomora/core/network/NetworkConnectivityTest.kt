package com.groomora.core.network

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NetworkConnectivityTest {

    @Test
    fun defaultNetworkConnectivity_startsConnected() {
        val manager = DefaultNetworkConnectivityManager()
        assertTrue(manager.isConnected.value, "Default connectivity state should be true (connected)")
    }

    @Test
    fun setConnected_updatesStateCorrectly() {
        val manager = DefaultNetworkConnectivityManager()
        
        manager.setConnected(false)
        assertFalse(manager.isConnected.value, "Connectivity state should transition to false (disconnected)")

        manager.setConnected(true)
        assertTrue(manager.isConnected.value, "Connectivity state should transition back to true (reconnected)")
    }
}
