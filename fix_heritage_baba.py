import re

with open('app/src/main/java/com/example/ui/HeritageSection.kt', 'r') as f:
    content = f.read()

# Update Darshan tips to be more Baba Dham oriented
content = content.replace('text = if (isHi) "केसरिया वस्त्र पहनें" else "Wear Saffron Clothes",', 'text = if (isHi) "जलार्पण मार्ग" else "Jalarpan Marg",')
content = content.replace('text = if (isHi) "दर्शन नियम" else "Darshan Tips",', 'text = if (isHi) "बाबा दर्शन" else "Baba Darshan",')
content = content.replace('color = SuccessGreen', 'color = Marigold')
content = content.replace('SuccessGreen', 'Marigold')
content = content.replace('SuccessGreen.copy(alpha = 0.04f)', 'Marigold.copy(alpha = 0.04f)')
content = content.replace('SuccessGreen.copy(alpha = 0.15f)', 'Marigold.copy(alpha = 0.15f)')

with open('app/src/main/java/com/example/ui/HeritageSection.kt', 'w') as f:
    f.write(content)
