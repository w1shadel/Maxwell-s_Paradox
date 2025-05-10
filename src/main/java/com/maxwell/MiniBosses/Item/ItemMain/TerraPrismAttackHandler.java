package com.maxwell.MiniBosses.Item.ItemMain;

import com.maxwell.MiniBosses.DamagetypeRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TerraPrismAttackHandler {
    private static final int ATTACK_INTERVAL = 1; // ✅ 攻撃間隔 (0.5秒 = 10 ticks)
    private static final float ATTACK_RADIUS = 3.0F; // ✅ 攻撃範囲 (半径5ブロック)

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Level level = player.level();

        // ✅ クライアント側なら処理しない
        if (level.isClientSide) {
            return;
        }
        // ✅ 特定の武器を持っているかチェック
        ItemStack weapon = player.getMainHandItem();
        if (!(weapon.getItem() instanceof terraprism)) {
            return; // ✅ 特定の武器でない場合は処理しない
        }
        if (player.swinging) {
            if (level.getGameTime() % ATTACK_INTERVAL == 0) { // ✅ 一定間隔で攻撃判定を作成
                createAreaAttack(player, level);
            }
        }
    }

    private static void createAreaAttack(Player player, Level level) {
        AABB attackBox = player.getBoundingBox().inflate(ATTACK_RADIUS); // ✅ プレイヤーのヒットボックス + 周囲5マス

        List<Entity> entities = level.getEntities(player, attackBox);

        for (Entity entity : entities) {
            if (entity instanceof LivingEntity && entity != player) {
                LivingEntity target = (LivingEntity) entity;
                target.invulnerableTime = 0;
                target.hurt(DamagetypeRegister.causeNotchDamage(player), 25);
            }
        }
    }
}

