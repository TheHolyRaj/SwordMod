package net.theholyraj.rajswordmod.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.theholyraj.rajswordmod.SwordMod;
import net.theholyraj.rajswordmod.network.packet.DeflectParticleS2CPacket;
import net.theholyraj.rajswordmod.network.packet.HolyFireParticleS2CPacket;

public class ModMessages {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(SwordMod.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        net.messageBuilder(DeflectParticleS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(DeflectParticleS2CPacket::new)
                .encoder(DeflectParticleS2CPacket::toBytes)
                .consumerMainThread(DeflectParticleS2CPacket::handle)
                .add();

        net.messageBuilder(HolyFireParticleS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(HolyFireParticleS2CPacket::new)
                .encoder(HolyFireParticleS2CPacket::toBytes)
                .consumerMainThread(HolyFireParticleS2CPacket::handle)
                .add();

        INSTANCE = net;
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToClients(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
}
