#!/usr/bin/env python3
"""
Simple YAML validator for the repository.
Usage: python scripts/validate_yaml.py [path/to/file.yml]
If no path is provided, it validates the AsyncAPI file in docs/ by default.
"""
import sys
import traceback

try:
    import yaml
except Exception:
    print("MISSING_PYYAML")
    sys.exit(3)

def validate(path):
    try:
        with open(path, 'r', encoding='utf-8') as f:
            data = yaml.safe_load(f)
        # If parsing succeeded, print VALID and exit 0
        print('VALID')
        return 0
    except Exception as e:
        print('INVALID')
        print(type(e).__name__, str(e))
        traceback.print_exc()
        return 2

if __name__ == '__main__':
    path = sys.argv[1] if len(sys.argv) > 1 else r'c:\Users\Thomas\source\labyrinth-interface\docs\GameServer_asyncapi.yml'
    raise_code = validate(path)
    sys.exit(raise_code)
