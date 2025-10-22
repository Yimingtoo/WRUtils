package com.yiming.wrutils;

import com.yiming.wrutils.data.RedstoneInfoManager;
import com.yiming.wrutils.data.event.EventRecorder;
import com.yiming.wrutils.entity.ModItemEntity;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;


public class Wrutils implements ModInitializer {
    public static final String MOD_ID = "wrutils-mod";

//    public static RedstoneInfoManager redstoneInfoManager = new RedstoneInfoManager();
    public static EventRecorder eventRecorder = new EventRecorder();
    public static final EntityType<ModItemEntity> MOD_ITEM_ENTITY_ENTITY_TYPE = Registry.register(
            Registries.ENTITY_TYPE,
            "wrutils_mod_item_entity",
            EntityType.Builder.create(ModItemEntity::new, SpawnGroup.MISC).build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.ofVanilla("wrutils_mod_item_entity")))
    );

    @Override
    public void onInitialize() {
    }
}
