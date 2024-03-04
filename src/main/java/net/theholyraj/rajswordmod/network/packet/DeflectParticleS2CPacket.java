package net.theholyraj.rajswordmod.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.network.NetworkEvent;
import net.theholyraj.rajswordmod.client.particle.ModParticles;
import net.theholyraj.rajswordmod.client.sound.ModSounds;

import java.util.function.Supplier;

public class DeflectParticleS2CPacket {
    private final double x;
    private final double y;
    private final double z;

    public DeflectParticleS2CPacket(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public DeflectParticleS2CPacket(FriendlyByteBuf buf) {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            Minecraft.getInstance().level.addParticle(ModParticles.DEFLECT_PARTICLES.get(),true,x,y,z,0,0,0);
            Minecraft.getInstance().level.playSound(null, x,y,z, ModSounds.PROJECTILE_SLASH.get(), SoundSource.PLAYERS,0.1f,1f);
        });
        return true;
    }
}
