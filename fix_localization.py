import re

with open('app/src/main/java/com/example/ui/Localization.kt', 'r') as f:
    content = f.read()

content = content.replace('"app_name" to "YatraSarthi",', '"app_name" to "Baba Yatra",')
content = content.replace('"app_name" to "यात्रा सारथी",', '"app_name" to "बाबा यात्रा",')

with open('app/src/main/java/com/example/ui/Localization.kt', 'w') as f:
    f.write(content)
