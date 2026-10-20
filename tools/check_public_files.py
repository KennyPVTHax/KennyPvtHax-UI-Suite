#!/usr/bin/env python3
"""Reject obvious credential material from staged files; report paths only."""
import re,subprocess
from pathlib import Path
r=Path(__file__).resolve().parents[1]
paths=subprocess.check_output(['git','diff','--cached','--name-only','--diff-filter=ACM','-z'],cwd=r).decode().split('\0');bad=[]
patterns=[rb'gh[pousr]_[A-Za-z0-9]{30,}',rb'github_pat_[A-Za-z0-9_]{30,}',rb'-----BEGIN (?:RSA |EC |OPENSSH )?PRIVATE KEY-----',rb'AKIA[0-9A-Z]{16}']
for name in paths:
 if not name:continue
 data=(r/name).read_bytes()
 if any(re.search(p,data) for p in patterns):bad.append(name)
assert not bad,'Potential credentials in: '+', '.join(bad)
print('PASS credential-pattern scan of',sum(bool(p) for p in paths),'staged files (does not prove absence of every secret)')
