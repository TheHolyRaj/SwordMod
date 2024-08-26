package net.theholyraj.rajswordmod.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.theholyraj.rajswordmod.world.item.util.ModCapabilities;

import java.util.UUID;
import java.util.function.Supplier;

public class CloneUUIDS2CPacket {
    private final UUID playerUUID;

    public CloneUUIDS2CPacket(UUID playerUUID) {
        this.playerUUID =playerUUID;
    }

    public CloneUUIDS2CPacket(FriendlyByteBuf buf) {
        this.playerUUID = buf.readUUID();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(playerUUID);
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {

        });
        return true;
    }
}
