import re

with open('app/src/main/java/com/example/ui/DashboardScreen.kt', 'r') as f:
    content = f.read()

# Let's ensure the Kpi section and others use the proper DeepShivBlue or BloodRed.
content = content.replace('BhasmGray.copy(alpha = 0.5f)', 'BhasmGray')
content = content.replace('SuccessGreen.copy(alpha = 0.5f)', 'SuccessGreen')
content = content.replace('Marigold.copy(alpha = 0.5f)', 'Marigold')

with open('app/src/main/java/com/example/ui/DashboardScreen.kt', 'w') as f:
    f.write(content)

