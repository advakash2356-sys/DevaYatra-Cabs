import re

with open('app/src/main/java/com/example/ui/DashboardScreen.kt', 'r') as f:
    content = f.read()

# Add a subtle background image or color gradient to the whole screen if possible, but for now we'll rely on the theme's DeepShivBlue
# Let's adjust the KpiCard border condition to match the new alphas
content = content.replace('if (borderColor == Saffron.copy(alpha = 0.3f))', 'if (borderColor == Saffron.copy(alpha = 0.5f))')
content = content.replace('else if (borderColor == SuccessGreen.copy(alpha = 0.3f))', 'else if (borderColor == SuccessGreen.copy(alpha = 0.5f))')

with open('app/src/main/java/com/example/ui/DashboardScreen.kt', 'w') as f:
    f.write(content)
