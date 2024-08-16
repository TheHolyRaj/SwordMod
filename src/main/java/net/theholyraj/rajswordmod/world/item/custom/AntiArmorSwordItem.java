package net.theholyraj.rajswordmod.world.item.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Random;

public class AntiArmorSwordItem extends SwordItem {
    public AntiArmorSwordItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
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
        int time = (50 - pRemainingUseDuration)/25;
        Vec3 viewVector = pLivingEntity.getViewVector(1f);
        Vec3 movementVector = new Vec3(viewVector.x/2, -0.5, viewVector.z/2);
        Vec3 movementVectorCheck = new Vec3(viewVector.x/2, 0.5, viewVector.z/2);


        // Get the bounding box of the entity
        AABB boundingBox = pLivingEntity.getBoundingBox();

        // Create an expanded bounding box in the direction of movement
        AABB expandedBox = boundingBox.expandTowards(movementVectorCheck);

        // Check if any block in the expanded box collides
        if (!pLevel.noCollision(expandedBox)) {
            System.out.println("yes");
            // Apply upward velocity to simulate a jump
            pLivingEntity.setDeltaMovement(pLivingEntity.getDeltaMovement().add(0, 0.8, 0));
        }
        else {
            // Apply the movement vector
            pLivingEntity.setDeltaMovement(movementVector.scale(1+time));
        }

        super.onUseTick(pLevel, pLivingEntity, pStack, pRemainingUseDuration);
    }

    @Override
    public void onStopUsing(ItemStack stack, LivingEntity entity, int count) {
        if (entity instanceof Player player){
            attack(player.level(),player,1,stack);
            player.setDeltaMovement(new Vec3(0,0,0));
            player.getCooldowns().addCooldown(this,20);
        }
        super.onStopUsing(stack, entity, count);
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


        for (Entity entity : list1) {
            if (!pLevel.isClientSide){
                entity.hurt(pLevel.damageSources().playerAttack((Player) pLivingEntity), 6);
            }
        }
        for (Entity entity : list2) {
            if (!pLevel.isClientSide) {
                entity.hurt(pLevel.damageSources().playerAttack((Player) pLivingEntity), 6);
                stack.setDamageValue(4*charge);
            }

        }
        for (Entity entity : list3) {
            if (!pLevel.isClientSide){
                entity.hurt(pLevel.damageSources().playerAttack((Player) pLivingEntity), 6);

            }
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
        pLevel.addParticle(ParticleTypes.ENCHANTED_HIT,vec3.x, vec3.y, vec3.z, 0, 0, 0);

    }
    public void addParticle(Level pLevel, AABB aabb){
        pLevel.addParticle(ParticleTypes.ENCHANTED_HIT,aabb.minX, aabb.minY, aabb.minZ, 0, 0, 0);
        pLevel.addParticle(ParticleTypes.ENCHANTED_HIT,aabb.minX, aabb.maxY, aabb.minZ, 0, 0, 0);
        pLevel.addParticle(ParticleTypes.ENCHANTED_HIT,aabb.minX, aabb.minY, aabb.maxZ, 0, 0, 0);
        pLevel.addParticle(ParticleTypes.ENCHANTED_HIT,aabb.minX, aabb.maxY, aabb.maxZ, 0, 0, 0);

        pLevel.addParticle(ParticleTypes.ENCHANTED_HIT,aabb.maxX, aabb.minY, aabb.minZ, 0, 0, 0);
        pLevel.addParticle(ParticleTypes.ENCHANTED_HIT,aabb.maxX, aabb.maxY, aabb.minZ, 0, 0, 0);
        pLevel.addParticle(ParticleTypes.ENCHANTED_HIT,aabb.maxX, aabb.minY, aabb.maxZ, 0, 0, 0);
        pLevel.addParticle(ParticleTypes.ENCHANTED_HIT,aabb.maxX, aabb.maxY, aabb.maxZ, 0, 0, 0);
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 50;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BLOCK;
    }

}
