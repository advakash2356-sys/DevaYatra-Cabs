import re

with open('app/src/main/java/com/example/ui/DashboardScreen.kt', 'r') as f:
    content = f.read()

# Fix EscalationCard
content = content.replace(
    """            Spacer(modifier = Modifier.width(12.dp))
            Column {""",
    """            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {"""
)

# Fix SummaryBookingCard
content = content.replace(
    """                Spacer(modifier = Modifier.width(12.dp))
                
                Column {""",
    """                Spacer(modifier = Modifier.width(12.dp))
                
                Column(modifier = Modifier.weight(1f)) {"""
)

# Wait, let's just use regex to replace `Column {` if it follows `Spacer(modifier = Modifier.width(12.dp))`
pattern = r"Spacer\(modifier = Modifier.width\(12.dp\)\)\s*Column \{"
replacement = r"Spacer(modifier = Modifier.width(12.dp))\n                Column(modifier = Modifier.weight(1f)) {"
content = re.sub(pattern, replacement, content)

with open('app/src/main/java/com/example/ui/DashboardScreen.kt', 'w') as f:
    f.write(content)
