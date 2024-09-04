package net.theholyraj.rajswordmod.world.item.util;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.theholyraj.rajswordmod.world.item.util.bloodsword.IBloodSwordData;
import net.theholyraj.rajswordmod.world.item.util.holysword.IHolySwordData;
import net.theholyraj.rajswordmod.world.item.util.upgradesword.IUpgradeSwordData;

;

public class ModCapabilities {
    public static final Capability<IBloodSwordData> BLOOD_DATA_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<IHolySwordData> HOLY_DATA_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<IUpgradeSwordData> UPGRADE_DATA_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});



    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(IBloodSwordData.class);
        event.register(IHolySwordData.class);
        event.register(IUpgradeSwordData.class);
    }
}
