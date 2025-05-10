package com.maxwell.MiniBosses.entity.custom;

import com.maxwell.MiniBosses.register.ItemRegister_Maxwell;
import com.maxwell.MiniBosses.register.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class boundingbox extends Monster implements GeoEntity {
    public boundingbox(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0)
                .add(Attributes.MOVEMENT_SPEED, 0)
                .add(Attributes.ATTACK_DAMAGE, 10)
                .add(Attributes.MAX_HEALTH, 200)
                .add(Attributes.ARMOR, 40)
                .add(Attributes.KNOCKBACK_RESISTANCE, 9999);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void tick() {
        super.tick();

        // 向きに基づく移動量を計算
        double speed = 0.1; // 移動速度
        double radians = Math.toRadians(this.getYRot()); // Y方向の回転をラジアンに変換
        double dx = -Math.sin(radians) * speed; // X方向の移動量
        double dz = Math.cos(radians) * speed;  // Z方向の移動量

        // 前方にブロックがあるか確認
        double nextX = this.getX() + 1;
        double nextY = this.getY();
        double nextZ = this.getZ() + 1;

        if (!this.level().isClientSide) {
            BlockPos entityPos = this.blockPosition();

            // 周囲1マスのブロックを破壊
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        BlockPos targetPos = entityPos.offset(x, y, z);
                        BlockState blockState = this.level().getBlockState(targetPos);

                        if (!blockState.isAir()) { // ブロックが空気でない場合削除
                            this.level().destroyBlock(targetPos, false); // ブロック破壊（アイテムドロップなし）
                            this.level().playSound(null, targetPos, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1.0F, 1.0F);
                            this.level().addParticle(ParticleTypes.EXPLOSION, targetPos.getX(), targetPos.getY(), targetPos.getZ(), 0, 0, 0);
                        }
                    }
                }
            }
        }
        this.setDeltaMovement(0, Math.sin(this.tickCount / 10.0) * 0.05, 0);
        this.setDeltaMovement(dx, this.getDeltaMovement().y, dz);
        this.move(MoverType.SELF, this.getDeltaMovement());
        if (this.getTarget() == null) {
            LivingEntity targetEntity = this.level().getNearestPlayer(this, 20); // 半径20ブロックでプレイヤーを検索
            this.setTarget(targetEntity); // ターゲットを設定
        }
    }

    @Override
    public boolean isAlliedTo(@NotNull Entity entity) {
        return true; // 他のエンティティが乗れるようにする
    }

    @Override
    public boolean canCollideWith(@NotNull Entity entity) {
        return !(entity instanceof Player);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    private float customScale = 1.6F;

    public float getCustomScale() {
        return this.customScale;
    }

    public void setCustomScale(float scale) {
        this.customScale = scale;
    }
    @Override
    public void die(@NotNull DamageSource source)
    {
        this.spawnAtLocation(ItemRegister_Maxwell.GHOST_INGOT.get());
        this.spawnAtLocation(ItemRegister_Maxwell.GHOST_INGOT.get());
        this.spawnAtLocation(ItemRegister_Maxwell.GHOST_INGOT.get());
        this.spawnAtLocation(ItemRegister_Maxwell.GHOST_INGOT.get());
    }
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new TNTAttackGoal(this));
        this.goalSelector.addGoal(1, new DirectionalTNTGoal(this));
        this.goalSelector.addGoal(1, new SideTNTAttackGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true)); // プレイヤーをターゲット
    }


    public static class TNTAttackGoal extends Goal {
        private final boundingbox shooter; // TNTを発射するエンティティ
        private int cooldown; // クールダウンタイマー

        public TNTAttackGoal(boundingbox shooter) {
            this.shooter = shooter;
            this.cooldown = 60;
        }
        @Override
        public boolean canUse() {
            return true;
        }
        @Override
        public void start() {
            LivingEntity target = this.shooter.getTarget();
            if (target != null) {
                PrimedTnt tnt = new PrimedTnt(EntityType.TNT,this.shooter.level());
                double dx = target.getX() - this.shooter.getX();
                double dy = target.getY() + target.getBbHeight() / 2.0 - (this.shooter.getY() + this.shooter.getBbHeight() / 2.0);
                double dz = target.getZ() - this.shooter.getZ();

                tnt.setDeltaMovement(dx * 0.05, dy * 0.05, dz * 0.05);
                this.shooter.level().addFreshEntity(tnt);
                this.shooter.level().playSound(null, this.shooter.blockPosition(), SoundEvents.TNT_PRIMED, SoundSource.HOSTILE, 1.0F, 1.0F);
                this.cooldown = 60; // 60tick (3秒) のクールダウン
                this.shooter.level().addParticle(ParticleTypes.EXPLOSION, this.shooter.getX(), this.shooter.getY(), this.shooter.getZ(), 0, 0, 0);

            } else {
                this.cooldown = 60; // クールダウンをリセットして再発動
            }
        }


        @Override
        public void tick() {
            if (cooldown > 0) {
                cooldown--;
            } else {
                LivingEntity target = this.shooter.getTarget();
                if (target != null) {
                    this.start();
                } else {
                    cooldown = 60; // クールダウンをリセット
                }
            }
        }
    }
    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        if (!(this.getCommandSenderWorld() instanceof ServerLevel serverLevel)) return;
        for (int i = 0; i < 2; i++) {
            ModEntities.BUTTON.get().spawn(serverLevel, null, (Player) null, this.blockPosition().offset(1, 0, i),
                    MobSpawnType.EVENT, true, false);
        }
    }
    public int a;
    @Override
    public boolean hurt(DamageSource source, float amount) {
        button button = new button(ModEntities.BUTTON.get(),level());
        a = button.getValue();
        if (a == 0) {
            return super.hurt(source, amount);
        }
        return false;
    }
    public static class DirectionalTNTGoal extends Goal {
        private final Mob shooter; // TNTを発射するエンティティ
        private int cooldown; // クールダウンタイマー

        public DirectionalTNTGoal(Mob shooter) {
            this.shooter = shooter;
            this.cooldown = 60; // 初期クールダウン設定（例: 3秒）
        }

        @Override
        public boolean canUse() {
            return true; // 常にゴールが使用可能（必要に応じて条件を追加）
        }

        @Override
        public void tick() {
            if (cooldown > 0) {
                cooldown--;
            } else {
                // TNTを発射
                fireTNT();
                cooldown = 60; // クールダウンをリセット
            }
        }

        private void fireTNT() {
            // TNTエンティティを生成
            PrimedTnt tnt = new PrimedTnt(this.shooter.level(), this.shooter.getX(), this.shooter.getY() + this.shooter.getBbHeight() / 2.0, this.shooter.getZ(), this.shooter);

            // 視線方向を計算
            double yawRadians = Math.toRadians(this.shooter.getYRot()); // Y方向の回転（水平）
            double pitchRadians = Math.toRadians(this.shooter.getXRot()); // X方向の回転（垂直）

            double dx = -Math.sin(yawRadians) * Math.cos(pitchRadians); // X方向
            double dy = -Math.sin(pitchRadians); // Y方向
            double dz = Math.cos(yawRadians) * Math.cos(pitchRadians); // Z方向

            // TNTの移動量を設定
            tnt.setDeltaMovement(dx * 1.5, dy * 1.5, dz * 1.5); // 速度倍率を調整（例: 1.5）

            // ワールドにTNTをスポーン
            this.shooter.level().addFreshEntity(tnt);
            this.shooter.level().addParticle(ParticleTypes.EXPLOSION, this.shooter.getX(), this.shooter.getY(), this.shooter.getZ(), 0, 0, 0);

            // サウンドを再生
            this.shooter.level().playSound(null, this.shooter.blockPosition(), SoundEvents.TNT_PRIMED, SoundSource.HOSTILE, 1.0F, 1.0F);
        }
    }
    public static class SideTNTAttackGoal extends Goal {
        private final Mob shooter; // TNTを発射するエンティティ
        private int cooldown; // クールダウンタイマー

        public SideTNTAttackGoal(Mob shooter) {
            this.shooter = shooter;
            this.cooldown = 120; // 初期クールダウン設定（例: 3秒）
        }

        @Override
        public boolean canUse() {
            return this.shooter.getTarget() != null; // ターゲットが存在する場合のみ実行
        }
        @Override
        public void tick() {
            if (cooldown > 0) {
                cooldown--;
            } else {
                fireTNTSeries();
                cooldown = 120; // クールダウンをリセット
            }
        }

        private void fireTNTSeries() {
            LivingEntity target = this.shooter.getTarget();
            if (target != null) {
                double targetX = target.getX();
                double targetZ = target.getZ();
                double shooterX = this.shooter.getX();
                double shooterZ = this.shooter.getZ();
                boolean isOnSideX = Math.abs(targetX - shooterX) > Math.abs(targetZ - shooterZ);
                for (int i = 0; i < 3; i++) {
                    double offsetX = isOnSideX ? shooterX + (i - 1) : shooterX;
                    double offsetZ = isOnSideX ? shooterZ : shooterZ + (i - 1);
                    double spawnY = this.shooter.getY() + 1.0;
                    PrimedTnt tnt = new PrimedTnt(this.shooter.level(), offsetX, spawnY, offsetZ, this.shooter);
                    double dx = targetX - offsetX;
                    double dy = target.getY() + target.getBbHeight() / 2.0 - spawnY;
                    double dz = targetZ - offsetZ;

                    tnt.setDeltaMovement(dx * 0.05, dy * 0.05, dz * 0.05);

                    // TNTをワールドに追加
                    this.shooter.level().addFreshEntity(tnt);
                    this.shooter.level().playSound(null, this.shooter.blockPosition(), SoundEvents.TNT_PRIMED, SoundSource.HOSTILE, 1.0F, 1.0F);
                }
            }
        }
    }

}
