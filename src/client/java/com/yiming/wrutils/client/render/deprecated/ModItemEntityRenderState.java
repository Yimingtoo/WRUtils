package com.yiming.wrutils.client.render.deprecated;

import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.text.Text;
@Deprecated
public class ModItemEntityRenderState extends EntityRenderState {
    public ModItemEntityRenderState() {
        super();
        this.displayName = Text.literal("WRUtils");
    }
}
