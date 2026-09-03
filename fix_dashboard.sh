sed -i '166,177c\
            item {\
                SectionTitle(translate("recent_activity", viewModel))\
            }\
            if (bookings.isEmpty()) {\
                item {\
                    EmptyStateView(\
                        title = if (currentLang == "hi") "कोई बुकिंग नहीं" else "No recent activity",\
                        message = if (currentLang == "hi") "शुरू करने के लिए एक नई बुकिंग बनाएं" else "Create a new booking to get started"\
                    )\
                }\
            } else {\
                items(bookings.take(5)) { booking ->\
                    SummaryBookingCard(booking, onClick = { onBookingClick(booking.id) }, viewModel = viewModel)\
                }\
            }\
            item {\
                Spacer(modifier = Modifier.height(16.dp))\
            }\
        }\
    }\
' app/src/main/java/com/example/ui/DashboardScreen.kt
