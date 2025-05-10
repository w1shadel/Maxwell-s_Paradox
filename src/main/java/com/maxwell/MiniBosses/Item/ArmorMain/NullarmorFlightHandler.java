package com.maxwell.MiniBosses.Item.ArmorMain;

import com.maxwell.MiniBosses.DamagetypeRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class NullarmorFlightHandler {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;

        if (hasFullArmor(player)) {
            player.getAbilities().mayfly = true;
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 2000, 2, true, true));
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 2000, 1, true, true));
        } else {
            if (!player.isCreative()) {

                player.getAbilities().flying = false;
                player.getAbilities().mayfly = false;
            }
            if (player.isSpectator()) {
                player.getAbilities().flying = true;
                player.getAbilities().mayfly = true;
            }
        }
        if (hasFullArmor(player) && player.swinging && player.getMainHandItem().isEmpty()) {
            shootLaser(player);
        }
    }

    private static boolean hasFullArmor(Player player) {
        return player.getInventory().getArmor(0).getItem() instanceof Nullarmor &&
                player.getInventory().getArmor(1).getItem() instanceof Nullarmor &&
                player.getInventory().getArmor(2).getItem() instanceof Nullarmor &&
                player.getInventory().getArmor(3).getItem() instanceof Nullarmor;
    }



    private static void shootLaser(Player player) {
        Level level = player.level();
        if (!(level instanceof ServerLevel serverLevel)) return;

        Vec3 direction = player.getLookAngle().normalize();
        Vec3 beamStart = player.position().add(direction.scale(1.5)); // 🔥 発射位置を前方へ

        int beamLength = 60; // ビームの長さ
        int beamWidth = 3;  // 🔥 横幅 (ビームの広がり)
        int beamHeight = 3; // 🔥 縦の高さ (縦に拡張)

        for (int i = 1; i <= beamLength; i++) {
            Vec3 currentPos = beamStart.add(direction.scale(i));

            // 🔥 ビームの広がり（横と縦に拡張）
            AABB laserArea = new AABB(
                    currentPos.x - beamWidth / 2.0, currentPos.y - beamHeight / 2.0, currentPos.z - beamWidth / 2.0,
                    currentPos.x + beamWidth / 2.0, currentPos.y + beamHeight / 2.0, currentPos.z + beamWidth / 2.0
            );

            // 🔥 パーティクルエフェクト（ビーム全体に適用）
            serverLevel.sendParticles(
                    ParticleTypes.SMALL_FLAME,
                    currentPos.x, currentPos.y, currentPos.z,
                    10, beamWidth * 0.2, beamHeight * 0.2, beamWidth * 0.2, 0.1
            );

            // 🔥 ターゲットにダメージ（範囲内の敵すべてに適用）
            serverLevel.getEntities(null, laserArea).forEach(e -> {
                if (e instanceof LivingEntity livingTarget) {
                    livingTarget.hurt(DamagetypeRegister.causePunchDamage(player), 12.0F);
                }
            });

            // 🔥 範囲内のブロック破壊（横幅と縦を考慮）
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

