package com.example.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast

object IntentUtils {
    private fun executeIntent(context: Context, intent: Intent, fallbackMsg: String = "Action failed") {
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e("IntentUtils", "Intent execution failed: ${intent.action}", e)
            Toast.makeText(context, fallbackMsg, Toast.LENGTH_SHORT).show()
        }
    }

    fun dialPhoneNumber(context: Context, phone: String) {
        executeIntent(context, Intent(Intent.ACTION_DIAL).apply { data = Uri.parse("tel:$phone") }, "Dialer not found")
    }

    fun openWhatsAppChat(context: Context, phone: String, message: String = "") {
        val cleanPhone = phone.replace(Regex("[^0-9]"), "")
        val formattedPhone = if (cleanPhone.length == 10) "91$cleanPhone" else cleanPhone
        val uriStr = "https://wa.me/$formattedPhone" + if (message.isNotEmpty()) "?text=${Uri.encode(message)}" else ""
        executeIntent(context, Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(uriStr) }, "WhatsApp not found")
    }

    fun openInGoogleMaps(context: Context, location: String) {
        val gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(location))
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, gmmIntentUri).apply { setPackage("com.google.android.apps.maps") })
        } catch (e: Exception) {
            executeIntent(context, Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=" + Uri.encode(location))), "Maps not found")
        }
    }

    fun shareText(context: Context, text: String, title: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        executeIntent(context, Intent.createChooser(shareIntent, title), "Share failed")
    }

    fun openUPIPayment(context: Context, upiId: String, name: String, amount: String, note: String = "Yatra Booking") {
        val uriStr = "upi://pay?pa=${Uri.encode(upiId)}&pn=${Uri.encode(name)}&am=${Uri.encode(amount)}&cu=INR&tn=${Uri.encode(note)}"
        executeIntent(context, Intent.createChooser(Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(uriStr) }, "Pay with UPI"), "No UPI app found")
    }
}
