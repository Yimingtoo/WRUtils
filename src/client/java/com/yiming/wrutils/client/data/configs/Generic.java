package com.yiming.wrutils.client.data.configs;

import com.yiming.wrutils.client.ModInfo;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.options.*;

import java.util.List;

public class Generic {
    private static final String GENERIC_KEY = ModInfo.MOD_ID + "client.data.config.generic";
    private static final ConfigListBuilder builder = new ConfigListBuilder();

    public static final ConfigBoolean BOOLEAN_TEMPLATE = builder.add(new ConfigBoolean("booleanTemplate", false).apply(GENERIC_KEY));
    public static final ConfigString STRING_TEMPLATE =builder.add( new ConfigString("stringTemplate","defaultValue").apply(GENERIC_KEY));
    public static final ConfigInteger INTEGER_TEMPLATE =builder.add( new ConfigInteger("IntegerTemplate", 0).apply(GENERIC_KEY));
    public static final ConfigFloat FLOAT_TEMPLATE =builder.add( new ConfigFloat("floatTemplate", 0.0f).apply(GENERIC_KEY));
//    public static final ConfigOptionList OPTION_LIST_TEMPLATE =builder.add( new ConfigOptionList("optionListTemplate", 0).apply(GENERIC_KEY));
    public static final ConfigBooleanHotkeyed BOOLEAN_HOTKEYED_TEMPLATE =builder.add( new ConfigBooleanHotkeyed("booleanHotkeyedTemplate", false,"").apply(GENERIC_KEY));

    // Warning: 必须放在类的最后
    public static final List<IConfigBase> GENERIC_LIST = builder.build();
}
