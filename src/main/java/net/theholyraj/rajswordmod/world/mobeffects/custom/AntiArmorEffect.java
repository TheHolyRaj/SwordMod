package net.theholyraj.rajswordmod.world.mobeffects.custom;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;


public class AntiArmorEffect extends MobEffect {
    private static final UUID ARMOR_REDUCTION_UUID = UUID.fromString("91AEAA56-376B-4498-935B-2F7F68070635");
    private static final UUID ARMOR_TOUGHNESS_REDUCTION_UUID = UUID.fromString("91AEAA56-376B-4498-935B-2F7F68070636");


    public AntiArmorEffect(MobEffectCategory pCategory, int pColor) {
        super(MobEffectCategory.HARMFUL, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity.getAttribute(Attributes.ARMOR) != null) {
            pLivingEntity.getAttribute(Attributes.ARMOR).removeModifier(ARMOR_REDUCTION_UUID);

            double reductionAmount = -0.1 * (pAmplifier + 1); // Each level reduces 10% more

            pLivingEntity.getAttribute(Attributes.ARMOR).addTransientModifier(new AttributeModifier(
                    ARMOR_REDUCTION_UUID,
                    "Armor Toughness Reduction",
                    reductionAmount,
                    AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
        if (pLivingEntity.getAttribute(Attributes.ARMOR_TOUGHNESS) != null) {
            pLivingEntity.getAttribute(Attributes.ARMOR_TOUGHNESS).removeModifier(ARMOR_TOUGHNESS_REDUCTION_UUID);

            double reductionAmount = -0.1 * (pAmplifier + 1); // Each level reduces 10% more

            pLivingEntity.getAttribute(Attributes.ARMOR_TOUGHNESS).addTransientModifier(new AttributeModifier(
                    ARMOR_TOUGHNESS_REDUCTION_UUID,
                    "Armor Toughness Reduction",
                    reductionAmount,
                    AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
        System.out.println(pLivingEntity.getArmorValue());

        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
        if (entity.getAttribute(Attributes.ARMOR) != null) {
            entity.getAttribute(Attributes.ARMOR).removeModifier(ARMOR_REDUCTION_UUID);
        }
        super.removeAttributeModifiers(entity, attributeMap, amplifier);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    @Override
    public @NotNull String getDescriptionId() {
        return "effect.rajswordmod.anti_armor_effect";
    }
}
