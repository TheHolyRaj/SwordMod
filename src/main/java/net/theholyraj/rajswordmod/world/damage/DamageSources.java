package net.theholyraj.rajswordmod.world.damage;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.level.Level;
import net.theholyraj.rajswordmod.SwordMod;

public class DamageSources {
    public static final ResourceKey<DamageType> HOLY = ResourceKey.create(Registries.DAMAGE_TYPE, SwordMod.loc("holy"));

}
