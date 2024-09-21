package net.theholyraj.rajswordmod.world.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModCommonConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> ARROW_RENDER_COOLDOWN;
    public static final ForgeConfigSpec.ConfigValue<Integer> ARROW_RENDER_DURABILITY_USE;
    public static final ForgeConfigSpec.ConfigValue<Integer> ARROW_RENDER_ABILITY_USE_TIME;

    public static final ForgeConfigSpec.ConfigValue<Integer> ARROW_RENDER_UPGRADE_AMOUNT;
    public static final ForgeConfigSpec.ConfigValue<Integer> ARROW_RENDER_UPGRADE_COOLDOWN;
    public static final ForgeConfigSpec.ConfigValue<Integer> ARROW_RENDER_UPGRADE_TIME;



    public static final ForgeConfigSpec.ConfigValue<Integer> SENTINEL_SWORD_CHARGE_TIME;
    public static final ForgeConfigSpec.ConfigValue<Integer> SENTINEL_SWORD_COOLDOWN;


    public static final ForgeConfigSpec.ConfigValue<Integer> KNOCKBACK_SWORD_CHARGE_TIME;
    public static final ForgeConfigSpec.ConfigValue<Integer> KNOCKBACK_SWORD_COOLDOWN;

    public static final ForgeConfigSpec.ConfigValue<Integer> HOLY_FIRE_DAMAGE;
    public static final ForgeConfigSpec.ConfigValue<Integer> MIN_HOLY_FIRE_DAMAGE;
    public static final ForgeConfigSpec.ConfigValue<Integer> HOLY_FIRE_DURATION;
    public static final ForgeConfigSpec.ConfigValue<Float> HOLY_EXPLOSION_CHANCE;

    public static final ForgeConfigSpec.ConfigValue<Float> GAIA_BLADE_PROJECTILE_DAMAGE_PERCENTAGE;

    public static final ForgeConfigSpec.ConfigValue<Float> BLOOD_GORGER_STORED_HEALTH_MAX;
    public static final ForgeConfigSpec.ConfigValue<Float> BLOOD_GORGER_LIFESTEAL_PERCENT;
    public static final ForgeConfigSpec.ConfigValue<Integer> BLOOD_GORGER_HEAL_SPEED;

    public static final ForgeConfigSpec.ConfigValue<Integer> ANTI_ARMOR_EFFECT_DURATION;
    public static final ForgeConfigSpec.ConfigValue<Float> ANTI_ARMOR_EFFECT_REDUCTION;
    public static final ForgeConfigSpec.ConfigValue<Integer> ANTI_ARMOR_CHARGE_DURATION;
    public static final ForgeConfigSpec.ConfigValue<Integer> ANTI_ARMOR_CHARGE_COOLDOWN;
    public static final ForgeConfigSpec.ConfigValue<Integer> ANTI_ARMOR_CHARGE_DAMAGE;

    public static final ForgeConfigSpec.ConfigValue<Integer> DARK_DECEIVER_COOLDOWN;
    public static final ForgeConfigSpec.ConfigValue<Integer> SHADOW_CLONE_LIFESPAN;

    public static final ForgeConfigSpec.ConfigValue<Integer> JUNGLE_TEMPLE_SWORD_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.ConfigValue<Integer> DESERT_PYRAMID_SWORD_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.ConfigValue<Integer> NETHER_FORTRESS_SWORD_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.ConfigValue<Integer> BASTION_SWORD_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.ConfigValue<Integer> STRONGHOLD_SWORD_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.ConfigValue<Integer> WOODLAND_MANSION_SWORD_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.ConfigValue<Integer> ABANDONED_MINESHAFT_SWORD_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.ConfigValue<Integer> DUNGEON_SWORD_SPAWN_WEIGHT;



    static {
        BUILDER.push("Configs for Sword Mod");

        ARROW_RENDER_COOLDOWN = BUILDER.comment("How long (in ticks) ArrowRender should go on cooldown").define("ArrowRender Cooldown", 200);
        ARROW_RENDER_DURABILITY_USE = BUILDER.comment("How much durability should ArrowRender use when finished").define("ArrowRender Durability Cost",20);
        ARROW_RENDER_ABILITY_USE_TIME = BUILDER.comment("How long should the player be allowed to use ArrowRenders Ability (in ticks)").define("ArrowRender Use Time",200);

        ARROW_RENDER_UPGRADE_AMOUNT = BUILDER.comment("How many Projectiles need to be deleted for upgrade").define("ArrowRender Upgrade Amount",5);
        ARROW_RENDER_UPGRADE_COOLDOWN = BUILDER.comment("How long (in ticks) ArrowRenderUpgraded should go on cooldown").define("ArrowRenderUpgraded Cooldown",150);
        ARROW_RENDER_UPGRADE_TIME = BUILDER.comment("How Long can ArrowRender be upgraded (in ticks)").define("ArrowRender Upgrade Time",3600);

        SENTINEL_SWORD_CHARGE_TIME = BUILDER.comment("How long (in ticks) should SentinelSword be used for")
                .define("SentinelSword Charge Time", 10);
        SENTINEL_SWORD_COOLDOWN = BUILDER.comment("How long (in ticks) SentinelSword should go on cooldown").define("SentinelSword Cooldown", 100);

        KNOCKBACK_SWORD_CHARGE_TIME = BUILDER.comment("How long (in ticks) to MAX charge LegBreaker").define("LegBreaker Charge Time",100);
        KNOCKBACK_SWORD_COOLDOWN = BUILDER.comment("How Long (in ticks) LegBreaker should go on cooldown?").define("LegBreaker Cooldown", 300);

        HOLY_FIRE_DAMAGE = BUILDER.comment("What percentage of a mobs health should HolyFire deal (does this about once per 10 ticks in duration)").define("Holy Fire Health Percentage",3);
        MIN_HOLY_FIRE_DAMAGE = BUILDER.comment("What is the minimum damage (not percent) should holyFire deal?").define("Holy Fire Min Damage",2);
        HOLY_FIRE_DURATION = BUILDER.comment("How Long (in Ticks) should Holy Fire Last").define("HolyFire Duration",50);
        HOLY_EXPLOSION_CHANCE = BUILDER.comment("What is the Ramping Chance for a Holy Explosion to fire?").define("HolyExplosion Chance, 2.5 = 2.5%",2.5f);

        GAIA_BLADE_PROJECTILE_DAMAGE_PERCENTAGE = BUILDER.comment("What percent of the swords damage should the GaiaBlade Projectile deal?").define("GaiaProjectile Damage, 1=100%",0.5f);

        BLOOD_GORGER_STORED_HEALTH_MAX = BUILDER.comment("How much health should BloodGorger be able to store").define("BloodGorger Max Health", 20f);
        BLOOD_GORGER_LIFESTEAL_PERCENT = BUILDER.comment("What Percentage should BloodGorger lifesteal be").define("BloodGorger Lifesteal Percentage, 1=100%", 0.15f);
        BLOOD_GORGER_HEAL_SPEED = BUILDER.comment("How fast should BloodGorger heal the player (in ticks)").define("BloodGorger Heal Speed",5);

        ANTI_ARMOR_EFFECT_DURATION = BUILDER.comment("How long (in ticks) should the anti armor effect last . Should be longer than the ArmorPiercer cooldown").define("AntiArmor Effect Duration",350);
        ANTI_ARMOR_EFFECT_REDUCTION = BUILDER.comment("How much Percent should anti armor effect reduce?").define("AntiArmor Effect Percentage Per Level (1 = 100%)",0.2f);
        ANTI_ARMOR_CHARGE_DURATION = BUILDER.comment("How long (in ticks) should the player be avle to charge ArmorPiercer").define("ArmorPiercer Charge Duration", 100);
        ANTI_ARMOR_CHARGE_COOLDOWN = BUILDER.comment().define("How long (in ticks) should ArmorPiercer go on cooldown",300);
        ANTI_ARMOR_CHARGE_DAMAGE = BUILDER.comment("How much damage should ArmorPiercer's charge do?").define("ArmorPiercer charge damage",8);

        DARK_DECEIVER_COOLDOWN = BUILDER.comment(" How long (in ticks) should DarkDeceiver go on cooldown").define("DarkDeciever Cooldown", 420);
        SHADOW_CLONE_LIFESPAN = BUILDER.comment("How long (in ticks) should a DarkDeceiver clone last").define("Clone Lifespan",140);

        JUNGLE_TEMPLE_SWORD_SPAWN_WEIGHT = BUILDER.comment("How rare should the swords be in jungle temples?").define("Jungle Temple Weight",4);
        DESERT_PYRAMID_SWORD_SPAWN_WEIGHT = BUILDER.comment("How rare should the swords be in desert temples?").define("Desert Temple Weight",3);
        NETHER_FORTRESS_SWORD_SPAWN_WEIGHT = BUILDER.comment("How rare should the swords be in nether fortresses?").define("Nether Fortress Weight",3);
        BASTION_SWORD_SPAWN_WEIGHT = BUILDER.comment("How rare should the swords be in bastion remnants?").define("Bastion Remnant Weight",3);
        STRONGHOLD_SWORD_SPAWN_WEIGHT = BUILDER.comment("How rare should the swords be in strongholds?").define("Stronghold Weight",4);
        WOODLAND_MANSION_SWORD_SPAWN_WEIGHT = BUILDER.comment("How rare should the swords be in woodland mansions?").define("Woodland Mansion Weight",4);
        ABANDONED_MINESHAFT_SWORD_SPAWN_WEIGHT = BUILDER.comment("How rare should the swords be in abandoned mineshafts?").define("Abandoned Mineshaft Weight",3);
        DUNGEON_SWORD_SPAWN_WEIGHT = BUILDER.comment("How rare should the swords be in dungeons?").define("Dingeon Weight",3);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
