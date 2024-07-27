package net.theholyraj.rajswordmod.world.item.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.network.PacketDistributor;
import net.theholyraj.rajswordmod.network.ModMessages;
import net.theholyraj.rajswordmod.network.packet.BloodDataS2CPacket;
import net.theholyraj.rajswordmod.world.item.util.ModCapabilities;
import net.theholyraj.rajswordmod.world.item.util.bloodsword.BloodCapabilityProvider;
import net.theholyraj.rajswordmod.world.item.util.bloodsword.IBloodSwordData;

import javax.annotation.Nullable;

public class BloodSwordItem extends SwordItem {
    public BloodSwordItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        if (!pLevel.isClientSide() && pLivingEntity instanceof Player player) {
            if (pLivingEntity.getHealth() == pLivingEntity.getMaxHealth()) {
                pLivingEntity.stopUsingItem();
            } else if (getCustomData(pStack) == 0) {
                pLivingEntity.stopUsingItem();
            } else {
                if (pRemainingUseDuration % 5 == 0 && pRemainingUseDuration != 72000) {
                    pLivingEntity.heal(1);
                    setCustomData(player, pStack, getCustomData(pStack) - 1);
                }
            }
        }
        super.onUseTick(pLevel, pLivingEntity, pStack, pRemainingUseDuration);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.DRINK;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pUsedHand == InteractionHand.MAIN_HAND) {
            pPlayer.startUsingItem(pUsedHand);
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (pAttacker instanceof Player player && !player.level().isClientSide()) {
            int currentData = getCustomData(pStack);
            if (currentData < 20) {
                setCustomData(player, pStack, currentData + 1);
            }
        }
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new BloodCapabilityProvider();
    }

    public void setCustomData(Player player, ItemStack stack, int data) {
        stack.getCapability(ModCapabilities.BLOOD_DATA_CAPABILITY).ifPresent(customData -> {
            customData.setData(data);
            if (!player.level().isClientSide()) {
                int slotIndex = player.getInventory().findSlotMatchingItem(stack);
                ModMessages.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
                        new BloodDataS2CPacket(slotIndex, data));
            }
        });
    }

    public int getCustomData(ItemStack stack) {
        return stack.getCapability(ModCapabilities.BLOOD_DATA_CAPABILITY).map(IBloodSwordData::getData).orElse(0);
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
}
