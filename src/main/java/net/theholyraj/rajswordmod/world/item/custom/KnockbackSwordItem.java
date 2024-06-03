package net.theholyraj.rajswordmod.world.item.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.theholyraj.rajswordmod.client.particle.ModParticles;

import java.util.ArrayList;
import java.util.List;

public class KnockbackSwordItem extends SwordItem {
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
        attack(pLevel, pLivingEntity);
     //   pLivingEntity.getBoundingBox().getV
    }

    public void attack(Level pLevel, LivingEntity pLivingEntity){
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
        double length = 3;
        double halfLength = length / 2.0;
        double width = 1;
        double halfWidth = width / 2.0;
        double height = 1;
        double halfHeight = height / 2.0;

        // Adjust the position to place the nearest face of the cuboid in front of the player
        Vec3 frontCenter = position.add(viewVector.scale(length / 2.0 + halfLength));

        // Calculate the eight corners of the cuboid
        Vec3 topFrontLeft = frontCenter.add(leftVector.scale(halfWidth)).add(upVector.scale(halfHeight));
        Vec3 topFrontRight = frontCenter.add(rightVector.scale(halfWidth)).add(upVector.scale(halfHeight));
        Vec3 bottomFrontLeft = frontCenter.add(leftVector.scale(halfWidth)).add(downVector.scale(halfHeight));
        Vec3 bottomFrontRight = frontCenter.add(rightVector.scale(halfWidth)).add(downVector.scale(halfHeight));
        Vec3 topBackLeft = frontCenter.add(leftVector.scale(halfWidth)).add(upVector.scale(halfHeight)).add(viewVector.scale(-length));
        Vec3 topBackRight = frontCenter.add(rightVector.scale(halfWidth)).add(upVector.scale(halfHeight)).add(viewVector.scale(-length));
        Vec3 bottomBackLeft = frontCenter.add(leftVector.scale(halfWidth)).add(downVector.scale(halfHeight)).add(viewVector.scale(-length));
        Vec3 bottomBackRight = frontCenter.add(rightVector.scale(halfWidth)).add(downVector.scale(halfHeight)).add(viewVector.scale(-length));


        pLevel.addParticle(ParticleTypes.BUBBLE,topFrontLeft.x, topFrontLeft.y, topFrontLeft.z, 0, 0, 0);
        pLevel.addParticle(ParticleTypes.BUBBLE,topFrontRight.x, topFrontRight.y, topFrontRight.z, 0, 0, 0);
        pLevel.addParticle(ParticleTypes.BUBBLE,bottomFrontLeft.x, bottomFrontLeft.y, bottomFrontLeft.z, 0, 0, 0);
        pLevel.addParticle(ParticleTypes.BUBBLE,bottomFrontRight.x, bottomFrontRight.y, bottomFrontRight.z, 0, 0, 0);
        pLevel.addParticle(ParticleTypes.BUBBLE,topBackLeft.x, topBackLeft.y, topBackLeft.z, 0, 0, 0);
        pLevel.addParticle(ParticleTypes.BUBBLE,topBackRight.x, topBackRight.y, topBackRight.z, 0, 0, 0);
        pLevel.addParticle(ParticleTypes.BUBBLE,bottomBackLeft.x, bottomBackLeft.y, bottomBackLeft.z, 0, 0, 0);
        pLevel.addParticle(ParticleTypes.BUBBLE,bottomBackRight.x, bottomBackRight.y, bottomBackRight.z, 0, 0, 0);


        Vec3 vectorFromCuboid = new Vec3(topFrontLeft.x - topBackLeft.x
                ,topFrontLeft.y - topBackLeft.y
                ,topFrontLeft.z - topBackLeft.z);

        Vec3 thirdVectorFromCuboid = new Vec3(vectorFromCuboid.x/3
                ,vectorFromCuboid.y/3
                ,vectorFromCuboid.z/3);

        Vec3 firstPosTopLeft1 = topBackLeft;
        Vec3 firstPosTopRight1 = topBackRight;
        Vec3 firstPosBottomLeft1 = bottomBackLeft;
        Vec3 firstPosBottomRight1 = bottomBackRight;

        Vec3 firstPosTopLeft2 = new Vec3(firstPosTopLeft1.x * thirdVectorFromCuboid.x
                , firstPosTopLeft1.y * thirdVectorFromCuboid.y
                , firstPosTopLeft1.z * thirdVectorFromCuboid.z) ;
        Vec3 firstPosTopRight2 = new Vec3(firstPosTopRight1.x * thirdVectorFromCuboid.x
                , firstPosTopRight1.y * thirdVectorFromCuboid.y
                , firstPosTopRight1.z * thirdVectorFromCuboid.z) ;
        Vec3 firstPosBottomLeft2 = new Vec3(firstPosBottomLeft1.x * thirdVectorFromCuboid.x
                , firstPosBottomLeft1.y * thirdVectorFromCuboid.y
                , firstPosBottomLeft1.z * thirdVectorFromCuboid.z) ;
        Vec3 firstPosBottomRight2 = new Vec3(firstPosBottomRight1.x * thirdVectorFromCuboid.x
                , firstPosBottomRight1.y * thirdVectorFromCuboid.y
                , firstPosBottomRight1.z * thirdVectorFromCuboid.z) ;
        pLevel.addParticle(ParticleTypes.SMOKE,firstPosTopLeft2.x,firstPosTopLeft2.y,firstPosTopLeft2.z, 0, 0, 0);
        pLevel.addParticle(ParticleTypes.SMOKE,firstPosTopRight2.x,firstPosTopRight2.y,firstPosTopRight2.z, 0, 0, 0);
        pLevel.addParticle(ParticleTypes.SMOKE,firstPosBottomLeft2.x,firstPosBottomLeft2.y,firstPosBottomLeft2.z, 0, 0, 0);
        pLevel.addParticle(ParticleTypes.SMOKE,firstPosBottomRight2.x,firstPosBottomRight2.y,firstPosBottomRight2.z, 0, 0, 0);


        Vec3 firstPosTopLeft3 = new Vec3(firstPosTopLeft2.x * thirdVectorFromCuboid.x
                , firstPosTopLeft2.y * thirdVectorFromCuboid.y
                , firstPosTopLeft2.z * thirdVectorFromCuboid.z) ;
        Vec3 firstPosTopRight3 = new Vec3(firstPosTopRight2.x * thirdVectorFromCuboid.x
                , firstPosTopRight2.y * thirdVectorFromCuboid.y
                , firstPosTopRight2.z * thirdVectorFromCuboid.z) ;
        Vec3 firstPosBottomLeft3 = new Vec3(firstPosBottomLeft2.x * thirdVectorFromCuboid.x
                , firstPosBottomLeft2.y * thirdVectorFromCuboid.y
                , firstPosBottomLeft2.z * thirdVectorFromCuboid.z) ;
        Vec3 firstPosBottomRight3 = new Vec3(firstPosBottomRight2.x * thirdVectorFromCuboid.x
                , firstPosBottomRight2.y * thirdVectorFromCuboid.y
                , firstPosBottomRight2.z * thirdVectorFromCuboid.z) ;

        Vec3 firstPosTopLeft4 = topFrontLeft;
        Vec3 firstPosTopRight4 = topFrontRight;
        Vec3 firstPosBottomLeft4 = bottomFrontLeft;
        Vec3 firstPosBottomRight4 = bottomFrontRight;

        AABB aabb1 = makeAABB(firstPosTopLeft1, firstPosTopRight1, firstPosBottomLeft1, firstPosBottomRight1
                , firstPosTopLeft2, firstPosTopRight2 ,firstPosBottomLeft2, firstPosBottomRight2);
        AABB aabb2 = makeAABB(firstPosTopLeft2, firstPosTopRight2, firstPosBottomLeft2, firstPosBottomRight2
                , firstPosTopLeft3, firstPosTopRight3 ,firstPosBottomLeft3, firstPosBottomRight3);
        AABB aabb3 = makeAABB(firstPosTopLeft3, firstPosTopRight3, firstPosBottomLeft3, firstPosBottomRight3
                , firstPosTopLeft4, firstPosTopRight4 ,firstPosBottomLeft4, firstPosBottomRight4);
       List<Entity> list = pLevel.getEntities(pLivingEntity, aabb1);
       for (Entity entity : list){
           entity.hurt(pLevel.damageSources().drown(), 4);
       }
        List<Entity> list2 = pLevel.getEntities(pLivingEntity, aabb2);
        for (Entity entity : list2){
            entity.hurt(pLevel.damageSources().drown(), 4);
        }
        List<Entity> list3 = pLevel.getEntities(pLivingEntity, aabb3);
        for (Entity entity : list3){
            entity.hurt(pLevel.damageSources().drown(), 4);
        }
    }

    public AABB makeAABB(Vec3 pos1, Vec3 pos2, Vec3 pos3, Vec3 pos4, Vec3 pos5, Vec3 pos6, Vec3 pos7, Vec3 pos8){
        double x1 = returnLowestNumber(pos1.x, pos2.x, pos3.x, pos4.x, pos5.x, pos6.x, pos7.x, pos8.x);
        double y1 = returnLowestNumber(pos1.y, pos2.y, pos3.y, pos4.y, pos5.y, pos6.y, pos7.y, pos8.y);
        double z1 = returnLowestNumber(pos1.z, pos2.z, pos3.z, pos4.z, pos5.z, pos6.z, pos7.z, pos8.z);
        double x2 = returnLowestNumber(pos1.x, pos2.x, pos3.x, pos4.x, pos5.x, pos6.x, pos7.x, pos8.x);
        double y2 = returnHighestNumber(pos1.y, pos2.y, pos3.y, pos4.y, pos5.y, pos6.y, pos7.y, pos8.y);
        double z2 = returnHighestNumber(pos1.z, pos2.z, pos3.z, pos4.z, pos5.z, pos6.z, pos7.z, pos8.z);

        return new AABB(x1,y1,z1,x2,y2,z2);
    }

    public double returnLowestNumber(double a, double b, double c, double d, double e, double f, double g, double h){
        if (a < b && a < c && a < d && a < e && a < f && a < g && a < h){
            return a;
        }
        else if (b < c && b < d && b < e && b < f && b < g && b < h){
            return b;
        }
        else if (c < d && c < e && c < f && c < g && c < h){
            return c;
        }
        else if (d < e && d < f && d < g && d < h){
            return d;
        }
        else if (e < f && e < g && e < h){
            return e;
        }
        else if (f < g && f < h){
            return f;
        }
        else if (g < h){
            return g;
        }
        else return h;
    }

    public double returnHighestNumber(double a, double b, double c, double d, double e, double f, double g, double h){
        if (a > b && a > c && a > d && a > e && a > f && a > g && a > h){
            return a;
        }
        else if (b > c && b > d && b > e && b > f && b > g && b > h){
            return b;
        }
        else if (c > d && c > e && c > f && c > g && c > h){
            return c;
        }
        else if (d > e && d > f && d > g && d > h){
            return d;
        }
        else if (e > f && e > g && e > h){
            return e;
        }
        else if (f > g && f > h){
            return f;
        }
        else if (g > h){
            return g;
        }
        else return h;
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
