import re

with open('app/src/main/java/com/example/ui/DashboardScreen.kt', 'r') as f:
    content = f.read()

# I want to add some cosmic/bhasm flair to the DashboardScreen itself if possible.
# Currently the screen background is handled by Scaffold in DevaYatraTheme.

content = content.replace('text = if (isHi) "📍 बाबा नगरी देवघर कैब सेवा" else "📍 Baidyanath Dham Cab Service"', 'text = if (isHi) "🔱 बाबा धाम की सेवा में" else "🔱 In the Service of Baba Dham"')

with open('app/src/main/java/com/example/ui/DashboardScreen.kt', 'w') as f:
    f.write(content)

