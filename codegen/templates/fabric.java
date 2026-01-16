package io.yukkuric.hexop.fabric;

import io.yukkuric.hexop.HexOverpowered;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

import static io.yukkuric.hexop.HexOPConfig.*;

{% set data_common = filter_val.side.common -%}
@Config(name = "HexOverpowered")
public class HexOPConfigFabric implements ConfigData {
    @ConfigEntry.Gui.CollapsibleObject
    private final Common common = new Common();

    public static void setup() {
        AutoConfig.register(HexOPConfigFabric.class, JanksonConfigSerializer::new);
        var instance = AutoConfig.getConfigHolder(HexOPConfigFabric.class).getConfig();
        bindConfigImp(instance.common);
    }

    public static class Common implements API, ConfigData {
        {%- for line in data_common %}
        @Comment("<{{line.category}}> " + desc_{{line.name}})
        private {{line.type}} {{line.name}} = {{line.default.split(',').0}};
        {%- endfor %}{{'\n'}}
        {%- for line in data_common %}
        public {{line.type}} {{line.name}}() {
            return {{line.name}};
        }
        {%- endfor %}
    }
}
