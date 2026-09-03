import re

with open('app/src/main/java/com/example/ui/DashboardScreen.kt', 'r') as f:
    content = f.read()

# Update KpiCard text color
content = content.replace('color = MaterialTheme.colorScheme.onSurface', 'color = MaterialTheme.colorScheme.onSurface')
content = content.replace('color = Color.Gray', 'color = BhasmDark')

# Update SectionTitle color
content = content.replace('color = MaterialTheme.colorScheme.onBackground', 'color = MaterialTheme.colorScheme.onBackground')

with open('app/src/main/java/com/example/ui/DashboardScreen.kt', 'w') as f:
    f.write(content)
