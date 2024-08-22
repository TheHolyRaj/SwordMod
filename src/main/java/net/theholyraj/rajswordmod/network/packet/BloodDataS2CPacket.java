package net.theholyraj.rajswordmod.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.theholyraj.rajswordmod.world.item.util.ModCapabilities;

import java.util.function.Supplier;

public class BloodDataS2CPacket {
    private final int slotIndex;
    private final int customData;

    public BloodDataS2CPacket(int slotIndex, int customData) {
        this.slotIndex = slotIndex;
        this.customData = customData;
    }

    public BloodDataS2CPacket(FriendlyByteBuf buf) {
        this.slotIndex = buf.readInt();
        this.customData = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(slotIndex);
        buf.writeInt(customData);
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                ItemStack stack = player.getInventory().getItem(slotIndex);
                stack.getCapability(ModCapabilities.BLOOD_DATA_CAPABILITY).ifPresent(customData -> {
                    customData.setData(this.customData);
                });
            }
        });
        return true;
    }
}

