package com.yiming.wrutils.client.data.configs;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.options.*;

public class ConfigListBuilder {
    private final ImmutableList.Builder<IConfigBase> builder = ImmutableList.builder();

    public ConfigBoolean add(ConfigBoolean config) {
        builder.add(config);
        return config;
    }

    public ConfigString add(ConfigString config) {
        builder.add(config);
        return config;
    }

    public ConfigOptionList add(ConfigOptionList config) {
        builder.add(config);
        return config;
    }

    public ConfigBooleanHotkeyed add(ConfigBooleanHotkeyed config) {
        builder.add(config);
        return config;
    }

    public ConfigFloat add(ConfigFloat config) {
        builder.add(config);
        return config;
    }

    public ConfigInteger add(ConfigInteger config) {
        builder.add(config);
        return config;
    }

    public ImmutableList<IConfigBase> build() {
        return builder.build();
    }
}
