package net.theholyraj.rajswordmod.world.item.util.upgradesword;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.theholyraj.rajswordmod.network.ModMessages;
import net.theholyraj.rajswordmod.network.packet.SyncSwordTimerS2CPacket;
import net.theholyraj.rajswordmod.world.item.ModItems;
import net.theholyraj.rajswordmod.world.item.custom.DeflectSwordItem;

import java.util.HashMap;
import java.util.Map;

public class UpgradeSwordTimer {

    private static final Map<ItemStack, Integer> upgradeTimers = new HashMap<>();

    public static void setUpgradeTimer(ItemStack stack, int time) {
        upgradeTimers.put(stack, time);
    }
    public static int getUpgradeTimer(ItemStack stack) {
        return upgradeTimers.getOrDefault(stack, 0);
    }
    public static void tick(ItemStack stack, ServerPlayer player) {
        int time = getUpgradeTimer(stack);
        if (time > 0) {
            upgradeTimers.put(stack, time - 1);
        } else {
            upgradeTimers.remove(stack);
            if (stack.is(ModItems.DEFLECT_SWORD.get())){
                DeflectSwordItem.setUpgraded(stack,false);
                DeflectSwordItem.setUpgradeData(stack,0);
            }
        }
        ModMessages.sendToPlayer(new SyncSwordTimerS2CPacket(getUpgradeTimer(stack),player.getInventory().selected), player);
    }
}
