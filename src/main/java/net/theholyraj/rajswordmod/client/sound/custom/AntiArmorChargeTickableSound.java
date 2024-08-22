package net.theholyraj.rajswordmod.client.sound.custom;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.theholyraj.rajswordmod.client.sound.ModSounds;
import net.theholyraj.rajswordmod.world.config.ModCommonConfigs;
import net.theholyraj.rajswordmod.world.item.ModItems;

public class AntiArmorChargeTickableSound extends AbstractTickableSoundInstance {

    private final Player player;
    private int timer;

    public AntiArmorChargeTickableSound(float volume, Player player) {
        super(SoundEvents.ELYTRA_FLYING,SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
        this.player = player;
        this.looping = true; // Set to true if the sound should loop
        this.delay = 200;
        this.volume = volume;
        this.pitch = volume;
        this.timer = 0;
    }

    @Override
    public void tick() {
        if (player.isAlive() && player.getMainHandItem().is(ModItems.ANTI_ARMOR_SWORD.get()) && player.isUsingItem()) {
            // Update sound position to player position
            this.x = (float) player.getX();
            this.y = (float) player.getY();
            this.z = (float) player.getZ();
            this.volume+=0.003;
            this.timer++;
            if (this.timer == ModCommonConfigs.ANTI_ARMOR_CHARGE_DURATION.get()/2){
                this.volume+=0.2;
            }
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
