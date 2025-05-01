import os, yaml
from jinja2 import Environment, FileSystemLoader

os.chdir(os.path.dirname(__file__))
env = Environment(loader=FileSystemLoader('templates'))

if 'prepare data':
    # type,name,side,category,descrip,default_cfg
    with open('config.yaml') as f:
        data = yaml.load(f, yaml.Loader)

    class _AttrGetter:

        def __init__(self, callback):
            self.callback = callback

        def __getattr__(self, attr):
            return self.callback(attr)

        __call__ = __getattr__

    filter_val = _AttrGetter(lambda key: _AttrGetter(lambda val: [l for l in data if l[key] == val]))

    def group_val(subdata, attr):
        grouper, res = {}, []
        for l in subdata:
            key = l[attr]
            if key not in grouper:
                grouper[key] = []
                res.append([key, grouper[key]])
            grouper[key].append(l)
        return res

    def to_java_type(raw):
        if raw == 'int':
            return 'Integer'
        return raw.capitalize()


if 'funcs':
    PATHS = {
        'common': 'common/src/main/java/io/yukkuric/hexop/HexOPConfig.java',
        'forge': 'forge/src/main/java/io/yukkuric/hexop/forge/HexOPConfigForge.java',
        'fabric': 'fabric/src/main/java/io/yukkuric/hexop/fabric/HexOPConfigFabric.java',
    }

    def gen(target):
        with open(os.path.join('..', PATHS[target]), 'w') as f:
            print(env.get_template(f'{target}.java').render(**globals()), file=f)


if __name__ == '__main__':
    gen('common')
    gen('forge')
    gen('fabric')