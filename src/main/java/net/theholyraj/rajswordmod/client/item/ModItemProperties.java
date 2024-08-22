package net.theholyraj.rajswordmod.client.item;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.theholyraj.rajswordmod.world.item.ModItems;
import net.theholyraj.rajswordmod.world.item.custom.BloodSwordItem;

public class ModItemProperties {
    public static void makeProperties(){
        makeBlockingItem(ModItems.DEFLECT_SWORD.get());
        makeBlockingItem(ModItems.SENTINEL_SWORD.get());
        makeBlockingItem(ModItems.KNOCKBACK_SWORD.get());
        makeBlockingItem(ModItems.ANTI_ARMOR_SWORD.get());


        registerCustomProperty(ModItems.BLOOD_SWORD.get());
    }


    private static void makeBlockingItem(Item item){
        ItemProperties.register(item, new ResourceLocation("blocking"), (p_174575_, p_174576_, p_174577_, p_174578_) -> {
            return p_174577_ != null && p_174577_.isUsingItem() && p_174577_.getUseItem() == p_174575_ ? 1.0F : 0.0F;});
    }

    private static void registerCustomProperty(Item item) {
        ItemProperties.register(item, new ResourceLocation("blood"), (stack, world, entity, seed) -> {
            if (stack.getItem() instanceof BloodSwordItem bloodSwordItem) {
                // Get the custom data value and map it to a float between 0.0F and 9.0F (10 options)
                int customData = bloodSwordItem.getCustomData(stack);
                int option = Math.min(customData / 2, 9);
                return (float) option;
            }
            return 0.0F;
        });
    }

}
