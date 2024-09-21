package net.theholyraj.rajswordmod.world.item.custom;

import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.extensions.IForgeBakedModel;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.theholyraj.rajswordmod.SwordMod;
import net.theholyraj.rajswordmod.client.sound.ModSounds;
import net.theholyraj.rajswordmod.network.ModMessages;
import net.theholyraj.rajswordmod.network.packet.DeflectParticleS2CPacket;
import net.theholyraj.rajswordmod.world.config.ModCommonConfigs;
import net.theholyraj.rajswordmod.world.entity.custom.DashProjectileEntity;
import net.theholyraj.rajswordmod.world.item.ModItems;
import net.theholyraj.rajswordmod.world.item.util.ModCapabilities;
import net.theholyraj.rajswordmod.world.item.util.bloodsword.IBloodSwordData;
import net.theholyraj.rajswordmod.world.item.util.upgradesword.IUpgradeSwordData;
import net.theholyraj.rajswordmod.world.item.util.upgradesword.UpgradeCapabilityProvider;
import net.theholyraj.rajswordmod.world.item.util.upgradesword.UpgradeSwordTimer;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = SwordMod.MODID)
public class DeflectSwordItem extends SwordItem {
    public DeflectSwordItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }
    public static final String SWORD_UUID_TAG = "SwordUUID";

    public static String getSwordUUID(ItemStack stack) {
        CompoundTag nbt = stack.getOrCreateTag();
        if (!nbt.contains(SWORD_UUID_TAG)) {
            String uniqueID = UUID.randomUUID().toString();
            nbt.putString(SWORD_UUID_TAG, uniqueID);
            stack.setTag(nbt);
        }
        return nbt.getString(SWORD_UUID_TAG);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pUsedHand == InteractionHand.MAIN_HAND){
            pPlayer.startUsingItem(pUsedHand);
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        if (pLivingEntity instanceof Player player){
            if (isUpgraded(pStack)){
                upgradedDeleteNearbyProjectiles(player, pStack);
            }
            else {
                deleteNearbyProjectiles(player, pStack);
            }
        }
        if (pRemainingUseDuration == 20){
            pLevel.playSound((Player)pLivingEntity,pLivingEntity,SoundEvents.SHULKER_BOX_CLOSE, SoundSource.PLAYERS, 1f,2f);
        }
        if (pRemainingUseDuration == 17){
            pLevel.playSound((Player)pLivingEntity,pLivingEntity,SoundEvents.SHULKER_BOX_CLOSE, SoundSource.PLAYERS, 1f,2f);
        }
        super.onUseTick(pLevel, pLivingEntity, pStack, pRemainingUseDuration);
    }

    @Override
    public void onStopUsing(ItemStack stack, LivingEntity entity, int count) {
        if (entity instanceof Player player){
            if (DeflectSwordItem.isUpgraded(stack)){
                player.getCooldowns().addCooldown(this, ModCommonConfigs.ARROW_RENDER_UPGRADE_COOLDOWN.get());
            }
            else {
                player.getCooldowns().addCooldown(this, ModCommonConfigs.ARROW_RENDER_COOLDOWN.get());
            }
            if (!entity.level().isClientSide()){
                stack.setDamageValue(ModCommonConfigs.ARROW_RENDER_DURABILITY_USE.get());
            }
            if (!player.isShiftKeyDown()){
                player.level().playSound(player,player.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP,SoundSource.PLAYERS,1,1);
            }
        }
        if (entity.level().isClientSide() && entity instanceof Player player){
            if (!player.isShiftKeyDown()){
                Vec3 playerLook = player.getViewVector(1);
                Vec3 dashVec = new Vec3(playerLook.x(), playerLook.y(), playerLook.z());
                player.setDeltaMovement(dashVec);
                player.level().playSound(player,player.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP,SoundSource.PLAYERS,1,1);
            }
        }
        super.onStopUsing(stack, entity, count);
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (!pLevel.isClientSide && pEntity instanceof ServerPlayer serverPlayer)
        {pStack.getCapability(ModCapabilities.UPGRADE_DATA_CAPABILITY).ifPresent(cap -> {
            // Force synchronization to prevent creative mode issues
            if (cap.isUpgraded()) {
                UpgradeSwordTimer.tick(pStack, serverPlayer);
            }
        });}
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return !DeflectSwordItem.isUpgraded(stack);
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new UpgradeCapabilityProvider();
    }

    @Override
    public CompoundTag getShareTag(ItemStack stack) {
        CompoundTag nbt = super.getShareTag(stack);
        System.out.println("SADdsa");
        if (nbt == null) {
            nbt = new CompoundTag();
        }
        final CompoundTag finalNbt = nbt;
        stack.getCapability(ModCapabilities.UPGRADE_DATA_CAPABILITY).ifPresent(cap -> {
            CompoundTag capTag = new CompoundTag();
            cap.writeToNBT(capTag);
            finalNbt.put("upgrade_data", capTag);
            finalNbt.putBoolean("isUpgraded", cap.isUpgraded());
            if (isUpgraded(stack)){
                finalNbt.putInt("timer", UpgradeSwordTimer.getUpgradeTimer(stack));
            }
        });
        return nbt;
    }

    @Override
    public void readShareTag(ItemStack stack, @javax.annotation.Nullable CompoundTag nbt) {
        super.readShareTag(stack, nbt);
        System.out.println("SADdsa");
        if (nbt != null && nbt.contains("upgrade_data")) {
            stack.getCapability(ModCapabilities.UPGRADE_DATA_CAPABILITY).ifPresent(cap -> {
                cap.readFromNBT(nbt.getCompound("upgrade_data"));

                // Load the isUpgraded boolean
                if (nbt.contains("isUpgraded")) {
                    cap.setUpgraded(nbt.getBoolean("isUpgraded"));
                }
                if (isUpgraded(stack) && nbt.contains("timer")){
                    UpgradeSwordTimer.setUpgradeTimer(stack, nbt.getInt("timer"));
                }
            });
        }
    }

    public static void setUpgradeData(ItemStack stack, int data) {
        stack.getCapability(ModCapabilities.UPGRADE_DATA_CAPABILITY).ifPresent(customData -> {
            customData.setData(data);
        });
    }

    public static int getUpgradeData(ItemStack stack) {
        return stack.getCapability(ModCapabilities.UPGRADE_DATA_CAPABILITY).map(IUpgradeSwordData::getData).orElse(0);
    }

    public static boolean isUpgraded(ItemStack stack){
        return stack.getCapability(ModCapabilities.UPGRADE_DATA_CAPABILITY).map(IUpgradeSwordData::isUpgraded).orElse(false);
    }
    public static void setUpgraded(ItemStack stack, boolean data) {
        stack.getCapability(ModCapabilities.UPGRADE_DATA_CAPABILITY).ifPresent(customData -> {
            if (customData.isUpgraded() != data) {
                UpgradeSwordTimer.setUpgradeTimer(stack, ModCommonConfigs.ARROW_RENDER_UPGRADE_TIME.get());
                customData.setUpgraded(data);
            }
        });
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return ModCommonConfigs.ARROW_RENDER_ABILITY_USE_TIME.get();
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BLOCK;
    }

    private void upgradedDeleteNearbyProjectiles(Player player, ItemStack stack) {
        Vec3 center = new Vec3(player.blockPosition().getX(), player.blockPosition().getY(), player.blockPosition().getZ());
        List<Projectile> projectiles = player.level().getEntitiesOfClass(Projectile.class, new AABB(center, center).inflate(6), e -> true)
                .stream()
                .sorted(Comparator.comparingDouble(ent -> ent.distanceToSqr(center)))
                .toList();

        for (Projectile projectile : projectiles) {
            if (projectile.distanceToSqr(player) < 7) {
                if (!player.level().isClientSide()) {
                    // Ensure projectile is not discarded before playing sound
                    Vec3 velocity = projectile.getDeltaMovement();
                    double velocityThreshold = 0.01; // Smaller threshold for nearly stationary projectiles

                    // Check if the projectile is nearly stationary
                    if (Math.abs(velocity.x) < velocityThreshold && Math.abs(velocity.y) < velocityThreshold && Math.abs(velocity.z) < velocityThreshold) {
                        // Check additional conditions if needed, like position or state
                        if (projectile.onGround() || projectile.blockPosition().equals(projectile.position())) {
                            player.level().playSound(null, projectile.blockPosition(), ModSounds.PROJECTILE_SLASH.get(), SoundSource.PLAYERS, 0.1f, 1f);
                            ModMessages.sendToClients(new DeflectParticleS2CPacket(projectile.position().x(), projectile.position().y(), projectile.position().z()));
                            projectile.discard(); // Discard the projectile if nearly stationary
                            continue; // Skip to the next projectile
                        }
                    }

                    // Additional check to discard projectile based on velocity
                    if (projectile.getDeltaMovement().lengthSqr() < 0.9) {
                        player.level().playSound(null, projectile.blockPosition(), ModSounds.PROJECTILE_SLASH.get(), SoundSource.PLAYERS, 0.1f, 1f);
                        ModMessages.sendToClients(new DeflectParticleS2CPacket(projectile.position().x(), projectile.position().y(), projectile.position().z()));
                        projectile.discard();
                        continue; // Skip to the next projectile
                    }

                    // Handle projectile deflection
                    Entity shooter = projectile.getOwner();
                    if (shooter != null) {
                        Vec3 shooterPosition = new Vec3(shooter.position().x,
                                shooter.position().y + (shooter.getBbHeight() / 2),
                                shooter.position().z);
                        Vec3 directionToShooter = shooterPosition.subtract(projectile.position()).normalize();
                        projectile.shoot(directionToShooter.x, directionToShooter.y, directionToShooter.z, (float) projectile.getDeltaMovement().length(), 0);
                        projectile.setOwner(player);
                        projectile.hasImpulse = true;
                        player.level().playSound(null, projectile.blockPosition(), ModSounds.PROJECTILE_SLASH.get(), SoundSource.PLAYERS, 0.1f, 1f);
                        ModMessages.sendToClients(new DeflectParticleS2CPacket(projectile.position().x(), projectile.position().y(), projectile.position().z()));
                    } else {
                        Vec3 directionToShooter = new Vec3(-projectile.getDeltaMovement().x,
                                -projectile.getDeltaMovement().y,
                                -projectile.getDeltaMovement().z);
                        projectile.shoot(directionToShooter.x, directionToShooter.y, directionToShooter.z, (float) projectile.getDeltaMovement().length(), 0);
                        projectile.setOwner(player);
                        projectile.hasImpulse = true;
                        player.level().playSound(null, projectile.blockPosition(), ModSounds.PROJECTILE_SLASH.get(), SoundSource.PLAYERS, 0.1f, 1f);
                        ModMessages.sendToClients(new DeflectParticleS2CPacket(projectile.position().x(), projectile.position().y(), projectile.position().z()));
                    }
                }
            }
        }
    }

    private void deleteNearbyProjectiles(Player player, ItemStack stack){
        Vec3 center = new Vec3(player.blockPosition().getX(),player.blockPosition().getY(),player.blockPosition().getZ());
        List<Projectile> projectiles = player.level().getEntitiesOfClass(Projectile.class, new AABB(center,center).inflate(6), e-> true).stream()
                .sorted(Comparator.comparingDouble(ent -> ent.distanceToSqr(center))).toList();

        for (Projectile projectile: projectiles){
            if (projectile.distanceToSqr(player) < 7){
                player.level().playSound(null, projectile.blockPosition(), ModSounds.PROJECTILE_SLASH.get(), SoundSource.PLAYERS,0.1f,1f);
                if (!player.level().isClientSide()){
                    ModMessages.sendToClients(new DeflectParticleS2CPacket(projectile.position().x(),projectile.position().y(),projectile.position().z()));
                    projectile.remove(Entity.RemovalReason.DISCARDED);
                    if (getUpgradeData(stack) < ModCommonConfigs.ARROW_RENDER_UPGRADE_AMOUNT.get()){
                        int currentAmount = getUpgradeData(stack);
                        setUpgradeData(stack, currentAmount+1);
                    }
                }
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (DeflectSwordItem.isUpgraded(pStack)){
            pTooltipComponents.add(Component.translatable("rajswordmod.hovertext.deflect_sword_awakened"));
        }
        else {
            pTooltipComponents.add(Component.translatable("rajswordmod.hovertext.deflect_sword_unawakened"));
        }
        int deletedProjectiles = DeflectSwordItem.getUpgradeData(pStack);

        int requiredProjectiles = ModCommonConfigs.ARROW_RENDER_UPGRADE_AMOUNT.get();
        if (requiredProjectiles > deletedProjectiles){
            pTooltipComponents.add(Component.translatable("rajswordmod.tooltip.projectiles_count_no", deletedProjectiles, requiredProjectiles));
        }
        else {
            pTooltipComponents.add(Component.translatable("rajswordmod.tooltip.projectiles_count_yes"));
        }
    }

    @Override
    public boolean isBarVisible(ItemStack pStack) {
        if (DeflectSwordItem.isUpgraded(pStack)){
            return true;
        }
        return super.isBarVisible(pStack);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        if (DeflectSwordItem.isUpgraded(stack)){
            int remainingTime = UpgradeSwordTimer.getUpgradeTimer(stack);
            int maxTime = ModCommonConfigs.ARROW_RENDER_UPGRADE_TIME.get();
            return Math.round(13.0F * (float) remainingTime / (float) maxTime);
        }
        return super.getBarWidth(stack);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        if (DeflectSwordItem.isUpgraded(stack)){
            return 0xFF00FF;
        }
        return super.getBarColor(stack);
    }
    //////////////////////////////////////////EVENT//////////////////////////////////////////////////////
    @SubscribeEvent
    public static void projectileFailsafe(ProjectileImpactEvent event){
        if (event.getRayTraceResult() instanceof EntityHitResult result){
            if (result.getEntity() instanceof Player player){
                if (!player.level().isClientSide() && player.getMainHandItem().is(ModItems.DEFLECT_SWORD.get())){
                    if (player.getMainHandItem().hasTag() && player.isUsingItem()){
                        Projectile projectile = event.getProjectile();
                        ModMessages.sendToClients(new DeflectParticleS2CPacket(projectile.getX(),projectile.getY(),projectile.getZ()));
                        event.setImpactResult(ProjectileImpactEvent.ImpactResult.STOP_AT_CURRENT_NO_DAMAGE);
                        player.getMainHandItem().setDamageValue(1);
                    }
                }
            }
        }
    }
}
