package com.groomora.core.analytics

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AnalyticsEvent(
    val name: String,
    val params: Map<String, String> = emptyMap(),
    val timestamp: Long = 0L
)

interface AnalyticsManager {
    fun logEvent(name: String, params: Map<String, String> = emptyMap())
    fun logScreenView(screenName: String)
    fun logFunnelStep(funnelName: String, stepNumber: Int, stepName: String)
    val eventHistory: StateFlow<List<AnalyticsEvent>>
}

class DefaultAnalyticsManager : AnalyticsManager {
    private val _eventHistory = MutableStateFlow<List<AnalyticsEvent>>(emptyList())
    override val eventHistory: StateFlow<List<AnalyticsEvent>> = _eventHistory.asStateFlow()

    override fun logEvent(name: String, params: Map<String, String>) {
        // Privacy filter: strip any PII/tokens before logging
        val safeParams = params.filterKeys { key ->
            !key.contains("password", ignoreCase = true) &&
            !key.contains("token", ignoreCase = true) &&
            !key.contains("otp", ignoreCase = true) &&
            !key.contains("card", ignoreCase = true)
        }
        val event = AnalyticsEvent(name = name, params = safeParams, timestamp = 0L)
        _eventHistory.value = _eventHistory.value + event
        println("[Analytics] $name: $safeParams")
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
