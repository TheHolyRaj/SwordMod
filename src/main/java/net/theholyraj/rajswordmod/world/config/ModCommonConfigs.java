package net.theholyraj.rajswordmod.world.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModCommonConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> ARROW_RENDER_CHARGE_TIME;
    public static final ForgeConfigSpec.ConfigValue<Integer> ARROW_RENDER_COOLDOWN;

    public static final ForgeConfigSpec.ConfigValue<Integer> SENTINEL_SWORD_CHARGE_TIME;




    static {
        BUILDER.push("Configs for Sword Mod");

        ARROW_RENDER_CHARGE_TIME = BUILDER.comment("How long (in ticks) should ArrowRender need to charge!")
                .define("ArrowRender Charge Time", 40);
        ARROW_RENDER_COOLDOWN = BUILDER.comment("How long (in ticks) should ArrowRender be on cooldown after use!")
                .define("ArrowRender Cooldown", 50);

        SENTINEL_SWORD_CHARGE_TIME = BUILDER.comment("How long (in ticks) should SentinelSword charge!")
                .define("ArrowRender Charge Time", 20);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
