package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingDao {
    @Query("SELECT * FROM bookings ORDER BY timestamp DESC")
    fun getAllBookings(): Flow<List<Booking>>

    @Query("SELECT * FROM bookings WHERE escalationLevel != 'NORMAL' AND status != 'DONE' AND status != 'CANCELLED' ORDER BY timestamp DESC")
    fun getActiveEscalations(): Flow<List<Booking>>

    @Query("SELECT * FROM bookings WHERE id = :id")
    suspend fun getBookingById(id: Long): Booking?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: Booking): Long

    @Update
    suspend fun updateBooking(booking: Booking)

    @Query("SELECT COUNT(*) FROM bookings WHERE status = 'DONE'")
    fun getCompletedCount(): Flow<Int>

    @Query("SELECT SUM(commission) FROM bookings WHERE status = 'DONE'")
    fun getTotalCommission(): Flow<Double?>
}

@Dao
interface DriverDao {
    @Query("SELECT * FROM drivers")
    fun getAllDrivers(): Flow<List<Driver>>

    @Query("SELECT * FROM drivers WHERE status = 'AVAILABLE'")
    fun getAvailableDrivers(): Flow<List<Driver>>

    @Query("SELECT * FROM drivers WHERE id = :id")
    suspend fun getDriverById(id: Long): Driver?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDriver(driver: Driver): Long

    @Update
    suspend fun updateDriver(driver: Driver)
}

@Dao
interface EscalationDao {
    @Query("SELECT * FROM escalations WHERE isResolved = 0 ORDER BY timestamp DESC")
    fun getUnresolvedEscalations(): Flow<List<Escalation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEscalation(escalation: Escalation)

    @Update
    suspend fun updateEscalation(escalation: Escalation)
}
