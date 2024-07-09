package net.theholyraj.rajswordmod.world.item;

import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.theholyraj.rajswordmod.SwordMod;
import net.theholyraj.rajswordmod.world.config.ModCommonConfigs;
import net.theholyraj.rajswordmod.world.item.custom.*;
import net.theholyraj.rajswordmod.world.item.util.ModTiers;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, SwordMod.MODID);


    public static final RegistryObject<DeflectSwordItem> DEFLECT_SWORD = ITEMS.register("deflect_sword",
            () -> new DeflectSwordItem(ModTiers.FABLED, 8-5,1.6f-4, new Item.Properties().fireResistant()));
    public static final RegistryObject<SentinelSwordItem> SENTINEL_SWORD = ITEMS.register("sentinel_sword",
            () -> new SentinelSwordItem(ModTiers.FABLED, 10-5,1.2f-4, new Item.Properties().fireResistant()));
    public static final RegistryObject<KnockbackSwordItem> KNOCKBACK_SWORD = ITEMS.register("knockback_sword",
            () -> new KnockbackSwordItem(ModTiers.FABLED, 8-5,1.6f-4, new Item.Properties().fireResistant()));
    public static final RegistryObject<HolySwordItem> HOLY_SWORD = ITEMS.register("holy_sword",
            () -> new HolySwordItem(ModTiers.FABLED, 8-5,1.6f-4, new Item.Properties().fireResistant()));
    public static final RegistryObject<GaiaBladeItem> GAIA_SWORD = ITEMS.register("gaia_sword",
            () -> new GaiaBladeItem(ModTiers.FABLED, 10-5,1.2f-4, new Item.Properties().fireResistant()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
