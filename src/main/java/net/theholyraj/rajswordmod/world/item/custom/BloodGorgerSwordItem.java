package net.theholyraj.rajswordmod.world.item.custom;

import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.IForgeBakedModel;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.theholyraj.rajswordmod.SwordMod;
import net.theholyraj.rajswordmod.world.config.ModCommonConfigs;
import net.theholyraj.rajswordmod.world.item.ModItems;
import net.theholyraj.rajswordmod.world.item.util.ModCapabilities;
import net.theholyraj.rajswordmod.world.item.util.bloodsword.BloodCapabilityProvider;
import net.theholyraj.rajswordmod.world.item.util.bloodsword.IBloodSwordData;
import net.theholyraj.rajswordmod.world.item.util.holysword.HolyCapabilityProvider;
import net.theholyraj.rajswordmod.world.item.util.holysword.IHolySwordData;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = SwordMod.MODID)
public class BloodGorgerSwordItem extends SwordItem {
    public BloodGorgerSwordItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pUsedHand == InteractionHand.MAIN_HAND){
            if (pPlayer.getMaxHealth() > pPlayer.getHealth()){
                pPlayer.startUsingItem(pUsedHand);
            }
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        if (pRemainingUseDuration%ModCommonConfigs.BLOOD_GORGER_HEAL_SPEED.get()==0){
            if (!pLevel.isClientSide()){
                if (pLivingEntity.getHealth() < pLivingEntity.getMaxHealth() && BloodGorgerSwordItem.getCustomData(pStack) > 0){
                    float customData = BloodGorgerSwordItem.getCustomData(pStack);
                    if (customData > 0){
                        pLivingEntity.heal(1);
                        BloodGorgerSwordItem.setCustomData(pStack, Math.max(customData-1,0));
                    }
                }
                else {
                    pLivingEntity.stopUsingItem();
                }
            }
        }
        super.onUseTick(pLevel, pLivingEntity, pStack, pRemainingUseDuration);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new BloodCapabilityProvider();
    }

    public static void setCustomData(ItemStack stack, float data) {
        stack.getCapability(ModCapabilities.BLOOD_DATA_CAPABILITY).ifPresent(customData -> {
            customData.setData(data);
        });
    }

    public static float getCustomData(ItemStack stack) {
        return stack.getCapability(ModCapabilities.BLOOD_DATA_CAPABILITY).map(IBloodSwordData::getData).orElse(0f);
    }

    @Override
    public CompoundTag getShareTag(ItemStack stack) {
        CompoundTag nbt = super.getShareTag(stack);
        if (nbt == null) {
            nbt = new CompoundTag();
        }
        final CompoundTag finalNbt = nbt;
        stack.getCapability(ModCapabilities.BLOOD_DATA_CAPABILITY).ifPresent(cap -> {
            CompoundTag capTag = new CompoundTag();
            cap.writeToNBT(capTag);
            finalNbt.put("blood_data", capTag);
        });
        return nbt;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
        super.readShareTag(stack, nbt);
        if (nbt != null && nbt.contains("blood_data")) {
            stack.getCapability(ModCapabilities.BLOOD_DATA_CAPABILITY).ifPresent(cap -> {
                cap.readFromNBT(nbt.getCompound("blood_data"));
            });
        }
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.DRINK;
    }


    //////////////////////////////////////////EVENT////////////////////////////////////////////////////////////

    @SubscribeEvent
    public static void storeDamage(LivingDamageEvent event){
        if (event.getSource().getEntity() instanceof Player player){
            if (player.getMainHandItem().is(ModItems.BLOOD_SWORD.get())){
                float customData = BloodGorgerSwordItem.getCustomData(player.getMainHandItem());
                if (customData < ModCommonConfigs.BLOOD_GORGER_STORED_HEALTH_MAX.get()){
                    customData = (customData + (event.getAmount()*ModCommonConfigs.BLOOD_GORGER_LIFESTEAL_PERCENT.get()));
                    if (customData > ModCommonConfigs.BLOOD_GORGER_STORED_HEALTH_MAX.get()){
                        BloodGorgerSwordItem.setCustomData(player.getMainHandItem(),ModCommonConfigs.BLOOD_GORGER_STORED_HEALTH_MAX.get());
                    }
                    else {
                        BloodGorgerSwordItem.setCustomData(player.getMainHandItem(),customData);
                    }
                }
            }
        }
    }
}
