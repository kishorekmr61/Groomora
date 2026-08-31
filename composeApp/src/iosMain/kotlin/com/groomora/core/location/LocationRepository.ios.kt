package com.groomora.core.location

actual fun createLocationRepository(): LocationRepository = MockLocationRepository()
