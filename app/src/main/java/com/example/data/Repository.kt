package com.example.data

import kotlinx.coroutines.flow.Flow

class DevaYatraRepository(
    private val bookingDao: BookingDao,
    private val driverDao: DriverDao,
    private val escalationDao: EscalationDao
) {
    val allBookings: Flow<List<Booking>> = bookingDao.getAllBookings()
    val activeEscalations: Flow<List<Booking>> = bookingDao.getActiveEscalations()
    val allDrivers: Flow<List<Driver>> = driverDao.getAllDrivers()
    val availableDrivers: Flow<List<Driver>> = driverDao.getAvailableDrivers()
    val unresolvedEscalations: Flow<List<Escalation>> = escalationDao.getUnresolvedEscalations()
    
    val completedCount: Flow<Int> = bookingDao.getCompletedCount()
    val totalCommission: Flow<Double?> = bookingDao.getTotalCommission()

    suspend fun insertBooking(booking: Booking) = bookingDao.insertBooking(booking)
    suspend fun updateBooking(booking: Booking) = bookingDao.updateBooking(booking)
    suspend fun getBookingById(id: Long) = bookingDao.getBookingById(id)

    suspend fun insertDriver(driver: Driver) = driverDao.insertDriver(driver)
    suspend fun updateDriver(driver: Driver) = driverDao.updateDriver(driver)
    suspend fun getDriverById(id: Long) = driverDao.getDriverById(id)

    suspend fun insertEscalation(escalation: Escalation) = escalationDao.insertEscalation(escalation)
    suspend fun updateEscalation(escalation: Escalation) = escalationDao.updateEscalation(escalation)
}
