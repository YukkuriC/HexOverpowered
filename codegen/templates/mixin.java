package io.yukkuric.hexop.mixin_interface;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class HexOPMixinConfigFile {
    static final String CONFIG_PATH = "config/hexoverpowered-mixin.ini";
    {%- for cfg in data %}
    static final String KEY_{{ cfg.name }} = "{{ cfg.name }}";
    {%- endfor %}

    final Path configPath = Path.of(CONFIG_PATH);
    {%- for cfg in data %}
    final Set<String> {{ cfg.name }} = new HashSet<>();
    {%- endfor %}
    void dumpSet(String raw, Set<String> target) {
        target.clear();
        for (var sub : raw.split(",")) {
            sub = sub.trim();
            if (StringUtils.isEmpty(sub)) continue;
            target.add(sub);
        }
    }
    void processConfigLine(String key, String val) {
        switch (key) {
            {%- for cfg in data %}
            case KEY_{{ cfg.name }} -> dumpSet(val, {{ cfg.name }});
            {%- endfor %}
        }
    }

    void load() {
        tryRun(this::loadInner, "read");
    }
    void save() {
        tryRun(this::saveInner, "writ");
    }

    interface RunnableWithError {
        void run() throws Throwable;
    }
    private void tryRun(RunnableWithError task, String name) {
        try {
            task.run();
        } catch (Throwable e) {
            HexOPMixinPlugin.getLOGGER().error("Error when {}ing mixin config: {}", name, e.getLocalizedMessage());
            try (var sw = new StringWriter()) {
                try (var writer = new PrintWriter(sw)) {
                    e.printStackTrace(writer);
                    HexOPMixinPlugin.getLOGGER().error(sw.toString());
                }
            } catch (Throwable ignored) {
            }
        }
    }
    private void loadInner() throws IOException {
        if (!Files.exists(configPath)) return;
        var lines = Files.readString(configPath, StandardCharsets.UTF_8).split("\n");
        for (var line : lines) {
            if (line.startsWith("#")) continue;
            var raw = line.split("=", -1);
            if (raw.length != 2) {
                HexOPMixinPlugin.getLOGGER().error("Error when reading mixin config line: need exactly 1 '='\nline: " + line);
                continue;
            }
            processConfigLine(raw[0].trim(), raw[1].trim());
        }
    }
    private void saveInner() throws IOException {
        var sb = new StringBuilder();
        {%- for cfg in data %}
            {%- for line in cfg.descrip.strip().split('\n') %}
        sb.append("# {{ line }}\n");
            {%- endfor %}
        sb.append(String.format("%s=%s\n", KEY_{{ cfg.name }}, String.join(", ", {{ cfg.name }})));
        {%- endfor %}
        Files.writeString(configPath, sb.toString(), StandardCharsets.UTF_8);
    }
}
