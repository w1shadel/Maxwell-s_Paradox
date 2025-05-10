package com.maxwell.MiniBosses.register;


import com.maxwell.MiniBosses.entity.custom.gulitchguy;
import com.maxwell.MiniBosses.entity.custom.nullguy;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class NullEntitySummoner {
    private static final int MIN_TIME = 13000; // 10秒 (ticks換算: 20 ticks = 1秒)
    private static final int MAX_TIME = 19000; // 300秒
    private static final int SPAWN_RADIUS = 80; // 20ブロック範囲

    private static final Map<UUID, Integer> timerMap = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Level level = player.level();

        if (!level.isClientSide) {
            UUID playerId = player.getUUID();
            timerMap.putIfAbsent(playerId, MIN_TIME + player.getRandom().nextInt(MAX_TIME - MIN_TIME));

            int timer = timerMap.get(playerId);
            timer--;
            if (timer <= 0) {
                spawnBossNearPlayer(player);
                timerMap.put(playerId, MIN_TIME + player.getRandom().nextInt(MAX_TIME - MIN_TIME)); // 次回のスポーン時間をランダム化
            } else {
                timerMap.put(playerId, timer);
            }
        }
    }

    private static void spawnBossNearPlayer(Player player) {
        Level level = player.level();
        BlockPos spawnPos = player.blockPosition().offset(
                player.getRandom().nextInt(SPAWN_RADIUS * 2) - SPAWN_RADIUS,
                0,
                player.getRandom().nextInt(SPAWN_RADIUS * 2) - SPAWN_RADIUS
        );

        if (level.getBlockState(spawnPos).isAir()) {
            nullguy boss = new nullguy(ModEntities.NULL.get(), level);
            boss.setPos(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
            level.addFreshEntity(boss);
            boss.removeWhenFarAway(200);
            Component message = Component.literal("§eNullがゲームに参加しました。");
            player.sendSystemMessage(message);
        }
    }
}
