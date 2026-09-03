import re

with open('app/src/main/java/com/example/ui/DashboardScreen.kt', 'r') as f:
    content = f.read()

# I will just use regex to extract the items and put them back in order.
# Items:
# 1. HeaderSection
# 2. Emergency Row
# 3. KpiSection
# 4. YatraQuickBookingSection
# 5. HeritageSectionLocalized

pattern_lazy_column = r"LazyColumn\([\s\S]*?verticalArrangement = Arrangement\.spacedBy\(20\.dp\)\n        \) \{([\s\S]*?)\n        \}\n    \}"
match = re.search(pattern_lazy_column, content)
if match:
    items_content = match.group(1)
    
    # Split by "            item {"
    items = items_content.split("            item {")
    items = [i for i in items if i.strip()]
    
    header = "            item {" + items[0]
    emergency = "            item {" + items[1]
    kpi = "            item {" + items[2]
    quick_booking = "            item {" + items[3]
    heritage = "            item {" + items[4]
    
    new_items_content = "\n" + header + quick_booking + kpi + emergency + heritage
    new_lazy_column = "LazyColumn(\n            modifier = Modifier\n                .fillMaxSize()\n                .padding(innerPadding)\n                .padding(horizontal = 16.dp),\n            verticalArrangement = Arrangement.spacedBy(20.dp)\n        ) {" + new_items_content + "\n        }\n    }"
    
    content = content.replace(match.group(0), new_lazy_column)
    with open('app/src/main/java/com/example/ui/DashboardScreen.kt', 'w') as f:
        f.write(content)
    print("Success reordering DashboardScreen")
else:
    print("Could not find LazyColumn")
