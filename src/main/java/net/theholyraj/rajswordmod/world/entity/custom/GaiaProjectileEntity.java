package net.theholyraj.rajswordmod.world.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.*;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;
import net.theholyraj.rajswordmod.client.particle.ModParticles;
import net.theholyraj.rajswordmod.world.config.ModCommonConfigs;
import net.theholyraj.rajswordmod.world.entity.ModEntities;
import net.theholyraj.rajswordmod.world.item.custom.GaiaBladeItem;

import java.util.*;

public class GaiaProjectileEntity extends Projectile {
    private static final EntityDataAccessor<Boolean> HIT =
            SynchedEntityData.defineId(GaiaProjectileEntity.class, EntityDataSerializers.BOOLEAN);
    private final float swordDamage;
    private final Map<Enchantment, Integer> swordEnchantments;
    private final Level level;
    private final Set<Entity> piercedEntities = new HashSet<>();


    public GaiaProjectileEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.swordDamage = 0;
        this.swordEnchantments = new HashMap<>();
        this.level = pLevel;
    }
    public GaiaProjectileEntity(Level pLevel, Player player, ItemStack sword) {
        super(ModEntities.GAIA_PROJECTILE.get(), pLevel);
        setOwner(player);
        this.swordEnchantments = EnchantmentHelper.getEnchantments(sword);
        this.swordDamage = ((GaiaBladeItem) sword.getItem()).getDamage()+1;
        this.level=pLevel;

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

                this.level().addParticle(ModParticles.GAIA_BLADE_PARTICLES.get(),
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
        if (this.level().getBlockStates(this.getBoundingBox()).noneMatch(BlockBehaviour.BlockStateBase::isAir) && !this.level().getBlockStates(this.getBoundingBox()).noneMatch(BlockBehaviour.BlockStateBase::isSolid) ) {
            this.discard();
        }

        if (!this.level.isClientSide) {
            AABB boundingBox = this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D);
            List<Entity> entities = this.level.getEntities(this, boundingBox, this::canHitEntity);

            for (Entity entity : entities) {
                if (!this.piercedEntities.contains(entity)) {
                    this.onHitEntity(new EntityHitResult(entity));
                }
            }
        }


        double d0 = this.getX() + vec3.x;
        double d1 = this.getY() + vec3.y;
        double d2 = this.getZ() + vec3.z;
        this.updateRotation();

        this.setDeltaMovement(vec3.scale(0.9F));
        this.setPos(d0, d1, d2);


    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
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
        if (!this.piercedEntities.contains(hitEntity)) {
            this.piercedEntities.add(hitEntity);
            super.onHitEntity(hitResult);
            LivingEntity livingentity = owner instanceof LivingEntity ? (LivingEntity)owner : null;
            totalDamage=totalDamage* (ModCommonConfigs.GAIA_BLADE_PROJECTILE_DAMAGE_PERCENTAGE.get());
            hitEntity.hurt(this.damageSources().mobProjectile(this, livingentity), totalDamage);        }

    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        BlockState blockState = this.level.getBlockState(pResult.getBlockPos());
        if (blockState.getBlock() != Blocks.WATER) {
            this.discard();
        }
    }

    @Override
    protected void onHit(HitResult hitResult) {
        HitResult.Type hitresult$type = hitResult.getType();
        if (hitresult$type == HitResult.Type.BLOCK) {
            BlockHitResult blockhitresult = (BlockHitResult)hitResult;
            this.onHitBlock(blockhitresult);
        }

    }
    private void spawnParticles() {
        if (this.level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel) this.level;
            for (int i = 0; i < 25; ++i) {
                double d0 = (this.random.nextDouble()) * this.getBbWidth();
                double d1 = (this.random.nextDouble()) * this.getBbHeight();
                double d2 = (this.random.nextDouble()) * this.getBbWidth();
                serverLevel.sendParticles(ModParticles.GAIA_BLADE_PARTICLES.get(), this.getX(), this.getY()+(this.random.nextDouble()), this.getZ(), 1, d0, d1, d2, 1.0D);
            }
        }
    }

    @Override
    public void remove(RemovalReason pReason) {
        if (!this.level.isClientSide && pReason == RemovalReason.DISCARDED) {
            spawnParticles();
        }
        super.remove(pReason);
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
