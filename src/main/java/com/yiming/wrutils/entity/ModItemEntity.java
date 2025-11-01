package com.yiming.wrutils.entity;

import com.yiming.wrutils.Wrutils;
import com.yiming.wrutils.data.event.EventRecorder;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class ModItemEntity extends Entity {


    public ModItemEntity(EntityType<?> type, World world) {
        super(type, world);
        setPosition(2, 2, 2);
        setBoundingBox(new Box(0, 0, 0, 0.5, 0.5, 0.5));

    }

    //    public ModItemEntity(World world, double x, double y, double z) {
//        this(EntityType.ITEM, world);
//        this.setPosition(x, y, z);
//        this.setVelocity(0, 0, 0);
//    }

    public static void spawnModItemEntity(World world) {
        if (EventRecorder.modItemEntity11 != null) {
            EventRecorder.modItemEntity11.discard();
        }
        ModItemEntity modItemEntity = new ModItemEntity(Wrutils.MOD_ITEM_ENTITY_ENTITY_TYPE, world);
        EventRecorder.modItemEntity11 = modItemEntity;
        world.spawnEntity(modItemEntity);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {

    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        return false;
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
    }
}

