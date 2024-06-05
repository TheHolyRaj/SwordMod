package net.theholyraj.rajswordmod.world.item.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.IForgeBakedModel;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.theholyraj.rajswordmod.SwordMod;
import net.theholyraj.rajswordmod.client.sound.ModSounds;
import net.theholyraj.rajswordmod.world.config.ModCommonConfigs;
import net.theholyraj.rajswordmod.world.item.ModItems;

import java.util.Random;

@Mod.EventBusSubscriber(modid = SwordMod.MODID)
public class SentinelSwordItem extends SwordItem {
    public SentinelSwordItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pUsedHand == InteractionHand.MAIN_HAND){
            pPlayer.startUsingItem(pUsedHand);
            ItemStack pStack = pPlayer.getItemInHand(pUsedHand);
            if (pStack.hasTag()){
                pStack.getTag().putFloat("damage", 0);
            }
            else {
                pStack.setTag(new CompoundTag());
                pStack.getTag().putFloat("damage", 0);
            }
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public void onStopUsing(ItemStack stack, LivingEntity entity, int count) {
        if (entity instanceof Player player){
            player.getCooldowns().addCooldown(this, ModCommonConfigs.SENTINEL_SWORD_COOLDOWN.get());
        }
        super.onStopUsing(stack, entity, count);
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        if (pLevel.isClientSide){
            pLivingEntity.setDeltaMovement(Vec3.ZERO);
            Random random = new Random();
            float x = random.nextFloat(-0.75f,0.75f);
            float y = random.nextFloat(-1.5f,1.5f);
            float z = random.nextFloat(-0.75f,0.75f);
            pLevel.addParticle(ParticleTypes.CLOUD, pLivingEntity.position().x+x, pLivingEntity.position().y+1+y, pLivingEntity.position().z+z, 0.0D, 0.0D, 0.0D);
        }
        pLivingEntity.resetFallDistance();
        super.onUseTick(pLevel, pLivingEntity, pStack, pRemainingUseDuration);
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return ModCommonConfigs.SENTINEL_SWORD_CHARGE_TIME.get();
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BLOCK;
    }
    //////////////////////////////////////////EVENT//////////////////////////////////////////////////////

    @SubscribeEvent
    public static void blockDamageEvent(LivingAttackEvent event){
        if (event.getEntity() instanceof Player player){
            if (player.getMainHandItem().is(ModItems.SENTINEL_SWORD.get())){
                if (player.isUsingItem() && player.getUseItem().is(ModItems.SENTINEL_SWORD.get())){
                    event.getEntity().level().playSound(null, player.blockPosition(), ModSounds.BLOCK_DAMAGE.get(),SoundSource.PLAYERS,1f,1f);
                    event.setCanceled(true);

                }
            }
        }
    }
}

