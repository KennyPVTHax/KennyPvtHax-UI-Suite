#!/usr/bin/env python3
"""Capture real installed APK output; requires an already-running authorized device."""
import argparse,subprocess,time
from pathlib import Path
p=argparse.ArgumentParser();p.add_argument('--adb',default='adb');a=p.parse_args();out=Path(__file__).resolve().parents[1]/'shared/visual-reference';out.mkdir(parents=True,exist_ok=True)
def adb(*args):return subprocess.check_output([a.adb,*args])
adb('shell','am','start','-n','com.kennypvthax.uisuite/.MainActivity');time.sleep(2)
for name in ('app-gallery','app-motion'):
 if name=='app-motion':adb('shell','input','swipe','300','1100','300','250','500');time.sleep(1)
 data=adb('exec-out','screencap','-p');assert data.startswith(b'\x89PNG\r\n\x1a\n');(out/(name+'.png')).write_bytes(data)
print('Actual Android screenshots saved to',out)
