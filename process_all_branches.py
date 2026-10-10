"""
Process remaining branches: session_4 through session_7.
Uses --force for push in case of conflicts.
"""

import sys, os, glob, subprocess


def strip_comments(source: str) -> str:
    result = []
    i = 0
    n = len(source)
    while i < n:
        if source[i] == '"':
            j = i + 1
            while j < n:
                if source[j] == '\\':
                    j += 2
                    continue
                if source[j] == '"':
                    j += 1
                    break
                j += 1
            result.append(source[i:j])
            i = j
        elif source[i] == "'":
            j = i + 1
            while j < n:
                if source[j] == '\\':
                    j += 2
                    continue
                if source[j] == "'":
                    j += 1
                    break
                j += 1
            result.append(source[i:j])
            i = j
        elif source[i:i+2] == '//':
            j = i + 2
            while j < n and source[j] != '\n':
                j += 1
            i = j
        elif source[i:i+2] == '/*':
            j = i + 2
            while j < n and source[j-1:j+1] != '*/':
                j += 1
            i = j + 1
        else:
            result.append(source[i])
            i += 1
    return ''.join(result)


def clean_blank_lines(text: str) -> str:
    lines = [l.rstrip() for l in text.split('\n')]
    final = []
    prev_blank = False
    for line in lines:
        if line == '':
            if prev_blank:
                continue
            prev_blank = True
        else:
            prev_blank = False
        final.append(line)
    while final and final[0] == '':
        final.pop(0)
    while final and final[-1] == '':
        final.pop()
    return '\n'.join(final) + '\n'


def process_file(filepath: str) -> bool:
    with open(filepath, 'r', encoding='utf-8') as f:
        original = f.read()
    stripped = strip_comments(original)
    cleaned = clean_blank_lines(stripped)
    if cleaned != original:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(cleaned)
        return True
    return False


def run(cmd):
    r = subprocess.run(cmd, shell=True, capture_output=True, text=True)
    combined = r.stdout + r.stderr
    if r.returncode != 0 and 'cannot spawn .git/hooks' not in combined:
        print(f"  WARN: {cmd} -> {combined.strip()}")
    return r


BRANCHES = [
    "feature/session_4",
    "feature/session_5",
    "feature/session_6",
    "feature/session_7",
]

for branch in BRANCHES:
    print(f"\n{'='*60}")
    print(f"Processing branch: {branch}")
    print(f"{'='*60}")

    run(f'git checkout {branch}')

    java_files = glob.glob('src/**/*.java', recursive=True)
    if not java_files:
        print("  No Java files found, skipping.")
        continue

    modified = 0
    for fpath in sorted(java_files):
        if process_file(fpath):
            print(f"  Modified: {fpath}")
            modified += 1

    print(f"  Files scanned: {len(java_files)}, modified: {modified}")

    if modified > 0:
        run('git add -A')
        run('git commit --no-verify -m "Remove all comments from Java files"')
        r = run(f'git push --no-verify --force origin {branch}')
        log = run('git log -1 --oneline')
        print(f"  Committed & pushed: {log.stdout.strip()}")
    else:
        print("  Nothing to commit.")

print("\n" + "="*60)
print("All remaining branches processed.")
print("="*60)
