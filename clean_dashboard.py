import re

with open('app/src/main/java/com/example/ui/DashboardScreen.kt', 'r') as f:
    content = f.read()

# Remove the escalations and recent activity sections from DashboardScreen
pattern = r"            if \(activeEscalations\.isNotEmpty\(\)\) \{.*?(?=            item \{\n                Spacer\(modifier = Modifier\.height\(16\.dp\)\)\n            \}) "
# Wait, this regex might be tricky. Let's do it with sed or string manipulation
