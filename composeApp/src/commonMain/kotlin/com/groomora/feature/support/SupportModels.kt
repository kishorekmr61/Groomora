package com.groomora.feature.support

import kotlinx.serialization.Serializable

@Serializable
data class FaqItem(
    val id: String,
    val question: String,
    val answer: String,
    val category: String
)

@Serializable
data class SupportTicket(
    val id: String,
    val subject: String,
    val status: String,
    val date: String,
    val lastMessage: String? = null
)

data class SupportState(
    val isLoading: Boolean = false,
    val faqs: List<FaqItem> = emptyList(),
    val tickets: List<SupportTicket> = emptyList(),
    val error: String? = null
)

sealed interface SupportIntent {
    data object LoadSupportData : SupportIntent
    data class SubmitQuery(val subject: String, val message: String) : SupportIntent
}
