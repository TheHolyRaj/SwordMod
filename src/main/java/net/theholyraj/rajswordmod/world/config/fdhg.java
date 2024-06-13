package net.theholyraj.rajswordmod.world.config;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.level.Level;

public class fdhg extends WitherBoss {
    public fdhg(EntityType<? extends WitherBoss> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public boolean removeEffect(MobEffect pEffect) {
        return super.removeEffect(pEffect);
    }
}
