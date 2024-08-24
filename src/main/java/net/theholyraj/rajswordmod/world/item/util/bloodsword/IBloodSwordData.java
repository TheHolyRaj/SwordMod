package net.theholyraj.rajswordmod.world.item.util.bloodsword;

import net.minecraft.nbt.CompoundTag;

public interface IBloodSwordData {
    void setData(float data);

    float getData();

    void readFromNBT(CompoundTag nbt);

    void writeToNBT(CompoundTag nbt);
}
