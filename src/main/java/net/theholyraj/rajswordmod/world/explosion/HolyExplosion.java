package net.theholyraj.rajswordmod.world.explosion;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.data.ForgeEntityTypeTagsProvider;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.theholyraj.rajswordmod.SwordMod;
import net.theholyraj.rajswordmod.client.sound.ModSounds;
import net.theholyraj.rajswordmod.world.config.ModCommonConfigs;
import net.theholyraj.rajswordmod.world.item.ModItems;
import net.theholyraj.rajswordmod.world.mobeffects.ModMobEffects;
import net.theholyraj.rajswordmod.world.mobeffects.custom.HolyFireEffect;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = SwordMod.MODID)
public class HolyExplosion extends Explosion {
    public HolyExplosion(Level world, Entity entity, double x, double y, double z, float size) {
        super(world, entity, x, y, z, size, false, BlockInteraction.KEEP);
    }

    @Override
    public void explode() {
        super.explode();
    }

    @Override
    public boolean interactsWithBlocks() {
        return false;
    }

    @Override
    public void finalizeExplosion(boolean spawnParticles) {
        super.finalizeExplosion(spawnParticles);
    }

    @Override
    public DamageSource getDamageSource() {
        return super.getDamageSource();
    }

    /////////////////////////////////////////////////////////EVENT//////////////////////
    @SubscribeEvent
    public static void undeadDamageEvent(ExplosionEvent.Detonate event){
        if (event.getExplosion() instanceof HolyExplosion){
            List<Entity> entities= event.getAffectedEntities();
            entities.removeIf(mob-> mob instanceof LivingEntity entity && entity.getMobType() != MobType.UNDEAD);
            for (Entity entity:entities){
                if (entity instanceof WitherBoss witherBoss){
                    witherBoss.forceAddEffect(new MobEffectInstance(ModMobEffects.HOLY_FIRE.get(),ModCommonConfigs.HOLY_FIRE_DURATION.get()),witherBoss);
                }
                if (entity instanceof LivingEntity livingEntity){
                    if (livingEntity.getMobType() == MobType.UNDEAD){
                        livingEntity.addEffect(new MobEffectInstance(ModMobEffects.HOLY_FIRE.get(), ModCommonConfigs.HOLY_FIRE_DURATION.get()));
                    }
                }
            }
        }
    }
}
