#!/usr/bin/env python3
"""Host checks for screen contracts; Android behavior is tested separately."""
from pathlib import Path
import subprocess,tempfile,xml.etree.ElementTree as E,re
r=Path(__file__).resolve().parents[1];src=r/'app-ui/app/src/main/java/com/kennypvthax/uisuite';res=r/'app-ui/app/src/main/res'
with tempfile.TemporaryDirectory() as tmp:
 subprocess.run(['javac','-d',tmp,*map(str,[src/'AuthProvider.java',src/'AuthController.java',src/'DebugPendingAuthProvider.java',r/'app-ui/tests/AuthControllerCheck.java'])],check=True)
 subprocess.run(['java','-cp',tmp,'com.kennypvthax.uisuite.AuthControllerCheck'],check=True)
base={x.attrib['name'] for x in E.parse(res/'values/strings.xml').getroot()}
for locale in ['values-zh','values-hi','values-ar']:
 rows=list(E.parse(res/locale/'strings.xml').getroot());assert {x.attrib['name'] for x in rows}==base
 assert all(x.text and x.text.strip() for x in rows)
assert 'AES/GCM/NoPadding' in (src/'CredentialVault.java').read_text() and 'AndroidKeyStore' in (src/'CredentialVault.java').read_text()
assert 'setSaveEnabled(false)' in (src/'LicenseField.java').read_text()
manifest=E.parse(r/'app-ui/app/src/main/AndroidManifest.xml').getroot();assert not any(x.tag=='uses-permission' and 'INTERNET' in str(x.attrib) for x in manifest)
body=(r/'menu-ui/jni/main.cpp').read_text();assert len(re.findall(r'KennyControls::(?:Checkbox|SliderFloat|SliderInt)\("[^\"]+"[^;\n]*\);',body))==37
print('PASS four complete localization bundles, encrypted storage/input contracts, offline backend boundary and all 37 original menu bindings')
