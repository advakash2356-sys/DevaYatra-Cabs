import re

with open('app/src/main/java/com/example/ui/Utils.kt', 'r') as f:
    content = f.read()

content = content.replace('import android.net.Uri', 'import android.net.Uri\nimport android.util.Log\nimport android.widget.Toast')

content = re.sub(r'e\.printStackTrace\(\)', 'Log.e("IntentUtils", "Action failed", e)\n            Toast.makeText(context, "App not found or action failed", Toast.LENGTH_SHORT).show()', content)
content = re.sub(r'ex\.printStackTrace\(\)', 'Log.e("IntentUtils", "Fallback action failed", ex)\n                Toast.makeText(context, "Action failed", Toast.LENGTH_SHORT).show()', content)

with open('app/src/main/java/com/example/ui/Utils.kt', 'w') as f:
    f.write(content)
