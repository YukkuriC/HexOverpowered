from YukkuriC.minecraft.codegen.jinja import *

load_data_yaml('config_mixin.yaml')
batch_gen(
    load_env(__file__),
    {
        'mixin': 'common/src/main/java/io/yukkuric/hexop/mixin_interface/HexOPMixinConfigFile.java',
    },
)
