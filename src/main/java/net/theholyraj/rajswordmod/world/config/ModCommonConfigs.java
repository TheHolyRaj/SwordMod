package net.theholyraj.rajswordmod.world.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModCommonConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> ARROW_RENDER_CHARGE_TIME;
    public static final ForgeConfigSpec.ConfigValue<Integer> ARROW_RENDER_COOLDOWN;

    public static final ForgeConfigSpec.ConfigValue<Integer> SENTINEL_SWORD_CHARGE_TIME;
    public static final ForgeConfigSpec.ConfigValue<Integer> SENTINEL_SWORD_COOLDOWN;


    public static final ForgeConfigSpec.ConfigValue<Integer> KNOCKBACK_SWORD_CHARGE_TIME;





    static {
        BUILDER.push("Configs for Sword Mod");

        ARROW_RENDER_CHARGE_TIME = BUILDER.comment("How long ArrowRender should be able to be used").define("ArrowRender Charge  Time", 50);
        ARROW_RENDER_COOLDOWN = BUILDER.comment("How long ArrowRender should go on cooldown").define("ArrowRender Cooldown", 100);

        SENTINEL_SWORD_CHARGE_TIME = BUILDER.comment("How long (in ticks) should SentinelSword charge!")
                .define("SentinelSword Charge Time", 20);
        SENTINEL_SWORD_COOLDOWN = BUILDER.comment("How long SentinelSword should go on cooldown").define("SentinelSword Cooldown", 100);


        KNOCKBACK_SWORD_CHARGE_TIME = BUILDER.comment("How long to MAX charge the KnockbackSword").define("KnockbackSword Charge Time",40);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
