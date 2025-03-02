package com.yiming.wrutils.client.render;

import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.text.Text;

public class ModItemEntityRenderState extends EntityRenderState {
    public ModItemEntityRenderState() {
        super();
        this.displayName = Text.literal("WRUtils");
    }
}
