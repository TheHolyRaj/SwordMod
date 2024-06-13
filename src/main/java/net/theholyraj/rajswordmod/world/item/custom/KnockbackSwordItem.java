package net.theholyraj.rajswordmod.world.item.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.theholyraj.rajswordmod.client.particle.ModParticles;
import net.theholyraj.rajswordmod.client.sound.ModSounds;
import net.theholyraj.rajswordmod.client.sound.custom.ChargeTickableSound;
import net.theholyraj.rajswordmod.world.config.ModCommonConfigs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KnockbackSwordItem extends SwordItem {
    @OnlyIn(Dist.CLIENT)
    private ChargeTickableSound instance1;
    @OnlyIn(Dist.CLIENT)
    private ChargeTickableSound instance2;
    @OnlyIn(Dist.CLIENT)
    private ChargeTickableSound instance3;
    @OnlyIn(Dist.CLIENT)
    SoundManager manager;

    public KnockbackSwordItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pUsedHand == InteractionHand.MAIN_HAND){
            pPlayer.startUsingItem(pUsedHand);
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        if (pLevel.isClientSide){
            playSounds(pLevel,pLivingEntity,pRemainingUseDuration);
        }
    }
    @OnlyIn(Dist.CLIENT)
    private void playSounds(Level pLevel, LivingEntity pLivingEntity, int pRemainingUseDuration){
        if (pLevel.isClientSide){
            getManager();
            createInstance((Player) pLivingEntity);
            if (72000 - pRemainingUseDuration == ModCommonConfigs.KNOCKBACK_SWORD_CHARGE_TIME.get()){
                if (manager.isActive(instance2)){
                    manager.stop(instance2);
                }
                manager.play(instance3);
            }
            else if (72000 - pRemainingUseDuration == (ModCommonConfigs.KNOCKBACK_SWORD_CHARGE_TIME.get()/3 + ModCommonConfigs.KNOCKBACK_SWORD_CHARGE_TIME.get()/3)){
                if (manager.isActive(instance1)){
                    manager.stop(instance1);
                }
                manager.play(instance2);
            }
            else if (72000 - pRemainingUseDuration == ModCommonConfigs.KNOCKBACK_SWORD_CHARGE_TIME.get()/3){
                manager.play(instance1);
            }
        }
    }
    @OnlyIn(Dist.CLIENT)
    public void createInstance(Player player){
        if (instance1==null){
            instance1 = new ChargeTickableSound(0.8f, player);
        }
        if (instance2==null){
            instance2 = new ChargeTickableSound(1.2f, player);
        }
        if (instance3==null){
            instance3 = new ChargeTickableSound(1.6f, player);
        }
    }
    @OnlyIn(Dist.CLIENT)
    public void getManager(){
        if (manager == null){
            manager = Minecraft.getInstance().getSoundManager();
        }
    }

    @Override
    public void onStopUsing(ItemStack stack, LivingEntity entity, int count) {
        if (entity instanceof Player player){
            player.getCooldowns().addCooldown(this, ModCommonConfigs.KNOCKBACK_SWORD_COOLDOWN.get());
        }
        super.onStopUsing(stack, entity, count);
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {

        if (72000 - pTimeCharged > ModCommonConfigs.KNOCKBACK_SWORD_CHARGE_TIME.get()){
            attack(pLevel,pLivingEntity,3, pStack);
        }
        else if (72000 - pTimeCharged > (ModCommonConfigs.KNOCKBACK_SWORD_CHARGE_TIME.get()/3 + ModCommonConfigs.KNOCKBACK_SWORD_CHARGE_TIME.get()/3)){
            attack(pLevel,pLivingEntity,2, pStack);
        }
        else if (72000 - pTimeCharged > ModCommonConfigs.KNOCKBACK_SWORD_CHARGE_TIME.get()/3){
            attack(pLevel,pLivingEntity,1, pStack);
        }

        super.releaseUsing(pStack, pLevel, pLivingEntity, pTimeCharged);
    }

    public void attack(Level pLevel, LivingEntity pLivingEntity, int charge, ItemStack stack) {
        Vec3 rightVector;
        Vec3 upVector;
        Vec3 viewVector;
        // Get the player's view vector
        viewVector = pLivingEntity.getViewVector(1f).normalize();

        // Get the player's position
        Vec3 position = new Vec3(pLivingEntity.position().x, pLivingEntity.position().y + 1.7, pLivingEntity.position().z);

        // Define a reference up vector
        Vec3 arbitraryUp = new Vec3(0, 1, 0);

        // If looking almost directly up or down, adjust the arbitrary up vector
        if (Math.abs(viewVector.dot(arbitraryUp)) > 0.99) {
            arbitraryUp = new Vec3(1, 0, 0);
        }

        // Calculate the right vector (cross product of view vector and arbitrary up vector)
        rightVector = viewVector.cross(arbitraryUp).normalize();

        // Calculate the up vector (cross product of right vector and view vector)
        upVector = rightVector.cross(viewVector).normalize();

        // Calculate the down vector (negative of the up vector)
        Vec3 downVector = upVector.scale(-1);

        // Calculate the left vector (negative of the right vector)
        Vec3 leftVector = rightVector.scale(-1);

        // Calculate the half dimensions to determine offsets
        double length = 2.25;
        double halfLength = length / 2.0;
        double width = 1.5;
        double halfWidth = width / 2.0;
        double height = 1.5;
        double halfHeight = height / 2.0;

        // Adjust the position to place the nearest face of the cuboid in front of the player
        Vec3 frontCenter = position.add(viewVector.scale(length / 2.0 + halfLength));

        // Length of each smaller cuboid
        double smallLength = length / 3.0;
        double smallHalfLength = smallLength / 2.0;

        // Calculate front centers for each smaller cuboid
        Vec3 frontCenter1 = frontCenter.add(viewVector.scale(-length / 2.0 + smallHalfLength));
        Vec3 frontCenter2 = frontCenter1.add(viewVector.scale(smallLength));
        Vec3 frontCenter3 = frontCenter2.add(viewVector.scale(smallLength));

        // Calculate the eight corners of the three smaller cuboids
        Vec3[] cuboid1Corners = calculateCorners(frontCenter1, smallLength, halfWidth, halfHeight, leftVector, rightVector, upVector, downVector, viewVector);
        Vec3[] cuboid2Corners = calculateCorners(frontCenter2, smallLength, halfWidth, halfHeight, leftVector, rightVector, upVector, downVector, viewVector);
        Vec3[] cuboid3Corners = calculateCorners(frontCenter3, smallLength, halfWidth, halfHeight, leftVector, rightVector, upVector, downVector, viewVector);

        for (Vec3 corner : cuboid1Corners){
            addParticle(pLevel, corner);
        }
        for (Vec3 corner : cuboid2Corners){
            addParticle(pLevel, corner);
        }
        for (Vec3 corner : cuboid3Corners){
            addParticle(pLevel, corner);
        }

        // Create AABB instances for each smaller cuboid
        AABB aabb1 = createAABBFromCorners(cuboid1Corners);
        AABB aabb2 = createAABBFromCorners(cuboid2Corners);
        AABB aabb3 = createAABBFromCorners(cuboid3Corners);

        List<Entity> list1 = pLevel.getEntities(pLivingEntity, aabb1);
        List<Entity> list2 = pLevel.getEntities(pLivingEntity, aabb2);
        List<Entity> list3 = pLevel.getEntities(pLivingEntity, aabb3);
        addParticle(pLevel, aabb1);
        addParticle(pLevel, aabb2);
        addParticle(pLevel, aabb3);

        double launchY ;
        if (viewVector.y< 0.5){
            launchY = 0.5;
        }else launchY = viewVector.y;

        Vec3 launchCode =new Vec3(viewVector.x *(charge + (double)EnchantmentHelper.getEnchantmentLevel(Enchantments.KNOCKBACK,pLivingEntity)/2)
                ,launchY *(charge + (double)EnchantmentHelper.getEnchantmentLevel(Enchantments.KNOCKBACK,pLivingEntity)/2)
                , viewVector.z *(charge + (double)EnchantmentHelper.getEnchantmentLevel(Enchantments.KNOCKBACK,pLivingEntity)/2));

        boolean soundFlag = false;

        for (Entity entity : list1) {
            soundFlag = true;
            if (!pLevel.isClientSide){
                entity.hurt(pLevel.damageSources().playerAttack((Player) pLivingEntity), 6);
            }
            entity.setDeltaMovement(0,0,0);
            entity.setDeltaMovement(launchCode);
        }
        for (Entity entity : list2) {
            soundFlag = true;
            if (!pLevel.isClientSide) {
                entity.hurt(pLevel.damageSources().playerAttack((Player) pLivingEntity), 6);
                stack.setDamageValue(4*charge);
            }
            entity.setDeltaMovement(0,0,0);
            entity.setDeltaMovement(launchCode);
        }
        for (Entity entity : list3) {
            soundFlag = true;
            if (!pLevel.isClientSide){
                entity.hurt(pLevel.damageSources().playerAttack((Player) pLivingEntity), 4);

            }
            entity.setDeltaMovement(0,0,0);
            entity.setDeltaMovement(launchCode);
        }
        if (soundFlag){
            pLevel.playSound(pLivingEntity,pLivingEntity.blockPosition(),SoundEvents.GENERIC_EXPLODE,SoundSource.PLAYERS,1f,1f);
        }
    }

    AABB createAABBFromCorners(Vec3[] corners) {
        double minX = Double.POSITIVE_INFINITY, minY = Double.POSITIVE_INFINITY, minZ = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY, maxZ = Double.NEGATIVE_INFINITY;
        for (Vec3 corner : corners) {
            if (corner.x < minX) minX = corner.x;
            if (corner.y < minY) minY = corner.y;
            if (corner.z < minZ) minZ = corner.z;
            if (corner.x > maxX) maxX = corner.x;
            if (corner.y > maxY) maxY = corner.y;
            if (corner.z > maxZ) maxZ = corner.z;
        }
        return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    Vec3[] calculateCorners(Vec3 center, double length, double halfWidth, double halfHeight, Vec3 leftVector, Vec3 rightVector, Vec3 upVector, Vec3 downVector, Vec3 viewVector) {
        Vec3[] corners = new Vec3[8];
        Vec3 halfLengthVec = viewVector.scale(length / 2.0);
        corners[0] = center.add(leftVector.scale(halfWidth)).add(upVector.scale(halfHeight)).add(halfLengthVec);
        corners[1] = center.add(rightVector.scale(halfWidth)).add(upVector.scale(halfHeight)).add(halfLengthVec);
        corners[2] = center.add(leftVector.scale(halfWidth)).add(downVector.scale(halfHeight)).add(halfLengthVec);
        corners[3] = center.add(rightVector.scale(halfWidth)).add(downVector.scale(halfHeight)).add(halfLengthVec);
        corners[4] = center.add(leftVector.scale(halfWidth)).add(upVector.scale(halfHeight)).subtract(halfLengthVec);
        corners[5] = center.add(rightVector.scale(halfWidth)).add(upVector.scale(halfHeight)).subtract(halfLengthVec);
        corners[6] = center.add(leftVector.scale(halfWidth)).add(downVector.scale(halfHeight)).subtract(halfLengthVec);
        corners[7] = center.add(rightVector.scale(halfWidth)).add(downVector.scale(halfHeight)).subtract(halfLengthVec);
        return corners;
    }
    public void addParticle(Level pLevel, Vec3 vec3){
        Random random = new Random();

        pLevel.addParticle(ParticleTypes.ENCHANTED_HIT,vec3.x+ random.nextFloat(-0.5f,0.5f), vec3.y+ random.nextFloat(-1,1), vec3.z+ random.nextFloat(-1,1), 0, 0, 0);

    }
    public void addParticle(Level pLevel, AABB aabb){
        Random random = new Random();

        pLevel.addParticle(ParticleTypes.ENCHANTED_HIT,aabb.minX+ random.nextFloat(-0.3f,0.3f), aabb.minY+ random.nextFloat(-0.3f,0.3f), aabb.minZ+ random.nextFloat(-0.3f,0.3f), 0, 0, 0);
        pLevel.addParticle(ParticleTypes.ENCHANTED_HIT,aabb.minX+ random.nextFloat(-0.3f,0.3f), aabb.maxY+ random.nextFloat(-0.3f,0.3f), aabb.minZ+ random.nextFloat(-0.3f,0.3f), 0, 0, 0);
        pLevel.addParticle(ParticleTypes.ENCHANTED_HIT,aabb.minX+ random.nextFloat(-0.3f,0.3f), aabb.minY+ random.nextFloat(-0.3f,0.3f), aabb.maxZ+ random.nextFloat(-0.3f,0.3f), 0, 0, 0);
        pLevel.addParticle(ParticleTypes.ENCHANTED_HIT,aabb.minX+ random.nextFloat(-0.3f,0.3f), aabb.maxY+ random.nextFloat(-0.3f,0.3f), aabb.maxZ+ random.nextFloat(-0.3f,0.3f), 0, 0, 0);

        pLevel.addParticle(ParticleTypes.ENCHANTED_HIT,aabb.maxX+ random.nextFloat(-0.3f,0.3f), aabb.minY+ random.nextFloat(-0.3f,0.3f), aabb.minZ+ random.nextFloat(-0.3f,0.3f), 0, 0, 0);
        pLevel.addParticle(ParticleTypes.ENCHANTED_HIT,aabb.maxX+ random.nextFloat(-0.3f,0.3f), aabb.maxY+ random.nextFloat(-0.3f,0.3f), aabb.minZ+ random.nextFloat(-0.3f,0.3f), 0, 0, 0);
        pLevel.addParticle(ParticleTypes.ENCHANTED_HIT,aabb.maxX+ random.nextFloat(-0.3f,0.3f), aabb.minY+ random.nextFloat(-0.3f,0.3f), aabb.maxZ+ random.nextFloat(-0.3f,0.3f), 0, 0, 0);
        pLevel.addParticle(ParticleTypes.ENCHANTED_HIT,aabb.maxX+ random.nextFloat(-0.3f,0.3f), aabb.maxY+ random.nextFloat(-0.3f,0.3f), aabb.maxZ+ random.nextFloat(-0.3f,0.3f), 0, 0, 0);

    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BLOCK;
    }
}
