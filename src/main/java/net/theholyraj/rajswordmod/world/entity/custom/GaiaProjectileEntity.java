package net.theholyraj.rajswordmod.world.entity.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;
import net.theholyraj.rajswordmod.world.entity.ModEntities;
import net.theholyraj.rajswordmod.world.item.custom.GaiaBladeItem;

import java.util.HashMap;
import java.util.Map;

public class GaiaProjectileEntity extends Projectile {
    private static final EntityDataAccessor<Boolean> HIT =
            SynchedEntityData.defineId(GaiaProjectileEntity.class, EntityDataSerializers.BOOLEAN);
    private final float swordDamage;
    private final Map<Enchantment, Integer> swordEnchantments;


    public GaiaProjectileEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.swordDamage = 0;
        this.swordEnchantments = new HashMap<>();
    }
    public GaiaProjectileEntity(Level pLevel, Player player, ItemStack sword) {
        super(ModEntities.GAIA_PROJECTILE.get(), pLevel);
        setOwner(player);
        this.swordEnchantments = EnchantmentHelper.getEnchantments(sword);
        this.swordDamage = ((GaiaBladeItem) sword.getItem()).getDamage()+1;

        Vec3 blockpos = player.position();
        double d0 = blockpos.x() ;
        double d1 = blockpos.y() + 1.75D;
        double d2 = blockpos.z() ;
        this.moveTo(d0, d1, d2, this.getYRot(), this.getXRot());
        Vec3 vec3 = this.getDeltaMovement();
        this.setDeltaMovement(vec3.scale(0.5));
    }

    @Override
    public void tick() {
        super.tick();

        if (this.tickCount >= 23) {
            this.remove(RemovalReason.DISCARDED);
        }
        if (this.level().isClientSide) {
            // Define the number of particles
            int particleCount = 2;

            // Spawn particles around the projectile
            for (int i = 0; i < particleCount; i++) {
                double offsetX = (this.random.nextDouble() - 0.5) * this.getBbWidth();
                double offsetY = (this.random.nextDouble() - 0.5) * this.getBbHeight();
                double offsetZ = (this.random.nextDouble() - 0.5) * this.getBbWidth();

                this.level().addParticle(ParticleTypes.HAPPY_VILLAGER,
                        this.getX() + offsetX,
                        this.getY() + offsetY,
                        this.getZ() + offsetZ,
                        0, 0, 0);
            }
        }

        Vec3 vec3 = this.getDeltaMovement();
        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, hitresult))
            this.onHit(hitresult);

        double d0 = this.getX() + vec3.x;
        double d1 = this.getY() + vec3.y;
        double d2 = this.getZ() + vec3.z;
        this.updateRotation();

        this.setDeltaMovement(vec3.scale(0.9F));
        this.setPos(d0, d1, d2);


    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        Entity hitEntity = hitResult.getEntity();
        Entity owner = this.getOwner();
        if(hitEntity == owner && this.level().isClientSide()) {
            return;
        }
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.NEUTRAL,
                2F, 1F);
        float totalDamage = swordDamage;
        if (swordEnchantments.containsKey(Enchantments.SHARPNESS)) {
            int sharpnessLevel = swordEnchantments.get(Enchantments.SHARPNESS);
            totalDamage += 0.5F * sharpnessLevel + 0.5F;
        }
        LivingEntity livingentity = owner instanceof LivingEntity ? (LivingEntity)owner : null;
        hitEntity.hurt(this.damageSources().mobProjectile(this, livingentity), totalDamage);
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if(this.level().isClientSide()) {
            return;
        }
        if (hitResult.getType() == HitResult.Type.BLOCK){
            if (this.tickCount <23){
                this.tickCount = 22;
            }
        }

        if(hitResult.getType() == HitResult.Type.ENTITY && hitResult instanceof EntityHitResult entityHitResult) {
            Entity hit = entityHitResult.getEntity();
            Entity owner = this.getOwner();
            if(owner != hit) {
                this.entityData.set(HIT, true);
            }
        } else {
            this.entityData.set(HIT, true);
        }
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(HIT, false);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
