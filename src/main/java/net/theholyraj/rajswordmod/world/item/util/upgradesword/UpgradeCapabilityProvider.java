package net.theholyraj.rajswordmod.world.item.util.upgradesword;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.theholyraj.rajswordmod.world.item.util.ModCapabilities;
import net.theholyraj.rajswordmod.world.item.util.holysword.HolySwordData;
import net.theholyraj.rajswordmod.world.item.util.holysword.IHolySwordData;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class UpgradeCapabilityProvider  implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    private final IUpgradeSwordData instance = new UpgradeSwordData();

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == ModCapabilities.UPGRADE_DATA_CAPABILITY ? LazyOptional.of(() -> instance).cast() : LazyOptional.empty();
    }
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        instance.writeToNBT(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        instance.readFromNBT(nbt);
    }

}
