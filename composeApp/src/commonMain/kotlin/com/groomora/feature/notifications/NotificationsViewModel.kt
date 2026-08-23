package com.groomora.feature.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NotificationsViewModel(
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private val _state = MutableStateFlow(NotificationsState())
    val state: StateFlow<NotificationsState> = _state.asStateFlow()

    init {
        loadNotifications()
    }

    private fun loadNotifications() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            combine(
                notificationRepository.getNotifications(),
                notificationRepository.getUnreadCount()
            ) { notifications, unreadCount ->
                NotificationsState(
                    isLoading = false,
                    notifications = notifications,
                    unreadCount = unreadCount
                )
            }.collect { newState ->
                _state.value = newState
            }
        }
    }

    fun onIntent(intent: NotificationsIntent) {
        when (intent) {
            NotificationsIntent.LoadNotifications -> loadNotifications()
            is NotificationsIntent.MarkAsRead -> {
                viewModelScope.launch {
                    notificationRepository.markAsRead(intent.id)
                }
            }
            NotificationsIntent.MarkAllAsRead -> {
                viewModelScope.launch {
                    notificationRepository.markAllAsRead()
                }
            }
        }
    }
}
