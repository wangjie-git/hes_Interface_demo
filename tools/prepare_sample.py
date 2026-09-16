"""Create a local replay copy without changing any original attachment."""
import argparse, hashlib, json
from pathlib import Path
p=argparse.ArgumentParser()
p.add_argument('input',type=Path);p.add_argument('output',type=Path)
p.add_argument('--rsid',required=True)
p.add_argument('--category',choices=['breathing'])
a=p.parse_args()
if not a.rsid.strip():p.error('rsid must not be blank')
if a.input.resolve()==a.output.resolve():p.error('Use a different output path to preserve the original')
if a.output.exists():p.error('Output already exists; choose a new path')
x=json.loads(a.input.read_text(encoding='utf-8-sig'))
if a.category:
    x={'dataId':'SYNTHETIC-FRAGMENT','orgCode':'DEMO-ORG','deviceCode':'DEMO-DEVICE',
       'doctorCode':'DEMO-DOCTOR',
       'personInfo':{'name':'SYNTHETIC TEST','sexCode':'0'},'checkData':{a.category:x},
       'time':'2026-09-16 10:00:00','checkDate':'2026-09-16 10:00:00','version':'1.0.0'}
x['key']=hashlib.md5((a.rsid+'konsungyitijijsondata').encode('utf-8')).hexdigest()
a.output.write_text(json.dumps(x,ensure_ascii=False,indent=2)+'\n',encoding='utf-8')
print('Wrote local replay copy:',a.output)
