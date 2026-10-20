#!/usr/bin/env python3
"""Capture real installed APK output at a deterministic gallery preview time."""
import argparse,subprocess,time,re,xml.etree.ElementTree as ET
from pathlib import Path
p=argparse.ArgumentParser();p.add_argument('--adb',default='adb');a=p.parse_args();out=Path(__file__).resolve().parents[1]/'shared/visual-reference';out.mkdir(parents=True,exist_ok=True)
def adb(*args):return subprocess.check_output([a.adb,*args],timeout=90)
def capture(name):
 data=adb('exec-out','screencap','-p');assert data.startswith(b'\x89PNG\r\n\x1a\n');(out/(name+'.png')).write_bytes(data)
def dump():
 adb('shell','uiautomator','dump','/sdcard/kenny-capture.xml');return ET.fromstring(adb('shell','cat','/sdcard/kenny-capture.xml'))
def find(tree,text):return next((n for n in tree.iter('node') if n.get('text')==text),None)
def tap(n):
 x1,y1,x2,y2=map(int,re.findall(r'\d+',n.get('bounds')));adb('shell','input','tap',str((x1+x2)//2),str((y1+y2)//2))
w,h=map(int,re.findall(r'(\d+)x(\d+)',adb('shell','wm','size').decode())[-1])
def swipe():adb('shell','input','swipe',str(w//2),str(int(h*.8)),str(w//2),str(int(h*.3)),'400');time.sleep(.3)
adb('shell','am','force-stop','com.kennypvthax.uisuite');adb('shell','am','start','-W','-n','com.kennypvthax.uisuite/.MainActivity','--ez','gallery','true','--ef','preview_time','3');time.sleep(1);capture('app-gallery')
for i in range(5):
 tree=dump()
 if find(tree,'Preview dialog') is not None:break
 swipe()
capture('app-controls')
button=find(tree,'Preview dialog')
if button is not None:
 tap(button);time.sleep(.3);capture('app-dialog');tree=dump();close=find(tree,'Close')
 if close is not None:tap(close)
for i in range(5):
 tree=dump()
 if find(tree,'Subtle tap sounds') is not None:break
 swipe()
capture('app-motion');print('Actual Android screenshots saved to',out)
