#!/usr/bin/env python3
import hashlib,json,re,subprocess,tempfile
from pathlib import Path
r=Path(__file__).resolve().parents[1];baseline='aa3411056ccd747787c80078c52165b80020482c'
def run(*args,**kw):return subprocess.check_output([str(x) for x in args],cwd=r,text=True,**kw)
# Complete original tracked tree, with deliberate documentation-only README extension.
# Hash through Git's source-defined EOL filters, without adding files to the index.
entries=run('git','ls-tree','-r',baseline).splitlines();files=[];expected=[]
for line in entries:
 meta,name=line.split('\t',1)
 if name=='README.md':continue
 path=r/'menu-ui'/name
 assert path.is_file(),name
 files.append('menu-ui/'+name);expected.append(meta.split()[2])
actual=run('git','hash-object','--stdin-paths',input='\n'.join(files)+'\n').splitlines()
assert len(actual)==len(expected)
for name,a,b in zip(files,actual,expected):assert a==b,name
count=len(files)
assert len(list((r/'menu-ui/jni').rglob('*.a')))==40
s=(r/'menu-ui/jni/ClaudeTheme.h').read_text();t=json.loads((r/'shared/design-tokens/tokens.json').read_text())
for name,value in t['colors'].items():assert re.search(r'\b'+name+r'\s*= Hex\(0x'+value[1:],s,re.I),name
app=r/'app-ui/app/src/main'
java_tokens=(app/'java/com/kennypvthax/uisuite/Tokens.java').read_text()
values={k:int(v,16)&0xffffff for k,v in re.findall(r'\b([A-Z_]+)=(0x[0-9a-f]+)',java_tokens)}
for java,native in {'BG':'bg','SURFACE':'surface','ELEVATED':'surfaceElev','SUBTLE':'surfaceSubtle','OUTLINE':'outline','TEXT':'textPrimary','SECONDARY':'textSecondary','ACCENT':'accent','FIRE':'accentPressed','GLOW':'glow','SELECTED':'selectedInk'}.items():
 assert values[java]==int(t['colors'][native][1:],16),java

assert not (app/'assets/menu.ttf').exists()
assert not list(app.rglob('*.so')) and not list(app.rglob('*.mp4')) and not list(app.rglob('*.ogg'))
assert len(list((app/'res/raw').glob('*')))==1
assert not any(x.is_symlink() and not x.exists() for x in r.rglob('*') if '.git' not in x.parts)
with tempfile.TemporaryDirectory() as tmp:
 model=app/'java/com/kennypvthax/uisuite/AuroraModel.java';check=r/'app-ui/tests/AuroraCheck.java'
 run('javac','-d',tmp,model,check);print(run('java','-cp',tmp,'AuroraCheck'))
 # Compare numerical outputs against the actual original C++ header.
 cpp=Path(tmp)/'parity.cpp';cpp.write_text('''#include <cstdio>\n#include "KennyAurora.h"\nint main(){for(int i=0;i<40;i++){auto h=KennyAurora::State::Home(i,3);printf("%.9f %.9f %.9f\\n",KennyEffects::Unit(i*11+319),h.x,h.y);}for(int l=0;l<3;l++)for(int i=0;i<97;i++){float u=i/96.f;auto c=KennyAurora::Curtain(u,3,l);c.light*=.78f+.22f*KennyAurora::Field(u*5,3*.08,l+939);printf("%.9f %.9f %.9f %.9f %.9f\\n",c.lower,c.height,c.fold,c.light,c.lean);}}''')
 run('g++','-std=c++17','-I',r/'menu-ui/jni',cpp,'-o',Path(tmp)/'parity')
 a=[float(v) for v in run(Path(tmp)/'parity').split()];b=[float(v) for v in run('java','-cp',tmp,'AuroraCheck','samples').split()]
 assert len(a)==len(b);error=max(abs(x-y) for x,y in zip(a,b));assert error<2e-6,error
 print(f'PASS {len(a)} original C++/Java samples; max absolute difference {error:.9g}')
report={'baseline':baseline,'unchanged_original_files':count,'original_files':count+1,'static_libraries':40,'numeric_samples':len(a),'max_absolute_error':error,'device_verified':False}
(r/'shared/visual-reference/source-verification.json').write_text(json.dumps(report,indent=2)+'\n');print(json.dumps(report))
