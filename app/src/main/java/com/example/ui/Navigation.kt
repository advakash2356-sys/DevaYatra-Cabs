package com.example.ui

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    object Dashboard
    
    @Serializable
    object Bookings
    
    @Serializable
    object Drivers
    
    @Serializable
    object Escalations
    
    @Serializable
    object PrivacyPolicy
    
    @Serializable
    data class BookingDetail(val bookingId: Long)
}
