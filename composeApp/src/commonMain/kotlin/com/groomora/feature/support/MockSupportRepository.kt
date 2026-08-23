package com.groomora.feature.support

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

class MockSupportRepository : SupportRepository {
    private val _tickets = MutableStateFlow(
        listOf(
            SupportTicket("t1", "Booking not showing", "Resolved", "Oct 18, 2024", "We have fixed the issue."),
            SupportTicket("t2", "Refund for Order #101", "Open", "Today", "Waiting for team response.")
        )
    )

    private val faqs = listOf(
        FaqItem("f1", "How to book a service?", "Select a salon, pick a service, choose a time, and confirm!", "Booking"),
        FaqItem("f2", "Can I cancel my booking?", "Yes, you can cancel up to 2 hours before the appointment.", "Booking"),
        FaqItem("f3", "What is Groomora Loyalty?", "It's our rewards program where you earn points for every service.", "Loyalty"),
        FaqItem("f4", "How to apply a coupon?", "Enter the code at the checkout screen before payment.", "Offers")
    )

    override fun getFaqs(): Flow<List<FaqItem>> = flow {
        delay(400)
        emit(faqs)
    }

    override fun getTickets(): Flow<List<SupportTicket>> = _tickets.asStateFlow()

    override suspend fun submitTicket(subject: String, message: String): Boolean {
        delay(1000)
        val newTicket = SupportTicket(
            id = "t_new_${(100..999).random()}",
            subject = subject,
            status = "Open",
            date = "Today",
            lastMessage = message
        )
        val current = _tickets.value.toMutableList()
        current.add(0, newTicket)
        _tickets.value = current
        return true
    }
}
