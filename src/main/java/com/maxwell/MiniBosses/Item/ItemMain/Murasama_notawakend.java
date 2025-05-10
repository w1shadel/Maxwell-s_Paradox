package com.maxwell.MiniBosses.Item.ItemMain;

import com.maxwell.MiniBosses.DamagetypeRegister;
import com.maxwell.MiniBosses.Item.ItemRenderer.Murasama_notawakendRenderer;
import com.maxwell.MiniBosses.Item.ItemRenderer.NightEdgeRenderer;
import com.maxwell.MiniBosses.register.ItemRegister_Maxwell;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class Murasama_notawakend extends SwordItem implements GeoItem {
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class BossKillTracker {
        private static final String BOSS_KILL_TAG = "boss_kill_count";

        @SubscribeEvent
        public static void onBossDefeated(LivingDeathEvent event) {
            if (!(event.getEntity() instanceof Zombie)) return; // ✅ ボスかどうかチェック

            Level level = event.getEntity().level();
            if (level.isClientSide) return; // ✅ クライアント側なら処理しない
            DamageSource source = event.getSource();
            if (!(source.getEntity() instanceof Player player)) return;

            // ✅ NBTデータを取得
            CompoundTag playerData = player.getPersistentData();

            // ✅ ボス討伐数を増加
            int bossKillCount = playerData.getInt(BOSS_KILL_TAG) + 1;
            playerData.putInt(BOSS_KILL_TAG, bossKillCount);
        }

        public static int getBossKillCount(Player player) {
            return player.getPersistentData().getInt(BOSS_KILL_TAG);
        }
    }
    private static final int AWAKEN_THRESHOLD = 100; // ✅ 10体倒したら覚醒
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof Player player) {
            int bossKillCount = BossKillTracker.getBossKillCount(player);
            System.out.println(bossKillCount);
            if (bossKillCount >= AWAKEN_THRESHOLD) {
                awakenWeapon(player);
            }
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    private void awakenWeapon(Player player) {
        ItemStack awakenedWeapon = new ItemStack(ItemRegister_Maxwell.MURASAMA.get()); // ✅ `Awakened Murasama` に切り替え
        player.getInventory().setItem(player.getInventory().selected, awakenedWeapon); // ✅ 現在の武器を置き換え
    }
    public Murasama_notawakend(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private Murasama_notawakendRenderer renderer;

            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null) {
                    this.renderer = new Murasama_notawakendRenderer();
                }

                return this.renderer;
            }
        });
    }
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache () {
        return this.cache;
    }
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.miniboss.muramasa_no.desc").withStyle(ChatFormatting.YELLOW));
    }
    @Override
    public boolean isDamageable(ItemStack stack) {
        return false; // 耐久値が減らない
    }
}
