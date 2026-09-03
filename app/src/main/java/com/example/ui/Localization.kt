package com.example.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

object Localization {
    private val translations = mapOf(
        "en" to mapOf(
            "app_name" to "Baba Yatra",
            "dashboard_title" to "Raja Bhaiya Dashboard",
            "welcome" to "🙏 JAI BABA BAIDYANATH!",
            "kpi_total" to "Total Bookings",
            "kpi_done" to "Completed",
            "kpi_earned" to "Earned",
            "needs_attention" to "🚨 Needs Attention",
            "recent_activity" to "Recent Activity",
            "heritage_title" to "The Legacy of Rajkumar Shukla",
            "heritage_subtitle" to "Proud Descendants of a Freedom Legend",
            "heritage_desc" to "YatraSarthi is guided by the values of Rajkumar Shukla, the man who brought Mahatma Gandhi to Champaran. Our commitment to service and integrity is rooted in this glorious Indian heritage.",
            "nav_home" to "Home",
            "nav_bookings" to "Bookings",
            "nav_drivers" to "Drivers",
            "nav_alerts" to "Alerts",
            
            // Bookings Screen
            "manage_bookings" to "Manage Bookings",
            "no_bookings" to "No bookings found",
            "tab_all" to "All",
            "tab_pending" to "Pending",
            "tab_active" to "Active",
            "tab_done" to "Done",
            "new_booking" to "New Booking",
            "add_booking_title" to "Create New Booking",
            "cust_name" to "Customer Name",
            "cust_phone" to "Customer Phone",
            "pickup_loc" to "Pickup Location",
            "drop_loc" to "Destination",
            "travel_date" to "Travel Date",
            "travel_time" to "Travel Time",
            "num_passengers" to "Passengers",
            "quoted_fare" to "Quoted Fare (₹)",
            "vehicle_type" to "Vehicle Type",
            "cancel" to "Cancel",
            "save" to "Save",
            
            // Drivers Screen
            "driver_network" to "Driver Network",
            "no_drivers" to "No drivers registered",
            "add_driver_title" to "Register New Driver",
            "driver_name" to "Driver Name",
            "driver_phone" to "Driver Phone",
            "vehicle_num" to "Vehicle Number",
            "due_commission" to "Due: ₹",
            "settled" to "Settle Due Commission",
            "change_status" to "Change Status",
            "status_avail" to "AVAILABLE",
            "status_busy" to "BUSY",
            "status_offline" to "OFFLINE",
            
            // Booking Detail Screen
            "booking_details" to "Booking Details",
            "cust_details" to "Customer Details",
            "trip_details" to "Trip Details",
            "fare_commission" to "Fare & Commission",
            "comm_rate" to "Commission (15%)",
            "cust_rating" to "Customer Rating",
            "assign_driver" to "Assign Driver",
            "mark_completed" to "Mark as Completed",
            "not_found" to "Booking not found",
            "escalate_alert" to "Trigger RED Alert Escalation",
            "de_escalate" to "Clear RED Alert",
            "whatsapp_actions" to "WhatsApp Automation Actions",
            "notify_driver_wa" to "Notify Driver via WhatsApp",
            "notify_customer_wa" to "Notify Customer via WhatsApp",
            "driver_details" to "Driver Details",
            "assigned_driver" to "Assigned Driver",
            
            // Dialogs & Actions
            "select_driver" to "Select Driver from Network",
            "no_drivers_avail" to "No available drivers found! Register or update drivers first.",

            // Redesign pilgrim additions
            "yatra_quick_booking" to "Yatra Quick Bookings (Pre-set Routes)",
            "jsh_to_mandir" to "Jasidih ⇄ Baba Mandir",
            "deoghar_to_basuki" to "Deoghar ⇄ Basukinath",
            "sightseeing" to "Deoghar Local Darshan",
            "darshan_guide" to "Baba Baidyanath Dham Yatra Guide",
            "darshan_tab_heritage" to "Our Heritage",
            "darshan_tab_rules" to "Darshan Tips & Rules",
            "darshan_tab_help" to "Yatra Helpline",
            "copy_slip" to "Copy Yatra Slip Receipt",
            "slip_copied" to "Yatra Slip Copied to Clipboard!",
            "all_clear" to "All systems clear. No active alerts.",
            
            // Real-Time Statistics & Navigation Rail
            "live_monitor" to "LIVE TELEMETRY",
            "active_bookings_title" to "Active Bookings Radar",
            "pending_escalations_title" to "Pending Driver Escalations",
            "live_status_active" to "Active Trips",
            "live_status_pending_assign" to "Unassigned",
            "live_status_in_progress" to "In Transit",
            "live_status_assigned" to "Assigned",
            "pilgrims_en_route" to "Pilgrims En Route",
            "active_fare_volume" to "Live Active Volume",
            "fleet_ready_capacity" to "Available Fleet",
            "resolve_alert_action" to "Resolve Alert",
            "alert_resolved_toast" to "Escalation resolved successfully!",
            "no_pending_escalations" to "No pending escalations. All drivers operating smoothly.",
            "urgent_attention_needed" to "Immediate Dispatch Action Required",
            "view_all_active" to "View All Active",
            "view_all_alerts" to "View All Alerts",
            "dispatch_now" to "Assign Driver",
            "nav_rail_title" to "Baba Yatra",
            "live_pulse" to "LIVE MONITOR"
        ),
        "hi" to mapOf(
            "app_name" to "यात्रासार्थी",
            "dashboard_title" to "राजा भैया डैशबोर्ड",
            "welcome" to "🙏 जय बाबा बैद्यनाथ!",
            "kpi_total" to "कुल बुकिंग",
            "kpi_done" to "पूरा हुआ",
            "kpi_earned" to "कमाई",
            "needs_attention" to "🚨 ध्यान दें",
            "recent_activity" to "हाल की गतिविधि",
            "heritage_title" to "राजकुमार शुक्ल की विरासत",
            "heritage_subtitle" to "स्वतंत्रता सेनानी के गर्वित वंशज",
            "heritage_desc" to "यात्रासार्थी राजकुमार शुक्ल के मूल्यों से प्रेरित है, जिन्होंने महात्मा गांधी को चंपारण लाया था। सेवा और ईमानदारी के प्रति हमारी प्रतिबद्धता इसी गौरवशाली भारतीय विरासत में निहित है।",
            "nav_home" to "होम",
            "nav_bookings" to "बुकिंग्स",
            "nav_drivers" to "ड्राइवर्स",
            "nav_alerts" to "अलर्ट्स",
            
            // Bookings Screen
            "manage_bookings" to "बुकिंग प्रबंधित करें",
            "no_bookings" to "कोई बुकिंग नहीं मिली",
            "tab_all" to "सभी",
            "tab_pending" to "लंबित",
            "tab_active" to "सक्रिय",
            "tab_done" to "पूरा हुआ",
            "new_booking" to "नई बुकिंग",
            "add_booking_title" to "नई बुकिंग बनाएं",
            "cust_name" to "ग्राहक का नाम",
            "cust_phone" to "ग्राहक का फोन",
            "pickup_loc" to "पिकअप स्थान",
            "drop_loc" to "गंतव्य स्थान",
            "travel_date" to "यात्रा की तिथि",
            "travel_time" to "यात्रा का समय",
            "num_passengers" to "यात्रियों की संख्या",
            "quoted_fare" to "तय किराया (₹)",
            "vehicle_type" to "गाड़ी का प्रकार",
            "cancel" to "रद्द करें",
            "save" to "सहेजें",
            
            // Drivers Screen
            "driver_network" to "ड्राइवर नेटवर्क",
            "no_drivers" to "कोई ड्राइवर पंजीकृत नहीं है",
            "add_driver_title" to "नया ड्राइवर पंजीकृत करें",
            "driver_name" to "ड्राइवर का नाम",
            "driver_phone" to "ड्राइवर का फोन",
            "vehicle_num" to "गाड़ी का नंबर",
            "due_commission" to "बकाया कमीशन: ₹",
            "settled" to "बकाया कमीशन चुकाएं",
            "change_status" to "स्थिति बदलें",
            "status_avail" to "उपलब्ध",
            "status_busy" to "व्यस्त",
            "status_offline" to "ऑफ़लाइन",
            
            // Booking Detail Screen
            "booking_details" to "बुकिंग विवरण",
            "cust_details" to "ग्राहक का विवरण",
            "trip_details" to "यात्रा का विवरण",
            "fare_commission" to "किराया और कमीशन",
            "comm_rate" to "कमिशन (15%)",
            "cust_rating" to "ग्राहक रेटिंग",
            "assign_driver" to "ड्राइवर नियुक्त करें",
            "mark_completed" to "यात्रा पूर्ण चिह्नित करें",
            "not_found" to "बुकिंग नहीं मिली",
            "escalate_alert" to "त्वरित रेड अलर्ट सक्रिय करें",
            "de_escalate" to "रेड अलर्ट हटाएं",
            "whatsapp_actions" to "व्हाट्सएप ऑटोमेशन एक्शन",
            "notify_driver_wa" to "व्हाट्सएप द्वारा ड्राइवर को सूचित करें",
            "notify_customer_wa" to "व्हाट्सएप द्वारा ग्राहक को सूचित करें",
            "driver_details" to "ड्राइवर का विवरण",
            "assigned_driver" to "नियुक्त ड्राइवर",
            
            // Dialogs & Actions
            "select_driver" to "नेटवर्क से ड्राइवर चुनें",
            "no_drivers_avail" to "कोई उपलब्ध ड्राइवर नहीं मिला! पहले ड्राइवर पंजीकृत करें या स्थिति अपडेट करें।",

            // Redesign pilgrim additions
            "yatra_quick_booking" to "त्वरित यात्रा बुकिंग (शॉर्टकट)",
            "jsh_to_mandir" to "जसीडीह स्टेशन ⇄ बाबा मंदिर",
            "deoghar_to_basuki" to "देवघर ⇄ बासुकीनाथ धाम",
            "sightseeing" to "देवघर लोकल दर्शन यात्रा",
            "darshan_guide" to "बाबा बैद्यनाथ धाम यात्रा मार्गदर्शिका",
            "darshan_tab_heritage" to "गौरवशाली विरासत",
            "darshan_tab_rules" to "दर्शन नियम और सलाह",
            "darshan_tab_help" to "यात्रा सहायता हेल्पलाइन",
            "copy_slip" to "बुकिंग रसीद पर्ची कॉपी करें",
            "slip_copied" to "बुकिंग रसीद कॉपी हो गई है!",
            "all_clear" to "कोई नया अलर्ट नहीं है। सब ठीक है।",

            // Real-Time Statistics & Navigation Rail (Hindi)
            "live_monitor" to "लाइव टेलीमेट्री",
            "active_bookings_title" to "सक्रिय बुकिंग रडार",
            "pending_escalations_title" to "लंबित ड्राइवर अलर्ट और शिकायतें",
            "live_status_active" to "सक्रिय यात्राएं",
            "live_status_pending_assign" to "ड्राइवर की आवश्यकता",
            "live_status_in_progress" to "सफ़र में",
            "live_status_assigned" to "ड्राइवर नियुक्त",
            "pilgrims_en_route" to "सफ़र में श्रद्धालु",
            "active_fare_volume" to "सक्रिय यात्रा मूल्य",
            "fleet_ready_capacity" to "उपलब्ध गाड़ियां",
            "resolve_alert_action" to "अलर्ट हल करें",
            "alert_resolved_toast" to "शिकायत का सफल समाधान किया गया!",
            "no_pending_escalations" to "कोई लंबित अलर्ट नहीं है। सभी गाड़ियां सुचारू रूप से चल रही हैं।",
            "urgent_attention_needed" to "तत्काल ड्राइवर प्रेषण व सहायता आवश्यक",
            "view_all_active" to "सभी सक्रिय देखें",
            "view_all_alerts" to "सभी अलर्ट देखें",
            "dispatch_now" to "ड्राइवर नियुक्त करें",
            "nav_rail_title" to "बाबा यात्रा",
            "live_pulse" to "लाइव मॉनिटर"
        )
    )

    fun get(key: String, lang: String): String {
        return translations[lang]?.get(key) ?: translations["en"]?.get(key) ?: key
    }
}

@Composable
fun translate(key: String, viewModel: DevaYatraViewModel): String {
    val lang by viewModel.currentLanguage.collectAsState()
    return Localization.get(key, lang)
}
