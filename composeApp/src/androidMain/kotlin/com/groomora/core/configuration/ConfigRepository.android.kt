package com.groomora.core.configuration

actual fun createConfigRepository(): ConfigRepository = FirebaseConfigRepository()
