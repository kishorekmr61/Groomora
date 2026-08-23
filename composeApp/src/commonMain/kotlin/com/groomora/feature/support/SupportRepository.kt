package com.groomora.feature.support

import kotlinx.coroutines.flow.Flow

interface SupportRepository {
    fun getFaqs(): Flow<List<FaqItem>>
    fun getTickets(): Flow<List<SupportTicket>>
    suspend fun submitTicket(subject: String, message: String): Boolean
}
