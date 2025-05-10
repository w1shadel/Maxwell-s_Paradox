package com.maxwell.MiniBosses.Item.ItemMain;

import com.maxwell.MiniBosses.DamagetypeRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
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

import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TerraBladeAttackHandler {
    private static final int ATTACK_INTERVAL = 2; // ✅ 攻撃間隔 (0.5秒 = 10 ticks)
    private static final float ATTACK_RADIUS = 5.0F; // ✅ 攻撃範囲 (半径5ブロック)

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
        if (!(weapon.getItem() instanceof TerraBlade)) {
            return; // ✅ 特定の武器でない場合は処理しない
        }
        if (player.swinging) {
            if (level.getGameTime() % ATTACK_INTERVAL == 0) { // ✅ 一定間隔で攻撃判定を作成
                createAreaAttack(player, level);
                shootLaser(player, (ServerLevel) level);
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

                target.hurt(DamagetypeRegister.causeNotchDamage(player), 88);
            }
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

            // 🔥 パーティクルエフェクト（ビーム全体に適用）
            serverLevel.sendParticles(
                    new DustParticleOptions(new Vector3f(red, green, blue), 1.0F), // ✅ カスタムカラー
                    currentPos.x, currentPos.y, currentPos.z,
                    10, beamWidth * 0.2, beamHeight * 0.2, beamWidth * 0.2, 0.1
            );

            // 🔥 ターゲットにダメージ（範囲内の敵すべてに適用）
            serverLevel.getEntitiesOfClass(LivingEntity.class, laserArea).forEach(livingTarget -> {
                livingTarget.hurt(DamagetypeRegister.causeNotchDamage(player), 32);
                livingTarget.setInvulnerable(false); // ✅ 無敵時間を無効化
            });
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
