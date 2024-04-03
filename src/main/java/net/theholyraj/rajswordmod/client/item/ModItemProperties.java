package net.theholyraj.rajswordmod.client.item;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.theholyraj.rajswordmod.world.item.ModItems;

public class ModItemProperties {
    public static void makeProperties(){
        makeBlockingItem(ModItems.DEFLECT_SWORD.get());
        makeBlockingItem(ModItems.SENTINEL_SWORD.get());
        itemReady(ModItems.SENTINEL_SWORD.get());
    }


    private static void makeBlockingItem(Item item){
        ItemProperties.register(item, new ResourceLocation("blocking"), (p_174575_, p_174576_, p_174577_, p_174578_) -> {
            return p_174577_ != null && p_174577_.isUsingItem() && p_174577_.getUseItem() == p_174575_ ? 1.0F : 0.0F;});
    }

    private static void itemReady(Item item){
        ItemProperties.register(item, new ResourceLocation("ready"), (p_174575_, p_174576_, p_174577_, p_174578_) -> {
            return p_174577_ != null  && p_174577_.getMainHandItem() == p_174575_
                    && p_174575_.hasTag() && p_174575_.getTag().contains("damage") && p_174575_.getTag().getFloat("damage") > 0 ? 1.0F : 0.0F;});
    }
}
