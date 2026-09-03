import re

with open('app/src/main/java/com/example/ui/BookingDetailScreen.kt', 'r') as f:
    content = f.read()

content = content.replace(
    """                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                                    ) {
                                        Column {""",
    """                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {"""
)

with open('app/src/main/java/com/example/ui/BookingDetailScreen.kt', 'w') as f:
    f.write(content)
