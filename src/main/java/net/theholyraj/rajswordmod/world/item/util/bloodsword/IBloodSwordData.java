package net.theholyraj.rajswordmod.world.item.util.bloodsword;

import net.minecraft.nbt.CompoundTag;

public interface IBloodSwordData {
    void setData(int data);

    int getData();

    void readFromNBT(CompoundTag nbt);

    void writeToNBT(CompoundTag nbt);
}
