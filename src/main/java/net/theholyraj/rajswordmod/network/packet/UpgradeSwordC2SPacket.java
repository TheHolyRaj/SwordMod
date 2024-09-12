package net.theholyraj.rajswordmod.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.Tags;
import net.minecraftforge.network.NetworkEvent;
import net.theholyraj.rajswordmod.client.particle.ModParticles;
import net.theholyraj.rajswordmod.network.ModMessages;
import net.theholyraj.rajswordmod.world.config.ModCommonConfigs;
import net.theholyraj.rajswordmod.world.item.ModItems;
import net.theholyraj.rajswordmod.world.item.custom.DeflectSwordItem;

import java.util.function.Supplier;

public class UpgradeSwordC2SPacket {

    public UpgradeSwordC2SPacket() {
    }

    public UpgradeSwordC2SPacket(FriendlyByteBuf buf) {
    }

    public void toBytes(FriendlyByteBuf buf) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            ServerPlayer player = supplier.get().getSender();
            if (player != null){
                ItemStack stack = player.getMainHandItem();
                if (stack.is(ModItems.DEFLECT_SWORD.get())){
                    if (DeflectSwordItem.getUpgradeData(stack) >= ModCommonConfigs.ARROW_RENDER_UPGRADE_AMOUNT.get() && !DeflectSwordItem.isUpgraded(stack)){
                        DeflectSwordItem.setUpgraded(stack, true);
                        ModMessages.sendToClients(new UpgradeSwordSERVERS2CPacket(player.getId()));
                    }
                }
            }
        });
        return true;
    }
}
