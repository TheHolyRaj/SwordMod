package net.theholyraj.rajswordmod.world.mobeffects.custom;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

import java.io.IOException;

public class AntiArmorEffectInstance extends MobEffectInstance {

    public AntiArmorEffectInstance(MobEffect pEffect, int pDuration, int pAmplifier) {
        super(pEffect, pDuration, pAmplifier);
    }

    @Override
    public boolean update(MobEffectInstance pOther) {
        return super.update(pOther);
    }

    @Override
    public boolean showIcon() {
        return false;
    }

    @Override
    public boolean isVisible() {
        return false;
    }
}
