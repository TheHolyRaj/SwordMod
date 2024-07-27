package net.theholyraj.rajswordmod.world.item.util.holysword;

import net.minecraft.nbt.CompoundTag;

public interface IHolySwordData {
    void setData(float data);

    float getData();

    void readFromNBT(CompoundTag nbt);

    void writeToNBT(CompoundTag nbt);
}
