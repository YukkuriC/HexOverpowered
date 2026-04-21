package io.yukkuric.hexop.forge;

import io.yukkuric.hexop.HexOverpowered;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

import static io.yukkuric.hexop.HexOPConfig.*;

{% set data_common = filter_val.side.common -%}
{% set data_forge = filter_val.side.forge -%}
public class HexOPConfigForge implements API {
    public static HexOPConfigForge INSTANCE;
    {%- for line in data_forge %}
    private static final String desc_{{line.name}} = "{{line.descrip}}";
    public static {{line.type}} {{line.name}}() {
        return INSTANCE.cfg_{{line.name}}.get();
    }
    {%- endfor %}{{'\n'}}

    {%- for line in data_common %}
    public {{line.type}} {{line.name}}() {
        return cfg_{{line.name}}.get();
    }
    {%- endfor %}{{'\n'}}

    {%- for grp,lines in group_val(data,'type') %}
    public ModConfigSpec.{{grp.capitalize()}}Value{% for line in lines %}
            cfg_{{line.name}}
            {%- if loop.last %};{% else %},{% endif %}
        {%- endfor %}
    {%- endfor %}

    public HexOPConfigForge(ModConfigSpec.Builder builder) {
        {%- for grp,lines in group_val(data,'category') %}
        {%- set grp_pieces = grp.split('.') -%}
        {%- for piece in grp_pieces %}
        builder.push("{{piece}}");
        {%- endfor %}
        {%- for line in lines %}
        cfg_{{line.name}} = builder.comment(desc_{{line.name}}) {{-''-}}
                .define{% if line.type == 'boolean' %}{% else %}InRange{% endif %}("{{line.name}}", {{line.default}});
        {%- endfor %}
        {%- for piece in grp_pieces %}
        builder.pop();
        {%- endfor %}{{'\n'}}
        {%- endfor %}
        INSTANCE = this;
    }

    public static final ModConfigSpec SPEC;

    static {
        var pair = new ModConfigSpec.Builder().configure(HexOPConfigForge::new);
        SPEC = pair.getValue();
    }

    public static void register(ModContainer modContainer) {
        bindConfigImp(INSTANCE);
        modContainer.registerConfig(ModConfig.Type.COMMON, SPEC);
    }
}