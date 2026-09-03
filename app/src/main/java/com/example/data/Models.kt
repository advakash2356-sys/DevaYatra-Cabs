package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
enum class BookingStatus {
    PENDING, ASSIGNED, ACTIVE, DONE, CANCELLED
}

@Serializable
enum class EscalationLevel {
    NORMAL, WARNING, URGENT
}

@Entity(tableName = "bookings")
@Serializable
data class Booking(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bookingId: String, // e.g., DYC-001
    val timestamp: Long = System.currentTimeMillis(),
    val customerName: String,
    val customerPhone: String,
    val pickupLocation: String,
    val destination: String,
    val travelDate: String,
    val travelTime: String,
    val passengers: Int,
    val status: BookingStatus = BookingStatus.PENDING,
    val driverId: Long? = null,
    val driverName: String? = null,
    val vehicleNumber: String? = null,
    val quotedFare: Double,
    val commission: Double,
    val escalationLevel: EscalationLevel = EscalationLevel.NORMAL,
    val rating: Int? = null,
    val notes: String? = null
)

@Entity(tableName = "drivers")
@Serializable
data class Driver(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val phone: String,
    val vehicleType: String,
    val vehicleNumber: String,
    val status: DriverStatus = DriverStatus.AVAILABLE,
    val totalRides: Int = 0,
    val averageRating: Float = 0f,
    val weeklyCommissionDue: Double = 0.0,
    val totalCommissionPaid: Double = 0.0
)

@Serializable
enum class DriverStatus {
    AVAILABLE, BUSY, OFFLINE, SUSPENDED
}

@Entity(tableName = "escalations")
@Serializable
data class Escalation(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bookingId: Long,
    val type: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isResolved: Boolean = false,
    val notes: String? = null
)
