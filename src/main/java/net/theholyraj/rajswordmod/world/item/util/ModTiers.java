package net.theholyraj.rajswordmod.world.item.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.TierSortingRegistry;
import net.theholyraj.rajswordmod.SwordMod;
import net.theholyraj.rajswordmod.world.item.ModItems;

import java.util.List;

public class ModTiers {
    public static final Tier FABLED = TierSortingRegistry.registerTier(
            new ForgeTier(5, 2500, 9.0F, 4.0F, 20,
                    Tags.Blocks.NEEDS_NETHERITE_TOOL, () -> Ingredient.of()),
            new ResourceLocation(SwordMod.MODID, "fabled"), List.of(Tiers.NETHERITE), List.of());
}
