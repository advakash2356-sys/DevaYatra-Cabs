import re

with open('app/src/main/java/com/example/ui/DashboardScreen.kt', 'r') as f:
    content = f.read()

content = content.replace('tint = if (borderColor == Saffron) Saffron else if (borderColor == SuccessGreen) SuccessGreen else Color.Gray', 'tint = if (borderColor == Saffron.copy(alpha = 0.5f)) Saffron else if (borderColor == SuccessGreen.copy(alpha = 0.5f)) SuccessGreen else BhasmDark')

with open('app/src/main/java/com/example/ui/DashboardScreen.kt', 'w') as f:
    f.write(content)
