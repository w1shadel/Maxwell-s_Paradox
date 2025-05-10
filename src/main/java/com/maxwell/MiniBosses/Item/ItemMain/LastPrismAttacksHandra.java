package com.maxwell.MiniBosses.Item.ItemMain;

import com.maxwell.MiniBosses.DamagetypeRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LastPrismAttacksHandra {
    private static final int CHARGE_TIME = 40; // ✅ 40 tick (2秒)
    private static final Map<UUID, Integer> chargeMap = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Level level = player.level();
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        ItemStack weapon = player.getMainHandItem();
        if (!(weapon.getItem() instanceof Lastprism)) {
            return;
        }
        if (player.swinging) {
                shootLaser(player, serverLevel);
        }
    }


    private static void shootLaser(Player player, ServerLevel serverLevel) {
        Vec3 direction = player.getLookAngle().normalize();
        Vec3 beamStart = player.position().add(direction.scale(1.5)); // 🔥 発射位置を前方へ

        int beamLength = 60; // ビームの長さ
        int beamWidth = 1;  // 🔥 横幅 (ビームの広がり)
        int beamHeight = 1; // 🔥 縦の高さ (縦に拡張)


        for (int i = 1; i <= beamLength; i++) {
            Vec3 currentPos = beamStart.add(direction.scale(i));

            // 🔥 ビームの広がり（横と縦に拡張）
            AABB laserArea = new AABB(
                    currentPos.x - beamWidth / 2.0, currentPos.y - beamHeight / 2.0, currentPos.z - beamWidth / 2.0,
                    currentPos.x + beamWidth / 2.0, currentPos.y + beamHeight / 2.0, currentPos.z + beamWidth / 2.0
            );

            // 🔥 パーティクルの色を変更 (赤 → 緑 → 青)
            float red = Math.abs((float) Math.sin(i * 0.05));  // ✅ 赤の変化
            float green = Math.abs((float) Math.sin(i * 0.05 + Math.PI / 3));  // ✅ 緑の変化
            float blue = Math.abs((float) Math.sin(i * 0.05 + 2 * Math.PI / 3));  // ✅ 青の変化

            // ✅ パーティクルが表示されているか確認
            serverLevel.sendParticles(
                    new DustParticleOptions(new Vector3f(red, green, blue), 1.0F), // ✅ カスタムカラー
                    currentPos.x, currentPos.y, currentPos.z,
                    10, beamWidth * 0.2, beamHeight * 0.2, beamWidth * 0.2, 0.1
            );


            // 🔥 ターゲットにダメージ（範囲内の敵すべてに適用）
            List<LivingEntity> targets = serverLevel.getEntitiesOfClass(LivingEntity.class, laserArea);
            if (!targets.isEmpty()) {
                for (LivingEntity livingTarget : targets) {
                    livingTarget.hurt(DamagetypeRegister.causeNotchDamage(player), 32);
                    livingTarget.setInvulnerable(false); // ✅ 無敵時間を無効化
                }
            } else {
            }

            // 🔥 範囲内のブロック破壊（横幅と縦を考慮）
            if (!serverLevel.isClientSide) {
                BlockPos minPos = new BlockPos((int) laserArea.minX, (int) laserArea.minY, (int) laserArea.minZ);
                BlockPos maxPos = new BlockPos((int) laserArea.maxX, (int) laserArea.maxY, (int) laserArea.maxZ);

                for (BlockPos pos : BlockPos.betweenClosed(minPos, maxPos)) {
                    BlockState currentState = serverLevel.getBlockState(pos);
                    if (!currentState.isAir()) {
                        serverLevel.removeBlock(pos, false);
                    }
                }
            }
        }
    }
}
