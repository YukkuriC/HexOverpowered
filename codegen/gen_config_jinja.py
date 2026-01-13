from YukkuriC.minecraft.codegen.jinja import *

load_data_yaml('config.yaml')
batch_gen(
    load_env(__file__),
    {
        'common': 'common/src/main/java/io/yukkuric/hexop/HexOPConfig.java',
        'forge': 'forge/src/main/java/io/yukkuric/hexop/forge/HexOPConfigForge.java',
        'fabric': 'fabric/src/main/java/io/yukkuric/hexop/fabric/HexOPConfigFabric.java',
    },
)
