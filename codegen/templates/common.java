package io.yukkuric.hexop;

{% set data_common = filter_val.side.common -%}
public class HexOPConfig {
    static API imp;

    public static boolean loaded() {
        return imp != null;
    }
    public static void bindConfigImp(API api) {
        imp = api;
    }{{'\n'}}
    {%- for line in data_common %}
    public static {{line.type}} {{line.name}}() {
        return imp.{{line.name}}();
    }
    {%- endfor %}
    public interface API {
        {%- for line in data_common %}
        String desc_{{line.name}} = "{{line.descrip}}";
        {%- endfor %}{{'\n'}}
        {%- for line in data_common %}
        {{line.type}} {{line.name}}();
        {%- endfor %}
    }
}