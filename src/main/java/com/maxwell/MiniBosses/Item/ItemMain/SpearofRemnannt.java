package com.maxwell.MiniBosses.Item.ItemMain;

import com.maxwell.MiniBosses.DamagetypeRegister;
import com.maxwell.MiniBosses.Item.ItemRenderer.HerobrineSwordRenderer;
import com.maxwell.MiniBosses.Item.ItemRenderer.SpearofremnantRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public final class SpearofRemnannt extends SwordItem implements GeoItem {
    private int blockCounter = 0; // 現在のブロック回数
    public SpearofRemnannt(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private SpearofremnantRenderer renderer;

            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null) {
                    this.renderer = new SpearofremnantRenderer();
                }

                return this.renderer;
            }
        });
    }
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        blockCounter = 0;
        player.startUsingItem(hand);
        if (!level.isClientSide) {
            LivingEntity target = findNearestEnemy(level, player);
            if (target != null) {
                Vec3 direction = target.position().subtract(player.position()).normalize().scale(2.5);
                player.setDeltaMovement(direction);
                player.hurtMarked = true;
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST,2000,2));
            }
        }
        player.getCooldowns().addCooldown(this, 320);
        ItemStack itemStack = player.getItemInHand(hand);
        return InteractionResultHolder.success(itemStack);
    }

    private LivingEntity findNearestEnemy(Level level, Player player) {
        double nearestDistance = Double.MAX_VALUE;
        LivingEntity nearestEntity = null;

        for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(10))) {
            if (entity != player && entity.isAlive() && entity instanceof Mob) {
                double distance = player.distanceTo(entity);
                if (distance < nearestDistance) {
                    nearestDistance = distance;
                    nearestEntity = entity;
                }
                if (distance <= 4)
                {
                    entity.hurt(DamagetypeRegister.causeSwordDamage(player),(float) 7);
                }
            }
        }
        return nearestEntity;
    }
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.miniboss.spearofremnant.desc").withStyle(ChatFormatting.BLUE));
        tooltip.add(Component.translatable("item.miniboss.spearofremnant.desc2").withStyle(ChatFormatting.BLUE));
    }
    @Override
    public boolean isDamageable(ItemStack stack) {
        return false; // 耐久値が減らない
    }

}
