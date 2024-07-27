package net.theholyraj.rajswordmod.world.item.util.bloodsword;

import net.minecraft.nbt.CompoundTag;

public class BloodSwordData implements IBloodSwordData {
    private int data;

    @Override
    public void setData(int data) {
        this.data = data;
    }

    @Override
    public int getData() {
        return data;
    }

    public void readFromNBT(CompoundTag nbt) {
        data = nbt.getInt("data");
    }

    public void writeToNBT(CompoundTag nbt) {
        nbt.putInt("data", data);
    }
}

