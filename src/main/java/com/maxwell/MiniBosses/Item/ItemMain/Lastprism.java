package com.maxwell.MiniBosses.Item.ItemMain;

import com.maxwell.MiniBosses.Item.ItemRenderer.LastprismRenderer;
import com.maxwell.MiniBosses.Item.ItemRenderer.terraprismRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
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

public class Lastprism extends Item implements GeoItem {


    public Lastprism(Properties pProperties) {
        super(pProperties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }


    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private LastprismRenderer renderer;

            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null) {
                    this.renderer = new LastprismRenderer();
                }
                return this.renderer;
            }
        });
    }
    @Override
    public InteractionResultHolder<ItemStack> use( Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        if (level instanceof ServerLevel serverLevel) {
            this.triggerAnim(player, GeoItem.getOrAssignId(player.getItemInHand(hand), serverLevel), "spining", "spining");
        }
        return InteractionResultHolder.success(itemStack);
    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "spining", 0, this::SPIN)
                .triggerableAnim("spining", SPINING));
    }
    private static final RawAnimation SPINING = RawAnimation.begin().thenPlay("spining");
    public int attackType;
    private <E extends Lastprism> PlayState SPIN(final AnimationState<E> event) {
        return switch (attackType) {
            case 1 -> event.setAndContinue(SPINING);
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
        tooltip.add(Component.translatable("item.miniboss.lastprism.desc").withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.translatable("item.miniboss.lastprism.desc2").withStyle(ChatFormatting.YELLOW));
    }
    @Override
    public boolean isDamageable(ItemStack stack) {
        return false; // 耐久値が減らない
    }
}
