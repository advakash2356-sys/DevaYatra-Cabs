import re

with open('app/src/main/java/com/example/ui/DashboardScreen.kt', 'r') as f:
    content = f.read()

# Re-adjust KPI card borders back to something more subtle, but keep the theme colors
content = content.replace('borderColor = BhasmGray,', 'borderColor = BhasmGray.copy(alpha = 0.5f),')
content = content.replace('borderColor = SuccessGreen,', 'borderColor = SuccessGreen.copy(alpha = 0.5f),')
content = content.replace('borderColor = Saffron,', 'borderColor = Saffron.copy(alpha = 0.5f),')

# Ensure the app background uses DeepShivBlue as set in Theme.kt
# The main app scaffold is transparent so it will pick up the theme background

with open('app/src/main/java/com/example/ui/DashboardScreen.kt', 'w') as f:
    f.write(content)

