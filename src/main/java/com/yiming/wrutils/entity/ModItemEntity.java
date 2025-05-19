package com.yiming.wrutils.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class ModItemEntity extends Entity {


    public ModItemEntity(EntityType<?> type, World world) {
        super(type, world);
        System.out.println("============================ ModItemEntity 一个三 ==============================");
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();

        double x1 = this.getX();
        double y1 = this.getY();
        double z1 = this.getZ();


    }

    //    public ModItemEntity(World world, double x, double y, double z) {
//        this(EntityType.ITEM, world);
//        this.setPosition(x, y, z);
//        this.setVelocity(0, 0, 0);
//    }
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

