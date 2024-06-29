package net.theholyraj.rajswordmod.world.item.util;

import net.minecraft.world.item.ItemStack;
import net.theholyraj.rajswordmod.world.config.ModCommonConfigs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HolySwordUtil {
    private static HashMap<ItemStack, Float> MAP;
    public static float getPercentage(ItemStack stack){
        if (MAP == null){
            createMap();
        }
        if (MAP.containsKey(stack)){
            MAP.put(stack,MAP.get(stack)+ ModCommonConfigs.HOLY_EXPLOSION_CHANCE.get());
        }
        else {
            MAP.put(stack,ModCommonConfigs.HOLY_EXPLOSION_CHANCE.get());
        }
        return MAP.get(stack);
    }
    public static void resetChance(ItemStack stack){
        MAP.remove(stack);
    }

    private static void createMap() {
        MAP = new HashMap<>();
    }
}
