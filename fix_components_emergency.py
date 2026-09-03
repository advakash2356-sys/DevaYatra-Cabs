import re

with open('app/src/main/java/com/example/ui/DashboardScreen.kt', 'r') as f:
    content = f.read()

content = content.replace('text = if (currentLang == "hi") "आपातकालीन" else "Emergency SOS",', 'text = if (currentLang == "hi") "आपातकालीन सहायता" else "Emergency SOS",')
content = content.replace('text = if (currentLang == "hi") "व्हाट्सएप सहायता" else "WhatsApp Help",', 'text = if (currentLang == "hi") "बाबा धाम WhatsApp" else "Baba Dham WhatsApp",')

with open('app/src/main/java/com/example/ui/DashboardScreen.kt', 'w') as f:
    f.write(content)
