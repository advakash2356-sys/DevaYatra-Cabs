import re

with open('app/src/main/java/com/example/ui/HeritageSection.kt', 'r') as f:
    content = f.read()

content = content.replace('color = Color.Gray', 'color = com.example.ui.theme.BhasmDark')

with open('app/src/main/java/com/example/ui/HeritageSection.kt', 'w') as f:
    f.write(content)
