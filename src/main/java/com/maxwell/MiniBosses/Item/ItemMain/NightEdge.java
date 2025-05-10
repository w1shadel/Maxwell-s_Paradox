package com.maxwell.MiniBosses.Item.ItemMain;

import com.maxwell.MiniBosses.Item.ItemRenderer.NightEdgeRenderer;
import com.maxwell.MiniBosses.Item.ItemRenderer.SpearofremnantRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
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

public final class NightEdge extends SwordItem implements GeoItem {
    public NightEdge(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private NightEdgeRenderer renderer;

            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null) {
                    this.renderer = new NightEdgeRenderer();
                }

                return this.renderer;
            }
        });
    }

    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        player.startUsingItem(hand);
        if (level instanceof ServerLevel serverLevel) {
            this.triggerAnim(player, GeoItem.getOrAssignId(player.getItemInHand(hand), serverLevel), "Spin", "Spin");
        }
        ItemStack itemStack = player.getItemInHand(hand);
        return InteractionResultHolder.success(itemStack);
    }

    private static final RawAnimation SPIN = RawAnimation.begin().thenPlay("Spin");

    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Spin", 0, this::unifiedAttackController)
                .triggerableAnim("Spin", SPIN));
    }

    public int attackType;

    private <E extends NightEdge> PlayState unifiedAttackController(final AnimationState<E> event) {
        return switch (attackType) {
            case 1 -> event.setAndContinue(SPIN);
            default -> PlayState.STOP;
        };
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.miniboss.nightedge.desc").withStyle(ChatFormatting.YELLOW));
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false; // 耐久値が減らない
    }
}



