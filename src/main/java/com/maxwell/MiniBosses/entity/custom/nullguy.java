package com.maxwell.MiniBosses.entity.custom;

import com.maxwell.MiniBosses.CustomBossBar;
import com.maxwell.MiniBosses.DamagetypeRegister;
import com.maxwell.MiniBosses.entity.block.DisappearingBlock;
import com.maxwell.MiniBosses.register.ItemRegister_Maxwell;
import com.maxwell.MiniBosses.register.ModBlocks_Miniboss;
import com.maxwell.MiniBosses.event.BossInfoServer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.joml.Random;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;
import java.util.Objects;

public class nullguy extends Monster implements GeoEntity {
    public nullguy(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    private final BossInfoServer bossEvent1 = new BossInfoServer(this.getDisplayName(), BossEvent.BossBarColor.PURPLE, true, 0);

    public void startSeenByPlayer(@NotNull ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent1.addPlayer(player);
        player.playSound(SoundEvents.MUSIC_DISC_PIGSTEP, 1000.0F, 1.0F);
    }

    public void stopSeenByPlayer(@NotNull ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent1.removePlayer(player);
        player.playSound(SoundEvents.MUSIC_DISC_PIGSTEP, 0.0F, 1.0F);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(0, new DataCorruptionGoal(this, 200));
        this.goalSelector.addGoal(1, new CrashWaveWithTeleportGoal(this, 5, 30));
        this.goalSelector.addGoal(1, new SystemErrorGoal(this, 13, 60));
        this.goalSelector.addGoal(1, new DataCollapseExplosionGoal(this, 13));
        this.goalSelector.addGoal(1, new WaveOfNullityGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    @Override
    public void die(@NotNull DamageSource source)
    {
        LivingEntity target = this.getTarget();
        Component message = Component.literal("§e??????がゲームから退出しました。");
        Objects.requireNonNull(target).sendSystemMessage(message);

    }
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0f)
                .add(Attributes.MOVEMENT_SPEED, 0.5F) // 移動速度
                .add(Attributes.ATTACK_DAMAGE, 10) // 攻撃力
                .add(Attributes.MAX_HEALTH, 1) // 最大HP
                .add(Attributes.KNOCKBACK_RESISTANCE, 3); // ノックバック耐性
    }

    public boolean IsInUnder400;

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (this.getHealth() <= 400 && !IsInUnder400) {
            this.bossEvent1.setProgress(this.getHealth() / this.getMaxHealth());
            float currentMaxHealth = (float) Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).getBaseValue();
            Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(currentMaxHealth + amount);
            this.setHealth(this.getMaxHealth());
            return super.hurt(source, -amount);
        } else {
            this.goalSelector.addGoal(1, new DEAD(this));
            IsInUnder400 = true;
        }
        return super.hurt(source, amount);
    }

    public static class DataCorruptionGoal extends Goal {
        private final LivingEntity entity; // Nullエンティティ
        private final int radius; // 操作する範囲
        private final Random random = new Random();

        public DataCorruptionGoal(LivingEntity entity, int radius) {
            this.entity = entity;
            this.radius = radius;
        }

        @Override
        public boolean canUse() {
            // ゴールを開始する条件
            return true; // 常に実行可能
        }

        @Override
        public void tick() {
            if (entity.getCommandSenderWorld() instanceof ServerLevel serverLevel && entity.getHealth() >= 400) {
                for (int i = 0; i < 5; i++) { // 一度に5回処理
                    BlockPos randomPos = entity.blockPosition().offset(
                            random.nextInt(radius * 2 + 1) - radius,
                            random.nextInt(3) - 1,
                            random.nextInt(radius * 2 + 1) - radius
                    );
                    Block block = serverLevel.getBlockState(randomPos).getBlock();
                    if (block != Blocks.AIR) {
                        serverLevel.setBlock(randomPos, Blocks.AIR.defaultBlockState(), 3);
                    }
                }
            }
        }

    }
    public static class DEAD extends Goal {
        private final nullguy mob;
        private final int moveCount = 5; // 5回移動
        private int currentMove = 0;
        private BlockPos targetPos;

        public DEAD(nullguy mob) {
            this.mob = mob;
        }

        @Override
        public boolean canUse() {
            return true; // 常に発動可能
        }

        @Override
        public void start() {
            this.currentMove = 0;
            this.targetPos = mob.blockPosition(); // 初期位置を保存
        }

        @Override
        public void tick() {
            if (currentMove < moveCount) {
                int dx = mob.getRandom().nextInt(5) + 1; // X方向に1~5ブロック移動
                int dz = mob.getRandom().nextInt(5) + 1; // Z方向に1~5ブロック移動

                targetPos = targetPos.offset(dx, 0, dz);
                mob.getNavigation().moveTo(targetPos.getX(), targetPos.getY(), targetPos.getZ(), 1.0);
                currentMove++;
                if (currentMove == 5)
                {
                    this.mob.spawnAtLocation(ItemRegister_Maxwell.NULLUPGREAD_INGOT.get());
                    this.mob.spawnAtLocation(ItemRegister_Maxwell.NULLUPGREAD_INGOT.get());
                    this.mob.spawnAtLocation(ItemRegister_Maxwell.NULLUPGREAD_INGOT.get());
                    this.mob.spawnAtLocation(ItemRegister_Maxwell.NULLUPGREAD_INGOT.get());
                    if (Math.random() < 0.15) { // ✅ 15%の確率で追加ドロップ
                        this.mob.spawnAtLocation(ItemRegister_Maxwell.NULLUPGREAD_INGOT.get());
                        this.mob.spawnAtLocation(ItemRegister_Maxwell.NULLUPGREAD_INGOT.get());
                        this.mob.spawnAtLocation(ItemRegister_Maxwell.NULLUPGREAD_INGOT.get());
                        this.mob.spawnAtLocation(ItemRegister_Maxwell.NULLUPGREAD_INGOT.get());
                    }
                    this.mob.discard();
                }
            } else {
                targetPos = findNearestAirBlock(targetPos, mob.level());
                mob.getNavigation().moveTo(targetPos.getX(), targetPos.getY(), targetPos.getZ(), 1.0);
            }
        }

        private BlockPos findNearestAirBlock(BlockPos startPos, Level level) {
            for (int y = startPos.getY(); y < level.getMaxBuildHeight(); y++) {
                BlockPos checkPos = new BlockPos(startPos.getX(), y, startPos.getZ());
                if (level.getBlockState(checkPos).isAir()) {
                    return checkPos;
                }
            }
            return startPos; // 空気ブロックがなければ元の位置に戻す
        }
    }
    public static class CrashWaveWithTeleportGoal extends Goal {
        private final LivingEntity entity; // Nullエンティティ
        private final int radius; // 波動の影響範囲
        private final int cooldownTicks; // クールダウン（tick単位）
        private int cooldownTimer = 0; // クールダウン用タイマー
        private final Random random = new Random();

        public CrashWaveWithTeleportGoal(LivingEntity entity, int radius, int cooldownTicks) {
            this.entity = entity;
            this.radius = radius;
            this.cooldownTicks = cooldownTicks;
        }

        @Override
        public boolean canUse() {
            return !entity.getCommandSenderWorld().getEntities(entity, entity.getBoundingBox().inflate(10), e -> e instanceof Player).isEmpty();
        }

        @Override
        public void start() {
            cooldownTimer = cooldownTicks;
            if (entity.getCommandSenderWorld() instanceof ServerLevel serverLevel) {
                Player targetPlayer = serverLevel.getNearestPlayer(entity, 10);
                if (targetPlayer == null) {
                    entity.getCommandSenderWorld().playSound(
                            null,
                            entity.blockPosition(),
                            SoundEvents.ENDERMAN_DEATH, // 例：別の効果音を再生
                            SoundSource.BLOCKS,
                            2.0f, // 音量
                            1.0f  // ピッチ
                    );
                    return; // ターゲットが見つからない場合はクラッシュを回避
                }

                BlockPos center = entity.blockPosition();

                // **ブロックの浮き上がり処理**
                for (int x = -radius; x <= radius; x++) {
                    for (int z = -radius; z <= radius; z++) {
                        BlockPos pos = center.offset(x, random.nextInt(4), z);
                        BlockState blockState = serverLevel.getBlockState(pos);
                        if (!blockState.isAir() && blockState.getBlock() instanceof DisappearingBlock) {
                            FallingBlockEntity.fall(serverLevel, pos, blockState);
                        }
                    }
                }

                AABB waveArea = new AABB(center.offset(-radius, -1, -radius), center.offset(radius, 2, radius));

                // ターゲットが存在する場合のみワープと攻撃を行う
                if (random.nextFloat() < 0.7) {
                    entity.teleportTo(targetPlayer.getX(), targetPlayer.getY(), targetPlayer.getZ());
                    serverLevel.getEntities(entity, waveArea).forEach(e -> {
                        e.hurt(DamagetypeRegister.causePunchDamage(this.entity), (float) 6);
                        e.setDeltaMovement(e.getDeltaMovement().add(random.nextFloat() - 0.5, 0.5, random.nextFloat() - 0.5)); // 吹き飛ばし
                    });
                    entity.getCommandSenderWorld().playSound(
                            null,
                            entity.blockPosition(),
                            SoundEvents.ANVIL_LAND, // 例：別の効果音を再生
                            SoundSource.BLOCKS,
                            2.0f, // 音量
                            1.0f  // ピッチ
                    );
                }
            }
            cooldownTimer = 40; // クールダウン設定
        }

        @Override
        public void tick() {
            if (cooldownTimer > 0) {
                cooldownTimer--;

            } else {
                this.start();
                if (entity.getCommandSenderWorld() instanceof ServerLevel serverLevel) {
                    for (int i = 0; i < 200; i++) { // 一度に5回処理
                        BlockPos randomPos = entity.blockPosition().offset(
                                random.nextInt(radius * 2 + 1) - radius,
                                random.nextInt(3) - 1,
                                random.nextInt(radius * 2 + 1) - radius
                        );
                        Block block = serverLevel.getBlockState(randomPos).getBlock();
                        if (block != Blocks.AIR) {
                            serverLevel.setBlock(randomPos, ModBlocks_Miniboss.DISAPPEARING_BLOCK.get().defaultBlockState(), 3);
                        }
                    }
                }
            }
        }
    }

    public static class SystemErrorGoal extends Goal {
        private final LivingEntity entity; // Nullエンティティ
        private final int radius; // 技の影響範囲
        private final int cooldownTicks; // クールダウン（tick単位）
        private int cooldownTimer = 0; // クールダウン用タイマー
        private final Random random = new Random();

        public SystemErrorGoal(LivingEntity entity, int radius, int cooldownTicks) {
            this.entity = entity;
            this.radius = radius;
            this.cooldownTicks = cooldownTicks;
        }

        @Override
        public boolean canUse() {
            // ゴールを開始する条件
            return true;
        }

        @Override
        public void start() {

            if (entity.getCommandSenderWorld().isClientSide) return;
            if (cooldownTimer > 0) return;
            entity.getCommandSenderWorld().getEntitiesOfClass(ServerPlayer.class, entity.getBoundingBox().inflate(radius))
                    .forEach(player -> {
                        float yaw = player.getYRot() + random.nextFloat() * 20 - 10;
                        float pitch = player.getXRot() + random.nextFloat() * 20 - 10;
                        player.setYRot(yaw);
                        player.setXRot(pitch);
                        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));
                        player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 10, 0));

                        // サウンドエフェクトを再生
                        player.level().playSound(
                                null,
                                player.blockPosition(),
                                SoundEvents.ENDERMAN_SCREAM,
                                SoundSource.PLAYERS,
                                1.0F,
                                1.0F + random.nextFloat() * 0.2F
                        );
                    });

            cooldownTimer = cooldownTicks; // クールダウンをリセット
        }

        @Override
        public void tick() {
            if (cooldownTimer > 0) {
                cooldownTimer--;
            } else {
                this.start();
            }
        }
    }

    public static class DataCollapseExplosionGoal extends Goal {
        private final LivingEntity entity; // Nullエンティティ
        private final int radius; // 爆発の影響範囲
        private final Random random = new Random();
        private int cooldownTimer = 100; // クールダウン用タイマー

        public DataCollapseExplosionGoal(LivingEntity entity, int radius) {
            this.entity = entity;
            this.radius = radius;
        }

        @Override
        public boolean canUse() {
            // ゴールが使用可能かどうか
            return true; // 常に実行可能
        }

        @Override
        public void start() {
            if (entity.getCommandSenderWorld() instanceof ServerLevel serverLevel) {
                BlockPos center = entity.blockPosition();
                AABB explosionArea = new AABB(center.offset(-radius, -1, -radius), center.offset(radius, 2, radius));
                serverLevel.getEntities(entity, explosionArea).forEach(e -> {
                    e.hurt(e.damageSources().magic(), 2f); // 高威力のダメージ
                    e.setDeltaMovement(e.getDeltaMovement().add(
                            random.nextInt(10),
                            9.5,
                            random.nextInt(10)
                    ));
                });
                // 周囲のブロックに影響を与える
                for (int x = -radius; x <= radius; x++) {
                    for (int z = -radius; z <= radius; z++) {
                        BlockPos pos = center.offset(x, random.nextInt(3) - 1, z); // ランダムな高さ
                        if (serverLevel.getBlockState(pos).getBlock() != Blocks.AIR) {
                            serverLevel.setBlock(pos, Blocks.CRYING_OBSIDIAN.defaultBlockState(), 5); // ブロックを変更
                            serverLevel.sendParticles(
                                    ParticleTypes.CLOUD,
                                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                                    10,
                                    0.5, 2, 0.5,
                                    0.05
                            );
                        }
                    }
                }

                // サウンドエフェクト
                serverLevel.playSound(
                        null,
                        center,
                        net.minecraft.sounds.SoundEvents.GENERIC_EXPLODE,
                        net.minecraft.sounds.SoundSource.HOSTILE,
                        3.0f, // 音量
                        0.7f + random.nextFloat() * 0.3f // ピッチ
                );
                cooldownTimer = 100;
            }
        }

        public void tick() {
            if (cooldownTimer >= 0) {
                cooldownTimer--;
            } else {
                this.start();
            }

        }
    }

    public static class DisintegrateBeamGoal extends Goal {
        private final LivingEntity entity; // Nullエンティティ
        private final int beamLength; // ビームの長さ
        private int durationTimer = 0; // ビームの持続時間タイマー
        private LivingEntity target; // ターゲットエンティティ
        private int cooldownTimer = 0; // クールダウンタイマー

        public DisintegrateBeamGoal(LivingEntity entity, int beamLength) {
            this.entity = entity;
            this.beamLength = beamLength;
        }

        @Override
        public boolean canUse() {
            // クールダウンが完了しているかチェック
            if (cooldownTimer > 0) {
                cooldownTimer--;
                return false;
            }

            // 最も近いプレイヤーやモブをターゲットとして設定
            if (entity.getCommandSenderWorld() instanceof ServerLevel serverLevel) {
                target = serverLevel.getNearestPlayer(entity, 20); // 半径20の範囲内
                return target != null;
            }
            return false;
        }

        @Override
        public void start() {
            durationTimer = 0;
            if (!(entity.getCommandSenderWorld() instanceof ServerLevel serverLevel)) return;
            serverLevel.playSound(
                    null,
                    entity.blockPosition(),
                    SoundEvents.END_PORTAL_FRAME_FILL,
                    SoundSource.HOSTILE,
                    1.5f,
                    1.0f
            );

            serverLevel.sendParticles(
                    ParticleTypes.ELECTRIC_SPARK,
                    entity.getX(), entity.getY() + 1, entity.getZ(),
                    30, 0.5, 0.5, 0.5, 0.1
            );
        }

        @Override
        public void tick() {
            if (!(entity.getCommandSenderWorld() instanceof ServerLevel serverLevel)) return;
            if (cooldownTimer > 0) {
                cooldownTimer--;
            } else {
                int maxDurationTicks = 60;
                if (durationTimer >= maxDurationTicks) {
                    this.stop();
                }
                if (target != null) {
                    Vec3 direction = target.position().subtract(entity.position()).normalize();
                    BlockPos beamStart = entity.blockPosition();

                    for (int i = 1; i <= beamLength; i++) {
                        BlockPos currentPos = beamStart.offset(
                                (int) (direction.x * i), (int) (direction.y * i), (int) (direction.z * i)
                        );
                        serverLevel.sendParticles(
                                ParticleTypes.SCULK_SOUL,
                                currentPos.getX() + 0.5, currentPos.getY() + 0.5, currentPos.getZ() + 0.5,
                                10, 0, 0, 0, 0.1
                        );

                        // ターゲットにダメージ
                        serverLevel.getEntities(null, new AABB(currentPos.offset(0, 0, 0), currentPos.offset(0, 0, 0)))
                                .forEach(e -> {
                                    if (e instanceof LivingEntity livingTarget) {
                                        livingTarget.hurt(DamagetypeRegister.causePunchDamage(this.entity), (float) 4);
                                    }
                                });

                        // 接触したブロックを破壊
                        BlockState currentState = serverLevel.getBlockState(currentPos);
                        if (!currentState.isAir()) {
                            serverLevel.removeBlock(currentPos, false);
                        }
                    }
                    durationTimer++;
                }
            }

        }

        @Override
        public void stop() {
            durationTimer = 0;
            cooldownTimer = 1000;
            if (entity.getCommandSenderWorld() instanceof ServerLevel serverLevel) {
                serverLevel.playSound(
                        null,
                        entity.blockPosition(),
                        SoundEvents.ENDER_DRAGON_GROWL,
                        SoundSource.HOSTILE,
                        2.0f,
                        1.0f
                );
            }
        }
    }

    public static class WaveOfNullityGoal extends Goal {
        private final LivingEntity entity; // Nullエンティティ
        private int cooldownTimer = 0; // クールダウンタイマー
        private final double range = 20.0; // 波動の範囲
        public WaveOfNullityGoal(LivingEntity entity) {
            this.entity = entity;
        }
        @Override
        public boolean canUse() {
            if (entity.getCommandSenderWorld() instanceof ServerLevel serverLevel) {
                return !serverLevel.getEntities(entity, entity.getBoundingBox().inflate(range)).isEmpty();
            }
            return false;
        }

        @Override
        public void start() {
            cooldownTimer = 20000;
            if (!(entity.getCommandSenderWorld() instanceof ServerLevel serverLevel)) return;
            applyWaveEffect(serverLevel);
        }
        private void applyWaveEffect(ServerLevel serverLevel) {
            AABB waveArea = entity.getBoundingBox().inflate(range);
            // 範囲内のエンティティにダメージ
            serverLevel.getEntities(entity, waveArea).forEach(e -> {
                if (e instanceof LivingEntity target) {
                    target.hurt(DamagetypeRegister.causePunchDamage(this.entity), (float) 8);
                }
            });

            // 範囲内のブロックを破壊
            BlockPos.betweenClosedStream((int) waveArea.minX, (int) waveArea.minY, (int) waveArea.minZ,
                            (int) waveArea.maxX, (int) waveArea.maxY, (int) waveArea.maxZ)
                    .forEach(pos -> {
                        if (!serverLevel.getBlockState(pos).isAir()) {
                            serverLevel.removeBlock(pos, false); // ブロックを破壊
                        }
                    });
            cooldownTimer = 66666666;
        }

        public void tick() {
            if (cooldownTimer > 0) {
                cooldownTimer--;
            } else {
                this.start();
            }
        }
    }
}

