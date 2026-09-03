import re

with open('app/src/main/res/values/strings.xml', 'r') as f:
    content = f.read()

content = content.replace('YatraSarthi', 'Baba Yatra')

with open('app/src/main/res/values/strings.xml', 'w') as f:
    f.write(content)
