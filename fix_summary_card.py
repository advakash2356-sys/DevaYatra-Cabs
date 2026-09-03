import re

with open('app/src/main/java/com/example/ui/DashboardScreen.kt', 'r') as f:
    content = f.read()

pattern = r"            Row\(\n                verticalAlignment = Alignment\.CenterVertically,\n                horizontalArrangement = Arrangement\.spacedBy\(8\.dp\)\n            \) \{\n                Column\(horizontalAlignment = Alignment\.End\) \{.*?                \}\n            \}\n        \}\n    \}\n\}"

new_card = """            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "₹${booking.quotedFare.toInt()}",
                        fontWeight = FontWeight.Black,
                        style = MaterialTheme.typography.titleMedium,
                        color = Saffron
                    )
                    Text(
                        text = if (booking.status == BookingStatus.PENDING) (if (currentLang == "hi") "लंबित" else "Pending") else if (booking.status == BookingStatus.DONE) (if (currentLang == "hi") "पूर्ण" else "Done") else (if (currentLang == "hi") "सक्रिय" else "Active"),
                        style = MaterialTheme.typography.labelSmall,
                        color = statusColor,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        shape = androidx.compose.foundation.shape.CircleShape,
                        color = Color(0xFF1976D2), // Highly visible rich blue for calls
                        contentColor = Color.White,
                        modifier = Modifier
                            .size(36.dp)
                            .clickable { IntentUtils.dialPhoneNumber(context, booking.customerPhone) }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = "Call Passenger",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    
                    Surface(
                        shape = androidx.compose.foundation.shape.CircleShape,
                        color = Color(0xFF2E7D32), // Deep WhatsApp style green
                        contentColor = Color.White,
                        modifier = Modifier
                            .size(36.dp)
                            .clickable { IntentUtils.openWhatsAppChat(context, booking.customerPhone, "Jai Baba Baidyanath!") }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Message,
                                contentDescription = "WhatsApp Passenger",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}"""

content = re.sub(pattern, new_card, content, flags=re.DOTALL)

with open('app/src/main/java/com/example/ui/DashboardScreen.kt', 'w') as f:
    f.write(content)
