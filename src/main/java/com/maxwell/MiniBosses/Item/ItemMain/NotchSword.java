package com.maxwell.MiniBosses.Item.ItemMain;

import com.maxwell.MiniBosses.Item.ItemRenderer.NotchSwordRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public final class NotchSword extends SwordItem implements GeoItem {
    private int blockCounter = 0; // 現在のブロック回数
    public NotchSword(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }
    private static final RawAnimation SWINGANIM = RawAnimation.begin().thenPlay("swniging");
    private static final RawAnimation SHEILD = RawAnimation.begin().thenPlay("sheilding");
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "NotchSwordAttacktriger", 0, this::unifiedAttackController)
                .triggerableAnim("swniging", SWINGANIM)
                .triggerableAnim("sheilding", SHEILD));
    }
    public int attackType;
    private <E extends NotchSword> PlayState unifiedAttackController(final AnimationState<E> event) {
        return switch (attackType) {
            case 1 -> event.setAndContinue(SWINGANIM);
            case 2 -> event.setAndContinue(SHEILD);
            default -> PlayState.STOP;
        };
    }
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private NotchSwordRenderer renderer;

            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null) {
                    this.renderer = new NotchSwordRenderer();
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
        if (level instanceof ServerLevel serverLevel) {
            this.triggerAnim(player, GeoItem.getOrAssignId(player.getItemInHand(hand), serverLevel), "NotchSwordAttacktriger", "sheilding");
        }
        player.getCooldowns().addCooldown(this, 600);
        ItemStack itemStack = player.getItemInHand(hand);
        return InteractionResultHolder.success(itemStack);
    }
    @Mod.EventBusSubscriber(modid = "miniboss", bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class DamageBlocker {
        private static final int MAX_BLOCK_COUNT = 3;
        private static final Map<UUID, Integer> blockCounterMap = new HashMap<>();
        @SubscribeEvent
        public static void onPlayerHurt(LivingHurtEvent event) {
            if (event.getEntity() instanceof Player player) {
                ItemStack stack = player.getMainHandItem();
                if (stack.getItem() instanceof NotchSword) {
                    UUID playerId = player.getUUID();
                    int blockCounter = blockCounterMap.getOrDefault(playerId, MAX_BLOCK_COUNT);
                    if (blockCounter > 0) {
                        blockCounterMap.put(playerId, blockCounter - 1);
                        event.setCanceled(true);
                    }
                    if (blockCounter <= 0) {
                        blockCounterMap.remove(playerId);
                    }
                }
            }
        }
    }
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.miniboss.notchsword.desc").withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.translatable("item.miniboss.notchsword.desc2").withStyle(ChatFormatting.YELLOW));
    }
    @Override
    public boolean isDamageable(ItemStack stack) {
        return false; // 耐久値が減らない
    }

}
