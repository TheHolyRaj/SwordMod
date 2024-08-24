package net.theholyraj.rajswordmod.world.item.util.bloodsword;

import net.minecraft.nbt.CompoundTag;

public class BloodSwordData implements IBloodSwordData {
    private float data;

    @Override
    public void setData(float data) {
        this.data = data;
    }

    @Override
    public float getData() {
        return data;
    }

    public void readFromNBT(CompoundTag nbt) {
        data = nbt.getFloat("data");
    }

    public void writeToNBT(CompoundTag nbt) {
        nbt.putFloat("data", data);
    }
}

