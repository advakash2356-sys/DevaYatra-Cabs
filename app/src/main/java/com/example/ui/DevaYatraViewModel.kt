package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DevaYatraViewModel(private val repository: DevaYatraRepository) : ViewModel() {

    val currentLanguage = MutableStateFlow("en")

    fun toggleLanguage() {
        currentLanguage.value = if (currentLanguage.value == "en") "hi" else "en"
    }

    val allBookings = repository.allBookings.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val activeEscalations = repository.activeEscalations.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allDrivers = repository.allDrivers.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val availableDrivers = repository.availableDrivers.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    val completedCount = repository.completedCount.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val totalCommission = repository.totalCommission.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    fun addBooking(booking: Booking) {
        viewModelScope.launch {
            repository.insertBooking(booking)
        }
    }

    fun updateBooking(booking: Booking) {
        viewModelScope.launch {
            repository.updateBooking(booking)
        }
    }

    fun addDriver(driver: Driver) {
        viewModelScope.launch {
            repository.insertDriver(driver)
        }
    }

    fun updateDriver(driver: Driver) {
        viewModelScope.launch {
            repository.updateDriver(driver)
        }
    }

    fun resolveEscalation(bookingId: Long) {
        viewModelScope.launch {
            val booking = repository.getBookingById(bookingId)
            if (booking != null) {
                repository.updateBooking(booking.copy(escalationLevel = EscalationLevel.NORMAL))
            }
        }
    }

    // Initial data - kept empty to ensure only exact, codified owner data is used in production.
    fun seedData() {
        // No mock data generated. 
    }
}

class DevaYatraViewModelFactory(private val repository: DevaYatraRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DevaYatraViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DevaYatraViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
