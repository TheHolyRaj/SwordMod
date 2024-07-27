package net.theholyraj.rajswordmod.world.item.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.theholyraj.rajswordmod.SwordMod;
import net.theholyraj.rajswordmod.world.config.ModCommonConfigs;
import net.theholyraj.rajswordmod.world.explosion.HolyExplosion;
import net.theholyraj.rajswordmod.world.item.ModItems;
import net.theholyraj.rajswordmod.world.item.util.ModCapabilities;
import net.theholyraj.rajswordmod.world.item.util.holysword.HolyCapabilityProvider;
import net.theholyraj.rajswordmod.world.item.util.holysword.IHolySwordData;

import javax.annotation.Nullable;
import java.util.Random;

@Mod.EventBusSubscriber(modid = SwordMod.MODID)
public class HolySwordItem extends SwordItem {
    public HolySwordItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (pTarget.getMobType() == MobType.UNDEAD){
            Random random = new Random();
            float chance = getCustomData(pStack);
            System.out.println(chance);
            if (random.nextFloat(0,100)<= chance){
                HolyExplosion explosion = new HolyExplosion(pAttacker.level(),pAttacker,pTarget.position().x,pTarget.position().y+pTarget.getBbHeight()/2,pTarget.position().z,2f);
                explosion.explode();
                explosion.finalizeExplosion(true);
                setCustomData(pStack, ModCommonConfigs.HOLY_EXPLOSION_CHANCE.get());
            }
            else {
                setCustomData(pStack,chance+ModCommonConfigs.HOLY_EXPLOSION_CHANCE.get());
            }
        }
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new HolyCapabilityProvider();
    }

    public void setCustomData(ItemStack stack, float data) {
        stack.getCapability(ModCapabilities.HOLY_DATA_CAPABILITY).ifPresent(customData -> {
            customData.setData(data);
        });
    }

    public float getCustomData(ItemStack stack) {
        return stack.getCapability(ModCapabilities.HOLY_DATA_CAPABILITY).map(IHolySwordData::getData).orElse(2.5f);
    }

    @Override
    public CompoundTag getShareTag(ItemStack stack) {
        CompoundTag nbt = super.getShareTag(stack);
        if (nbt == null) {
            nbt = new CompoundTag();
        }
        final CompoundTag finalNbt = nbt;
        stack.getCapability(ModCapabilities.HOLY_DATA_CAPABILITY).ifPresent(cap -> {
            CompoundTag capTag = new CompoundTag();
            cap.writeToNBT(capTag);
            finalNbt.put("holy_data", capTag);
        });
        return nbt;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
        super.readShareTag(stack, nbt);
        if (nbt != null && nbt.contains("holy_data")) {
            stack.getCapability(ModCapabilities.HOLY_DATA_CAPABILITY).ifPresent(cap -> {
                cap.readFromNBT(nbt.getCompound("holy_data"));
            });
        }
    }


    /////////////////////////////////////////////EVENT//////////////////////////////////////////
    @SubscribeEvent
    public static void doHolyExplosion(LivingDeathEvent event){
        LivingEntity entity = event.getEntity();
        if (!event.getSource().isIndirect()){
            if (event.getSource().getEntity() != null && event.getSource().getEntity() instanceof Player player){
                if (player.getMainHandItem().is(ModItems.HOLY_SWORD.get())){
                    if (entity.getMobType() == MobType.UNDEAD){
                        HolyExplosion explosion = new HolyExplosion(player.level(),player,entity.position().x,entity.position().y+entity.getBbHeight()/2,entity.position().z,2f);
                        explosion.explode();
                        explosion.finalizeExplosion(true);
                    }
                }
            }
        }
    }

}
