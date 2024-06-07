package net.theholyraj.rajswordmod.client.sound.custom;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.theholyraj.rajswordmod.client.sound.ModSounds;
import net.theholyraj.rajswordmod.world.item.ModItems;

public class ChargeTickableSound extends AbstractTickableSoundInstance {

    private final Player player;

    public ChargeTickableSound(float volume, Player player) {
        super(ModSounds.CHARGE_KNOCKBACK.get(),SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
        this.player = player;
        this.looping = false; // Set to true if the sound should loop
        this.delay = 0; // No delay
        this.volume = volume;
        this.pitch = volume;
    }

    @Override
    public void tick() {
        if (player.isAlive() && player.getMainHandItem().is(ModItems.KNOCKBACK_SWORD.get()) && player.isUsingItem()) {
            // Update sound position to player position
            this.x = (float) player.getX();
            this.y = (float) player.getY();
            this.z = (float) player.getZ();
        } else {
            // Stop the sound if the player is not alive
            this.stop();
        }
    }

    @Override
    public boolean isStopped() {
        return !player.isAlive()||!player.isUsingItem();
    }
}
