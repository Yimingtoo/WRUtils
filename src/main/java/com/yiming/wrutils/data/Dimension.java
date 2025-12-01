package com.yiming.wrutils.data;

import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public enum Dimension {
    OVERWORLD("Overworld"),
    NETHER("Nether"),
    END("End"),
    NONE("None");

    private final String name;

    Dimension(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Dimension getWorldDimension(World world) {
        RegistryKey<World> dimensionKey = world.getRegistryKey();
        Identifier dimensionId = dimensionKey.getValue();
        return switch (dimensionId.toString()) {
            case "minecraft:overworld" -> OVERWORLD;
            case "minecraft:the_nether" -> NETHER;
            case "minecraft:the_end" -> END;
            default -> NONE;
        };
    }
}
