package net.theholyraj.rajswordmod.world.item.custom;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.theholyraj.rajswordmod.SwordMod;
import net.theholyraj.rajswordmod.world.explosion.HolyExplosion;
import net.theholyraj.rajswordmod.world.item.ModItems;
import net.theholyraj.rajswordmod.world.item.util.holysword.HolySwordUtil;

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
            double chance = HolySwordUtil.getPercentage(pStack)/100.0;
            if (random.nextDouble()<= chance){
                HolyExplosion explosion = new HolyExplosion(pAttacker.level(),pAttacker,pTarget.position().x,pTarget.position().y+pTarget.getBbHeight()/2,pTarget.position().z,2f);
                explosion.explode();
                explosion.finalizeExplosion(true);
                HolySwordUtil.resetChance(pStack);
            }
        }
        return super.hurtEnemy(pStack, pTarget, pAttacker);
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
