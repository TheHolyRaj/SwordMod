package net.theholyraj.rajswordmod.world.item.util.upgradesword;

import net.minecraft.nbt.CompoundTag;

public class UpgradeSwordData implements IUpgradeSwordData{
    private int data;

    @Override
    public void setData(int data) {this.data=data;}

    @Override
    public int getData() {return data;}

    @Override
    public void readFromNBT(CompoundTag nbt) {data = nbt.getInt("data");}

    @Override
    public void writeToNBT(CompoundTag nbt) {nbt.putInt("data", data);}
}
