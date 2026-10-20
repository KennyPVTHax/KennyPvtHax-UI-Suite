#!/usr/bin/env python3
"""Render the genuine preserved menu at t=3; uses existing production host harness.
Run menu regressions first to generate fixtures; no SDK substitutes or source edits.
"""
import subprocess,os,struct
from pathlib import Path
from PIL import Image
r=Path(__file__).resolve().parents[1];menu=r/'menu-ui';work=menu/'.verification';work.mkdir(exist_ok=True)
s=(menu/'tests/kenny_phase_b_capture.cpp').read_text().split('int main() {',1)[0]
s+='''\nint main(){InitializeCapture();menu.appearance.theme=KennyAppearance::Theme::Aurora;menu.aurora.Initialize();menu.aurora.time=menu.aurora.particleTime=3;menu.aurora.frame=ImGui::GetFrameCount()+1;for(int i=0;i<KennyAurora::StarCount;i++)menu.aurora.stars[i].position=KennyAurora::State::Home(i,3);Frame();Save("suite-aurora-03");ImGui::DestroyContext();}\n'''
p=work/'suite_capture.cpp';p.write_text(s)
objects=sorted((work/'suite-relocation').glob('imgui*-engine.o'))
if len(objects)!=4:raise SystemExit('Run documented menu regressions with --label suite-relocation first')
def run(*args):subprocess.run([str(x) for x in args],cwd=menu,check=True)
run('g++','-std=c++17','-O0','-g','-pthread','-DIMGUI_ENABLE_TEST_ENGINE','-fsanitize=address,undefined','-fno-omit-frame-pointer','-fno-pie','-no-pie','-I','jni','-I','tests','-I','.verification',p,*objects,'-lz','-o',work/'suite-capture')
run(work/'suite-capture');run('g++','-std=c++17','-O2','tests/kenny_host_rasterize.cpp','-o',work/'suite-raster');run(work/'suite-raster',work/'phase_b-atlas.rgba',work/'suite-aurora-03.draw')
b=(work/'suite-aurora-03.rgba').read_bytes();w,h=struct.unpack('<ii',b[:8]);Image.frombytes('RGBA',(w,h),b[8:]).save(r/'shared/visual-reference/menu-aurora.png')
print('Actual ImGui menu at t=3 saved; host harness uses K fallback because no brand texture is uploaded')
