import re

with open('app/src/main/res/values/strings.xml', 'r') as f:
    content = f.read()

content = content.replace('<string name="app_name">YatraSarthi</string>', '<string name="app_name">Baba Yatra</string>')

with open('app/src/main/res/values/strings.xml', 'w') as f:
    f.write(content)

with open('settings.gradle.kts', 'r') as f:
    content = f.read()

content = content.replace('rootProject.name = "YatraSarthi"', 'rootProject.name = "Baba Yatra"')

with open('settings.gradle.kts', 'w') as f:
    f.write(content)
