package net.theholyraj.rajswordmod.client.particle;

import net.minecraft.client.particle.AttackSweepParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.theholyraj.rajswordmod.SwordMod;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, SwordMod.MODID);

    public static final RegistryObject<SimpleParticleType> DEFLECT_PARTICLES =
            PARTICLE_TYPES.register("deflect_particles", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> HOLY_EXPLOSION_PARTICLES =
            PARTICLE_TYPES.register("holy_explosion_particles", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> HOLY_FIRE_PARTICLES =
            PARTICLE_TYPES.register("holy_fire_particles", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> ANTI_ARMOR_PARTICLES =
            PARTICLE_TYPES.register("anti_armor_particles", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> GAIA_BLADE_PARTICLES =
            PARTICLE_TYPES.register("gaia_blade_particles", () -> new SimpleParticleType(true));

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
