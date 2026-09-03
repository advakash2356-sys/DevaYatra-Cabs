import re

with open('app/src/main/java/com/example/ui/DashboardScreen.kt', 'r') as f:
    lines = f.readlines()

for i, line in enumerate(lines):
    if "HeritageSectionLocalized(viewModel)" in line:
        # i is the line with HeritageSectionLocalized
        # The next line should be '            }'
        # And next should be '        }'
        # And next should be '    }'
        lines[i+1] = "            }\n"
        lines[i+2] = "        }\n"
        lines[i+3] = "    }\n"
        break

with open('app/src/main/java/com/example/ui/DashboardScreen.kt', 'w') as f:
    f.writelines(lines)
