package net.theholyraj.rajswordmod.world.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.theholyraj.rajswordmod.SwordMod;
import net.theholyraj.rajswordmod.world.entity.custom.CloneEntity;
import net.theholyraj.rajswordmod.world.entity.custom.DashProjectileEntity;
import net.theholyraj.rajswordmod.world.entity.custom.GaiaProjectileEntity;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SwordMod.MODID);

    public static final RegistryObject<EntityType<DashProjectileEntity>> DASH_PROJECTILE =
            ENTITY_TYPES.register("dash_projectile",
                    () -> EntityType.Builder.<DashProjectileEntity>of(DashProjectileEntity::new, MobCategory.MISC)
                            .sized(2f, 1f)
                            .clientTrackingRange(4)
                            .updateInterval(20)
                            .build("dash_projectile"));

    public static final RegistryObject<EntityType<GaiaProjectileEntity>> GAIA_PROJECTILE =
            ENTITY_TYPES.register("gaia_projectile",
                    () -> EntityType.Builder.<GaiaProjectileEntity>of(GaiaProjectileEntity::new, MobCategory.MISC)
                            .sized(2f, 0.3f)
                            .clientTrackingRange(4)
                            .updateInterval(20)
                            .build("gaia_projectile"));

    public static final RegistryObject<EntityType<CloneEntity>>CLONE_ENTITY =
            ENTITY_TYPES.register("clone_entity", ()-> EntityType.Builder.of(CloneEntity::new, MobCategory.MISC)
                    .sized(1,2).build("clone_entity"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
