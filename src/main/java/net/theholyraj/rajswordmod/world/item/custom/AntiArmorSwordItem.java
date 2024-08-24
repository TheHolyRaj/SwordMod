package net.theholyraj.rajswordmod.world.item.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.theholyraj.rajswordmod.client.particle.ModParticles;
import net.theholyraj.rajswordmod.client.sound.custom.AntiArmorChargeTickableSound;
import net.theholyraj.rajswordmod.world.config.ModCommonConfigs;
import net.theholyraj.rajswordmod.world.mobeffects.ModMobEffects;

import java.util.List;

public class AntiArmorSwordItem extends SwordItem {
    @OnlyIn(Dist.CLIENT)
    private AntiArmorChargeTickableSound instance;
    @OnlyIn(Dist.CLIENT)
    SoundManager manager;

    public AntiArmorSwordItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pUsedHand == InteractionHand.MAIN_HAND){
            pPlayer.startUsingItem(pUsedHand);
            if (pLevel.isClientSide()){
                playSounds(pLevel,pPlayer);
            }
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }


    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        int time = (ModCommonConfigs.KNOCKBACK_SWORD_CHARGE_TIME.get() - pRemainingUseDuration)/50;
        Vec3 viewVector = pLivingEntity.getViewVector(1f);
        Vec3 movementVector = new Vec3(viewVector.x/2, -0.5, viewVector.z/2);
        Vec3 movementVectorCheck = new Vec3(viewVector.x/2, 0.5, viewVector.z/2);

        AABB boundingBox = pLivingEntity.getBoundingBox();
        AABB expandedBox = boundingBox.expandTowards(movementVectorCheck);


        for (LivingEntity entity : pLevel.getEntitiesOfClass(LivingEntity.class, expandedBox)) {
            if (entity != pLivingEntity && entity.isAlive()) {
                pLivingEntity.stopUsingItem();
                if (!pLevel.isClientSide()){
                    entity.setDeltaMovement(new Vec3(viewVector.x,+0.5,viewVector.z));
                }
                return;
            }
        }
        if (!pLevel.noCollision(expandedBox)) {
            pLivingEntity.setDeltaMovement(pLivingEntity.getDeltaMovement().add(0, 0.8, 0));
        }
        else {
            pLivingEntity.setDeltaMovement(movementVector.scale(1+time));
        }

        super.onUseTick(pLevel, pLivingEntity, pStack, pRemainingUseDuration);
    }

    @Override
    public void onStopUsing(ItemStack stack, LivingEntity entity, int count) {
        if (entity instanceof Player player){
            attack(player.level(),player,stack);
            player.setDeltaMovement(new Vec3(0,0,0));
            player.getCooldowns().addCooldown(this,ModCommonConfigs.ANTI_ARMOR_CHARGE_COOLDOWN.get());
            if (entity.level().isClientSide()){
                stopSounds(entity.level(),player);
            }
        }
        super.onStopUsing(stack, entity, count);
    }
    public void applyArmorReductionEffect(LivingEntity target, int amplifier) {
        // Apply the custom armor reduction effect with a specific amplifier
        target.addEffect(new MobEffectInstance(ModMobEffects.ANTI_ARMOR.get(),ModCommonConfigs.ANTI_ARMOR_EFFECT_DURATION.get(),amplifier));
    }

    public void attack(Level pLevel, LivingEntity pLivingEntity, ItemStack stack) {
        Vec3 rightVector;
        Vec3 upVector;
        Vec3 viewVector;
        // Get the player's view vector
        viewVector = pLivingEntity.getViewVector(1f).normalize();
        spawnSlashParticles(pLivingEntity,pLevel);

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
        double length = 2;
        double halfLength = length / 2.0;
        double width = 2;
        double halfWidth = width / 2.0;
        double height = 2;
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
         //   addParticle(pLevel, corner);
        }
        for (Vec3 corner : cuboid2Corners){
         //   addParticle(pLevel, corner);
        }
        for (Vec3 corner : cuboid3Corners){
        //    addParticle(pLevel, corner);
        }

        // Create AABB instances for each smaller cuboid
        AABB aabb1 = createAABBFromCorners(cuboid1Corners);
        AABB aabb2 = createAABBFromCorners(cuboid2Corners);
        AABB aabb3 = createAABBFromCorners(cuboid3Corners);


        List<Entity> list1 = pLevel.getEntities(pLivingEntity, aabb1);
        List<Entity> list2 = pLevel.getEntities(pLivingEntity, aabb2);
        List<Entity> list3 = pLevel.getEntities(pLivingEntity, aabb3);
       // addParticle(pLevel, aabb1);
       // addParticle(pLevel, aabb2);
       // addParticle(pLevel, aabb3);

        boolean check = false;

        for (Entity entity : list1) {
            if (entity instanceof LivingEntity livingEntity){
                check = true;
                if (!pLevel.isClientSide){
                    livingEntity.hurt(pLevel.damageSources().playerAttack((Player) pLivingEntity), ModCommonConfigs.ANTI_ARMOR_CHARGE_DAMAGE.get());
                    applyArmorReductionEffect(livingEntity
                            ,getEffectAmplifier(livingEntity,ModMobEffects.ANTI_ARMOR.get())+1);//increases the level of the effect
                }
            }
        }
        for (Entity entity : list2) {
            if (entity instanceof LivingEntity livingEntity) {
                check = true;
                if (!pLevel.isClientSide){
                    livingEntity.hurt(pLevel.damageSources().playerAttack((Player) pLivingEntity), ModCommonConfigs.ANTI_ARMOR_CHARGE_DAMAGE.get());
                    applyArmorReductionEffect(livingEntity
                            ,getEffectAmplifier(livingEntity,ModMobEffects.ANTI_ARMOR.get())+1);//increases the level of the effect
                }
            }

        }
        for (Entity entity : list3) {
            if (entity instanceof LivingEntity livingEntity){
                if (!pLevel.isClientSide){
                    check = true;
                    livingEntity.hurt(pLevel.damageSources().playerAttack((Player) pLivingEntity), ModCommonConfigs.ANTI_ARMOR_CHARGE_DAMAGE.get());
                    applyArmorReductionEffect(livingEntity
                            ,getEffectAmplifier(livingEntity,ModMobEffects.ANTI_ARMOR.get())+1);//increases the level of the effect
                }
            }
        }
        stack.setDamageValue(5);
        if (check){
            pLevel.playSound(pLivingEntity,pLivingEntity.blockPosition(),SoundEvents.SHIELD_BREAK,SoundSource.PLAYERS,0.7f,1.3f);
        }
    }

    public int getEffectAmplifier(LivingEntity entity, MobEffect effect) {
        MobEffectInstance effectInstance = entity.getEffect(effect);
        if (effectInstance != null) {
            return Math.min(effectInstance.getAmplifier(), 1);
        }
        return 0;
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

    public static void spawnSlashParticles(LivingEntity player, Level level) {
        Vec3 forward = player.getViewVector(1.0F); // Direction the player is facing
        Vec3 right = forward.cross(new Vec3(0, 1, 0)).normalize(); // Right vector
        Vec3 up = right.cross(forward).normalize(); // Up vector, adjusted for player pitch

        double squareHalfSize = 0.5; // Half-size of the square's side (closer to player)
        double pointDistance = 3.2; // Distance of the central point from the player
        double cornerDistance = 1.0; // Distance of the corners from the player (closer)
        int particlesPerLine = 20; // Number of particles per line

        // Position the central point farther in front of the player
        Vec3 centerPoint = player.position().add(forward.scale(pointDistance));

        // Define the four corners of the square, but closer to the player
        Vec3[] squareCorners = new Vec3[] {
                player.position().add(forward.scale(cornerDistance)).add(right.scale(squareHalfSize)).add(up.scale(squareHalfSize)), // Top-right
                player.position().add(forward.scale(cornerDistance)).add(right.scale(-squareHalfSize)).add(up.scale(squareHalfSize)), // Top-left
                player.position().add(forward.scale(cornerDistance)).add(right.scale(-squareHalfSize)).add(up.scale(-squareHalfSize)), // Bottom-left
                player.position().add(forward.scale(cornerDistance)).add(right.scale(squareHalfSize)).add(up.scale(-squareHalfSize))  // Bottom-right
        };

        // Spawn particles along the lines connecting each corner to the central point
        for (Vec3 corner : squareCorners) {
            for (int i = 0; i <= particlesPerLine; i++) {
                double progress = (double) i / particlesPerLine;
                Vec3 particlePos = corner.add(centerPoint.subtract(corner).scale(progress));
                level.addParticle(ModParticles.ANTI_ARMOR_PARTICLES.get(), particlePos.x, particlePos.y+1.6, particlePos.z, 1, 0, 0);
            }
        }
    }

    @Override
    public int getUseDuration(ItemStack pStack) {return ModCommonConfigs.ANTI_ARMOR_CHARGE_DURATION.get();}

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BLOCK;
    }

    @OnlyIn(Dist.CLIENT)
    private void playSounds(Level pLevel, LivingEntity pLivingEntity){
        if (!pLevel.isClientSide()){
            return;
        }
        getManager();
        createInstance((Player) pLivingEntity);
        manager.play(instance);
    }
    @OnlyIn(Dist.CLIENT)
    private void stopSounds(Level pLevel, LivingEntity pLivingEntity){
        if (!pLevel.isClientSide()){
            return;
        }
        getManager();
        createInstance((Player) pLivingEntity);
        manager.stop(instance);
    }

    @OnlyIn(Dist.CLIENT)
    public void createInstance(Player player){

        instance = new AntiArmorChargeTickableSound(0.01f, player);

    }

    @OnlyIn(Dist.CLIENT)
    public void getManager(){
        if (manager == null){
            manager = Minecraft.getInstance().getSoundManager();
        }
    }

}
