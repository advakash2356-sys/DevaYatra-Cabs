import re

with open('app/src/main/java/com/example/ui/PrivacyPolicyScreen.kt', 'r') as f:
    content = f.read()

pattern = r'            Text\(\s*text = "Note: Exercising this right will remove your consent'
replacement = """            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Updated with YatraSarthi. Created by Akash Sync Agent",
                style = MaterialTheme.typography.bodySmall,
                color = androidx.compose.ui.graphics.Color.Gray,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )
            Text(
                text = "Note: Exercising this right will remove your consent"""

new_content = re.sub(pattern, replacement, content)
with open('app/src/main/java/com/example/ui/PrivacyPolicyScreen.kt', 'w') as f:
    f.write(new_content)
