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
        makeBlockingItem(ModItems.KNOCKBACK_SWORD.get());
    }


    private static void makeBlockingItem(Item item){
        ItemProperties.register(item, new ResourceLocation("blocking"), (p_174575_, p_174576_, p_174577_, p_174578_) -> {
            return p_174577_ != null && p_174577_.isUsingItem() && p_174577_.getUseItem() == p_174575_ ? 1.0F : 0.0F;});
    }

}
