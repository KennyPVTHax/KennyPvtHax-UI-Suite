#!/usr/bin/env python3
"""Real APK accessibility/input smoke test; does not assert speaker/device FPS."""
import subprocess,argparse,time,xml.etree.ElementTree as ET,re,json
from pathlib import Path
p=argparse.ArgumentParser();p.add_argument('--adb',default='adb');a=p.parse_args();r=Path(__file__).resolve().parents[2];out=r/'shared/visual-reference'
def adb(*args):return subprocess.check_output([a.adb,*args],timeout=90)
def dump():
 adb('shell','uiautomator','dump','/sdcard/kenny-gallery.xml');return ET.fromstring(adb('shell','cat','/sdcard/kenny-gallery.xml'))
def find(tree,label):
 for n in tree.iter('node'):
  if n.get('text')==label:return n
 raise AssertionError('Missing '+label)
def tap(n):
 x1,y1,x2,y2=map(int,re.findall(r'\d+',n.get('bounds')));adb('shell','input','tap',str((x1+x2)//2),str((y1+y2)//2))
adb('shell','am','force-stop','com.kennypvthax.uisuite');adb('shell','am','start','-W','-n','com.kennypvthax.uisuite/.MainActivity','--ef','preview_time','3');tree=dump();assert find(tree,'Standard button').get('enabled')=='true';assert find(tree,'Disabled button').get('enabled')=='false';tap(find(tree,'Standard button'))
# Scroll until a real checkbox is visible, then confirm semantic state transition.
for i in range(4):
 tree=dump()
 try:check=find(tree,'Circular checkbox');break
 except AssertionError:adb('shell','input','swipe','400','1400','400','400','450')
else:raise AssertionError('checkbox unavailable')
old=check.get('checked');tap(check);tree=dump();assert find(tree,'Circular checkbox').get('checked')!=old
for i in range(4):
 slider=next((n for n in tree.iter('node') if n.get('class')=='android.widget.SeekBar'),None)
 if slider is not None:break
 adb('shell','input','swipe','400','1400','400','400','450');tree=dump()
assert slider is not None,'Slider missing'
x1,y1,x2,y2=map(int,re.findall(r'\d+',slider.get('bounds')));assert y2-y1>=48,'Slider collapsed'
adb('shell','input','tap',str(int(x1+(x2-x1)*.75)),str((y1+y2)//2));tree=dump();assert any(n.get('text','').startswith('Intensity · ') and n.get('text')!='Intensity · 55%' for n in tree.iter('node'))
# Persistent state across process restart.
adb('shell','am','force-stop','com.kennypvthax.uisuite');adb('shell','am','start','-W','-n','com.kennypvthax.uisuite/.MainActivity');tree=dump()
for i in range(4):
 try:after=find(tree,'Circular checkbox');break
 except AssertionError:adb('shell','input','swipe','400','1400','400','400','450');tree=dump()
assert after.get('checked')!=old
(out/'app-device-smoke.json').write_text(json.dumps({'runtime':'authorized connected Android emulator/device','checks':['button enabled','button disabled','real checkbox toggle','visible slider touch input','preference persistence across force-stop'],'audio_audibility_verified':False,'physical_device_verified':False},indent=2)+'\n')
print('PASS real Android button/checkbox semantics and persisted state; speaker/performance not verified')
