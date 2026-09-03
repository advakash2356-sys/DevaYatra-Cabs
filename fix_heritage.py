import re

with open('app/src/main/java/com/example/ui/HeritageSection.kt', 'r') as f:
    content = f.read()

# Make it more Shiva themed
content = content.replace('text = "🌸"', 'text = "ॐ"')
content = content.replace('text = if (isHi) "यात्रा सहायता एवं मार्गदर्शिका" else "Yatra Support & Quick Guide"', 'text = if (isHi) "बाबा धाम सहायता" else "Baba Dham Support"')

with open('app/src/main/java/com/example/ui/HeritageSection.kt', 'w') as f:
    f.write(content)
