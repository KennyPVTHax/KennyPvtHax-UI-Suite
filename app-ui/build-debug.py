#!/usr/bin/env python3
"""Build a debug APK using official SDK tools, without a Gradle download.
Requires JDK17+, Android SDK platform35/build-tools35.0.0. Debug keys stay outside Git.
"""
import os,subprocess,zipfile,shutil,argparse
from pathlib import Path
p=argparse.ArgumentParser();p.add_argument('--sdk',default=os.environ.get('ANDROID_HOME',os.environ.get('ANDROID_SDK_ROOT','')));a=p.parse_args()
if not a.sdk:p.error('Set ANDROID_HOME or supply --sdk')
r=Path(__file__).resolve().parent;sdk=Path(a.sdk);ext='.exe' if os.name=='nt' else '';tools=sdk/'build-tools/35.0.0';android=sdk/'platforms/android-35/android.jar';out=r/'build/manual';out.mkdir(parents=True,exist_ok=True)
def run(*args):subprocess.run([str(x) for x in args],check=True)
for n in ('aapt2','d8','zipalign','apksigner'):
 if not (tools/(n+('.bat' if os.name=='nt' and n in ('d8','apksigner') else ext))).exists():raise SystemExit('Missing SDK tool '+n)
run(tools/('aapt2'+ext),'compile','--dir',r/'app/src/main/res','-o',out/'resources.zip')
manifest=(r/'app/src/main/AndroidManifest.xml').read_text().replace('<manifest ','<manifest package="com.kennypvthax.uisuite" android:versionCode="2" android:versionName="0.2-reference" ',1);manifest=manifest.replace('<application ', '<application android:debuggable="true" ',1);(out/'AndroidManifest.xml').write_text(manifest)
run(tools/('aapt2'+ext),'link','-o',out/'unsigned.apk','--manifest',out/'AndroidManifest.xml','-I',android,'--java',out/'generated','--min-sdk-version','26','--target-sdk-version','35',*(['-A',r/'app/src/main/assets'] if (r/'app/src/main/assets').is_dir() else []),out/'resources.zip')
classes=out/'classes';classes.mkdir(exist_ok=True)
# javac argfile avoids Windows/Unix command-line length and quoting differences.
sources=list((r/'app/src/main/java').rglob('*.java'))+list((out/'generated').rglob('*.java'));argfile=out/'javac.args';argfile.write_text('\n'.join('"'+str(x).replace('\\','/')+'"' for x in sources))
run('javac','--release','17','-classpath',android,'-d',classes,'@'+str(argfile))
with zipfile.ZipFile(out/'classes.jar','w') as z:
 for path in classes.rglob('*.class'):z.write(path,path.relative_to(classes))
run(tools/('d8.bat' if os.name=='nt' else 'd8'),'--lib',android,'--min-api','26','--output',out,out/'classes.jar')
with zipfile.ZipFile(out/'unsigned.apk','a') as z:z.write(out/'classes.dex','classes.dex')
run(tools/('zipalign'+ext),'-f','4',out/'unsigned.apk',out/'aligned.apk')
key=Path.home()/'.android/kenny-suite-debug.keystore';key.parent.mkdir(exist_ok=True)
if not key.exists():run('keytool','-genkeypair','-keystore',key,'-storepass','android','-keypass','android','-alias','androiddebugkey','-dname','CN=Android Debug,O=Android,C=US','-keyalg','RSA','-validity','10000')
apk=out/'KennyPvtHax-UI-Suite-debug.apk';run(tools/('apksigner.bat' if os.name=='nt' else 'apksigner'),'sign','--ks',key,'--ks-pass','pass:android','--out',apk,out/'aligned.apk');run(tools/('apksigner.bat' if os.name=='nt' else 'apksigner'),'verify','--verbose',apk)
print(apk)
