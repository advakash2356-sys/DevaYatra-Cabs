import re

with open('app/src/main/java/com/example/ui/DashboardScreen.kt', 'r') as f:
    content = f.read()

content = content.replace('if (borderColor == Marigold.copy(alpha = 0.5f)) Marigold else if (borderColor == SuccessGreen.copy(alpha = 0.5f)) SuccessGreen else BhasmDark', 'if (borderColor == BhasmGray.copy(alpha = 0.5f)) BhasmGray else if (borderColor == SuccessGreen.copy(alpha = 0.5f)) SuccessGreen else Marigold')

with open('app/src/main/java/com/example/ui/DashboardScreen.kt', 'w') as f:
    f.write(content)
