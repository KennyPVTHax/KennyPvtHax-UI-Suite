#!/usr/bin/env python3
"""Compose labels around authentic captures; never synthesizes application pixels."""
from pathlib import Path
import base64,hashlib,html,json
from PIL import Image,ImageDraw,ImageFont
r=Path(__file__).resolve().parents[1];v=r/'shared/visual-reference'
labels={'menu-aurora.png':'Native menu / ImGui host renderer / t=3 s','app-gallery.png':'Android APK / component gallery / t=3 s','app-controls.png':'Android APK / input controls / t=3 s','app-dialog.png':'Android APK / opaque popup surface','app-motion.png':'Android APK / motion and tap settings'}
font=ImageFont.truetype('/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf',30)
sheet=Image.new('RGB',(2304,3032),'#060609');d=ImageDraw.Draw(sheet)
d.text((48,18),labels['menu-aurora.png'],font=font,fill='#F4C8D0');sheet.paste(Image.open(v/'menu-aurora.png').convert('RGB'),(192,72))
for x,name in [(48,'app-gallery.png'),(1176,'app-controls.png')]:
 d.text((x,990),labels[name],font=font,fill='#F4C8D0');sheet.paste(Image.open(v/name).convert('RGB'),(x,1048))
sheet.save(v/'menu-app-comparison.png');labels['menu-app-comparison.png']='Actual native/Android comparison; different viewport adaptation, not pixel equality'
page='<!doctype html><html lang="en"><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>KennyPvtHax UI Suite — actual renders</title><style>body{background:#060609;color:#f6f2f3;font:16px system-ui;margin:24px}img{max-width:100%;height:auto}a{color:#f4c8d0}article{margin:32px 0}</style><h1>Actual renderer evidence</h1><p>Native ImGui host triangles and Android emulator APK captures. No mockups. Both background compositions are frozen at three seconds. Portrait geometry, text rasterization and font differ; pixel-perfect equivalence is not claimed. Native host uses its K fallback without a logo texture. Physical-device audio and performance remain unverified.</p>'
records={}
for name,label in labels.items():
 p=v/name
 with Image.open(p) as im:im.verify()
 with Image.open(p) as im:size=list(im.size)
 records[name]={'bytes':p.stat().st_size,'dimensions':size,'sha256':hashlib.sha256(p.read_bytes()).hexdigest()}
 data=base64.b64encode(p.read_bytes()).decode();page+=f'<article><h2>{html.escape(label)}</h2><a download="{name}" href="data:image/png;base64,{data}">Download PNG</a><br><img alt="{html.escape(label)}" src="data:image/png;base64,{data}"></article>'
(v/'index.html').write_text(page+'</html>');(v/'image-inventory.json').write_text(json.dumps(records,indent=2)+'\n');print('Validated six actual-render/comparison PNGs and self-contained viewer')
