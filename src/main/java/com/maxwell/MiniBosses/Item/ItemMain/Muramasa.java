package com.maxwell.MiniBosses.Item.ItemMain;

import com.maxwell.MiniBosses.DamagetypeRegister;
import com.maxwell.MiniBosses.Item.ItemRenderer.MuramasaRenderer;
import com.maxwell.MiniBosses.Item.ItemRenderer.TerraBladeRenderer;
import com.maxwell.MiniBosses.entity.custom.herobrine;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
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
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Muramasa extends SwordItem implements GeoItem {
    public Muramasa(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private MuramasaRenderer renderer;

            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null) {
                    this.renderer = new MuramasaRenderer();
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
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "attack", 0, this::SPIN)
                .triggerableAnim("attack", SPINING));
    }
    private static final RawAnimation SPINING = RawAnimation.begin().thenPlay("attack");
    public int attackType;
    private <E extends Muramasa> PlayState SPIN(final AnimationState<E> event) {
        return switch (attackType) {
            case 1 -> event.setAndContinue(SPINING);
            default -> PlayState.STOP;
        };
    }
    private static final int ATTACK_RANGE = 4; // ✅ 攻撃範囲
    private static final int ATTACK_DELAY = 20; // ✅ 1秒 (20 ticks)
    private static final int COOLDOWN = 8; // ✅ クールダウン (1秒)
    private List<LivingEntity> pendingTargets = new ArrayList<>();
    private int attackTimer = 0;
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.fail(itemStack);
        }
        List<LivingEntity> targets = getTargetsInSight(player, level);
        if (!targets.isEmpty()) {
            attackOnce(player, targets);
            pendingTargets = targets;
            attackTimer = ATTACK_DELAY;
            player.getCooldowns().addCooldown(this, COOLDOWN); // ✅ クールダウンを設定
        }
        if (level instanceof ServerLevel serverLevel) {
            this.triggerAnim(player, GeoItem.getOrAssignId(player.getItemInHand(hand), serverLevel), "attack", "attack");
        }
        return InteractionResultHolder.success(itemStack);
    }
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (attackTimer > 0) {
            attackTimer--;
            if (attackTimer == 0 && entity instanceof Player player) {
                attackOnce(player, pendingTargets);
                pendingTargets.clear(); // ✅ 攻撃後にリストをクリア
            }
        }
    }

    private List<LivingEntity> getTargetsInSight(Player player, Level level) {
        Vec3 startPos = player.getEyePosition();
        Vec3 lookVec = player.getLookAngle().normalize();
        Vec3 endPos = startPos.add(lookVec.scale(ATTACK_RANGE));

        AABB attackBox = new AABB(startPos, endPos).inflate(1.0); // ✅ 4ブロック以内の範囲
        return level.getEntitiesOfClass(LivingEntity.class, attackBox, e -> e != player);
    }

    private void attackOnce(Player player, List<LivingEntity> targets) {
        for (LivingEntity target : targets) {
            target.hurt(DamagetypeRegister.causeNotchDamage(player), 30);
        }
    }
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.miniboss.muramasa.desc").withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.translatable("item.miniboss.muramasa.desc2").withStyle(ChatFormatting.YELLOW));
    }
    @Override
    public boolean isDamageable(ItemStack stack) {
        return false; // 耐久値が減らない
    }
}
