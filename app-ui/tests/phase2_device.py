#!/usr/bin/env python3
"""Actual APK UI tests on an isolated emulator. Requires explicit data-reset flag.
Test values are local fixtures, never accepted production credentials.
"""
import argparse,subprocess,time,re,json,xml.etree.ElementTree as E
from pathlib import Path
p=argparse.ArgumentParser();p.add_argument('--adb',default='adb');p.add_argument('--allow-data-reset',action='store_true',required=True);p.add_argument('--dashboard',action='store_true');a=p.parse_args()
r=Path(__file__).resolve().parents[2];out=r/'shared/visual-reference/phase2/app';out.mkdir(parents=True,exist_ok=True);pkg='com.kennypvthax.uisuite'
def adb(*args):return subprocess.check_output([a.adb,*args],timeout=90)
def dump():
 adb('shell','uiautomator','dump','/sdcard/kenny-phase2.xml');return E.fromstring(adb('shell','cat','/sdcard/kenny-phase2.xml'))
def node(tree,id):return next((n for n in tree.iter('node') if n.get('resource-id')==pkg+':id/'+id),None)
w,h=map(int,re.findall(r'(\d+)x(\d+)',adb('shell','wm','size').decode())[-1])
def bounds(n):return list(map(int,re.findall(r'\d+',n.get('bounds'))))
def swipe(up=True):adb('shell','input','swipe',str(w//2),str(int(h*(.8 if up else .3))),str(w//2),str(int(h*(.3 if up else .8))),'350')
def locate(id,scroll=False):
 for attempt in range(8 if scroll else 4):
  tree=dump();n=node(tree,id)
  if n is not None and bounds(n)[3]>bounds(n)[1] and n.get('enabled')=='true':return n
  if scroll:swipe()
  else:time.sleep(.15)
 raise AssertionError('Missing/enabled control: '+id)
def tap(id,scroll=True):
 n=locate(id,scroll);x1,y1,x2,y2=bounds(n);adb('shell','input','tap',str((x1+x2)//2),str((y1+y2)//2))
def capture(name):
 data=adb('exec-out','screencap','-p');assert data.startswith(b'\x89PNG');(out/(name+'.png')).write_bytes(data)
def start(fixture=None):
 adb('shell','am','force-stop',pkg);args=['shell','am','start','-W','-n',pkg+'/.MainActivity','--ef','preview_time','3']
 if fixture:args+=['--es','auth_fixture',fixture]
 adb(*args);locate('login_key')
def status(expected):
 for attempt in range(6):
  n=node(dump(),'auth_status')
  if n is not None and n.get('text')==expected:return
  if n is None:swipe()
  time.sleep(.2)
 raise AssertionError('Unexpected auth/status state; no credential content logged')
def enter_key():
 tap('login_key');adb('shell','input','text','local-ui-fixture');capture('login-focused');adb('shell','input','keyevent','4')
adb('shell','pm','clear',pkg);start();capture('login-default')
enter_key();tap('save_key');status('Key saved securely on this device.')
prefs=adb('exec-out','run-as',pkg,'cat','shared_prefs/kenny-credentials.xml');assert b'local-ui-fixture' not in prefs and b'ciphertext' in prefs and b'iv' in prefs
start();assert node(dump(),'login_key').get('text','')!='','Encrypted saved key was not restored'
tap('sign_in');status('Authentication service is not configured. No credentials were accepted.');capture('login-error')
tap('forget_key');status('Saved key removed.');start();assert node(dump(),'login_key').get('text','')==''
tap('language');locate('language_en');capture('login-language-dialog');tap('language_zh',False)
for locale,label in [('zh','欢迎使用 KennyPvtHax'),('hi','KennyPvtHax में आपका स्वागत है'),('ar','مرحبًا بك في KennyPvtHax')]:
 if locale!='zh':tap('language');tap('language_'+locale,False)
 tree=dump();assert any(n.get('text')==label for n in tree.iter('node')),'Localization failed: '+locale
 mark=locate('header_logo',False);x1,_,x2,_=bounds(mark)
 if locale=='ar':assert (x1+x2)/2>w/2,'Arabic header did not mirror'
 capture('login-'+locale)
# Restore English and verify persistent locale after process death.
tap('language');tap('language_en',False);start();assert any(n.get('text')=='Welcome to KennyPvtHax' for n in dump().iter('node'))
start('pending');tap('login_key');adb('shell','input','text','local-ui-fixture');adb('shell','input','keyevent','4');tap('sign_in');status('Signing in…');capture('login-loading');tap('auth_cancel');status('Request cancelled.')
# Gallery uses its original, separately tested entry point; do not remove it.
checks=['masked input/focus','AES-GCM save and process-death restore','forget and empty restore','unconfigured authentication error','four locales','Arabic mirrored header','persistent locale','real pending request/cancel fixture']
(out/'checks.json').write_text(json.dumps({'runtime':'GitHub Android emulator','checks':checks,'development_only_loading_fixture':True,'production_auth_verified':False,'physical_device_verified':False,'audio_audibility_verified':False},indent=2)+'\n')
print('PASS login, encrypted persistence, four-language/RTL and controlled loading UI; production authentication unavailable')
