package com.yiming.wrutils.client.utils;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.options.ConfigHotkey;

import java.util.List;

public class MemberRegistry<T> {
    protected final ImmutableList.Builder<T> builder = ImmutableList.builder();
    public final List<T> LIST;

    protected MemberRegistry() {
        LIST = builder.build();
    }

    public T register(T member) {
        builder.add(member);
        return member;
    }


}
