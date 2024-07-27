package net.theholyraj.rajswordmod.world.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModCommonConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> ARROW_RENDER_COOLDOWN;

    public static final ForgeConfigSpec.ConfigValue<Integer> SENTINEL_SWORD_CHARGE_TIME;
    public static final ForgeConfigSpec.ConfigValue<Integer> SENTINEL_SWORD_COOLDOWN;


    public static final ForgeConfigSpec.ConfigValue<Integer> KNOCKBACK_SWORD_CHARGE_TIME;
    public static final ForgeConfigSpec.ConfigValue<Integer> KNOCKBACK_SWORD_COOLDOWN;

    public static final ForgeConfigSpec.ConfigValue<Integer> HOLY_FIRE_DAMAGE;
    public static final ForgeConfigSpec.ConfigValue<Integer> MIN_HOLY_FIRE_DAMAGE;
    public static final ForgeConfigSpec.ConfigValue<Integer> HOLY_FIRE_DURATION;
    public static final ForgeConfigSpec.ConfigValue<Float> HOLY_EXPLOSION_CHANCE;

    public static final ForgeConfigSpec.ConfigValue<Float> GAIA_BLADE_PROJECTILE_DAMAGE_PERCENTAGE;





    static {
        BUILDER.push("Configs for Sword Mod");

        ARROW_RENDER_COOLDOWN = BUILDER.comment("How long ArrowRender should go on cooldown").define("ArrowRender Cooldown", 100);

        SENTINEL_SWORD_CHARGE_TIME = BUILDER.comment("How long (in ticks) should SentinelSword charge!")
                .define("SentinelSword Charge Time", 20);
        SENTINEL_SWORD_COOLDOWN = BUILDER.comment("How long SentinelSword should go on cooldown").define("SentinelSword Cooldown", 100);


        KNOCKBACK_SWORD_CHARGE_TIME = BUILDER.comment("How long to MAX charge LegBreaker").define("LegBreaker Charge Time",100);
        KNOCKBACK_SWORD_COOLDOWN = BUILDER.comment("How Long LegBreaker should go on cooldown?").define("LegBreaker Cooldown", 200);

        HOLY_FIRE_DAMAGE = BUILDER.comment("What percentage of a mobs health should HolyFire deal (does this about once per 10 ticks in duration)?").define("Holy Fire Health Percentage",3);
        MIN_HOLY_FIRE_DAMAGE = BUILDER.comment("What is the minimum damage (not percent) should holyFire deal?").define("Holy Fire Min Damage",2);
        HOLY_FIRE_DURATION = BUILDER.comment("How Long (in Ticks) should Holy Fire Last").define("HolyFire Duration",50);
        HOLY_EXPLOSION_CHANCE = BUILDER.comment("What is the Ramping Chance for a Holy Explosion to fire?").define("HolyExplosion Chance, 2.5 = 2.5%",2.5f);

        GAIA_BLADE_PROJECTILE_DAMAGE_PERCENTAGE = BUILDER.comment("What percent of the swords damage should the GaiaBlade Projectile deal?").define("GaiaProjectile Damage, 1=100%",0.5f);


        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
