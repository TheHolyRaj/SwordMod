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

    private static final Map<String, Integer> upgradeTimers = new HashMap<>();

    public static void setUpgradeTimer(ItemStack stack, int time) {
        String swordUUID = DeflectSwordItem.getSwordUUID(stack);
        upgradeTimers.put(swordUUID, time);
    }

    public static int getUpgradeTimer(ItemStack stack) {
        String swordUUID = DeflectSwordItem.getSwordUUID(stack);
        return upgradeTimers.getOrDefault(swordUUID, 0);
    }

    public static void tick(ItemStack stack, ServerPlayer player) {
        String swordUUID = DeflectSwordItem.getSwordUUID(stack);
        int time = getUpgradeTimer(stack);

        if (time > 0) {
            upgradeTimers.put(swordUUID, time - 1);
        } else {
            upgradeTimers.remove(swordUUID);

            if (stack.is(ModItems.DEFLECT_SWORD.get())) {
                DeflectSwordItem.setUpgraded(stack, false);
                DeflectSwordItem.setUpgradeData(stack, 0);
            }
        }

        int slotIndex = player.getInventory().findSlotMatchingItem(stack);
        if (slotIndex != -1) {
            ModMessages.sendToPlayer(new SyncSwordTimerS2CPacket(getUpgradeTimer(stack), slotIndex), player);
        }
    }
}
