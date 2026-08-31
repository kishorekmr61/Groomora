package com.groomora.core.analytics

import com.groomora.core.util.GroomoraLog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * iOS implementation of AnalyticsManager.
 * To enable real Firebase tracking on iOS:
 * 1. Add FirebaseAnalytics pod to your Xcode project.
 * 2. Bridge the calls below to FIRAnalytics.
 */
class IosAnalyticsManager : AnalyticsManager {
    private val _eventHistory = MutableStateFlow<List<AnalyticsEvent>>(emptyList())
    override val eventHistory: StateFlow<List<AnalyticsEvent>> = _eventHistory.asStateFlow()

    override fun logEvent(name: String, params: Map<String, String>) {
        val safeParams = params.filterKeys { key ->
            !key.contains("password", ignoreCase = true) &&
            !key.contains("token", ignoreCase = true) &&
            !key.contains("otp", ignoreCase = true) &&
            !key.contains("card", ignoreCase = true)
        }
        
        // Mock logging for now to prevent build errors without CocoaPods
        GroomoraLog.d("Analytics-iOS", "[MOCK] $name: $safeParams")
        
        val event = AnalyticsEvent(name = name, params = safeParams, timestamp = 0L)
        _eventHistory.value = _eventHistory.value + event
    }

    override fun logScreenView(screenName: String) {
        logEvent("screen_view", mapOf("screen_name" to screenName))
    }

    override fun logFunnelStep(funnelName: String, stepNumber: Int, stepName: String) {
        logEvent(
            "funnel_step",
            mapOf(
                "funnel_name" to funnelName,
                "step_number" to stepNumber.toString(),
                "step_name" to stepName
            )
        )
    }
}

actual fun createAnalyticsManager(): AnalyticsManager = IosAnalyticsManager()
