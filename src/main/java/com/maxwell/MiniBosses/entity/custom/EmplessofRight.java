package com.maxwell.MiniBosses.entity.custom;


import com.maxwell.MiniBosses.event.BossInfoServer;
import com.maxwell.MiniBosses.register.ItemRegister_Maxwell;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;


public class EmplessofRight extends Mob implements GeoEntity {
    private final BossInfoServer bossEvent2 = new BossInfoServer(this.getDisplayName(), BossEvent.BossBarColor.PURPLE, true, 11);
    public void stopSeenByPlayer(@NotNull ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent2.removePlayer(player);
    }

    public void startSeenByPlayer(@NotNull ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent2.addPlayer(player);
    }

    public EmplessofRight(EntityType<? extends Mob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    protected static final RawAnimation MOVE = RawAnimation.begin().thenLoop("tossin");

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Movecontroler", 10, this::movecontroler).triggerableAnim("move",MOVE));
    }

    protected <E extends EmplessofRight> PlayState movecontroler(final AnimationState<E> event) {

        if (event.isMoving()) {
            return event.setAndContinue(MOVE);
        }
        return PlayState.STOP;
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (amount >= 1) {
            amount = 1;
        }
        Entity attacker = source.getEntity();
        if (attacker instanceof LivingEntity) {
            double distance = this.distanceTo(attacker);

            if (distance > 100) { // 10ブロック以上ならダメージ無効
                return false;
            }
            else
            {
                this.removeAllEffects();
            }
        }
        this.bossEvent2.setProgress(this.getHealth() / this.getMaxHealth());
        return super.hurt(source, amount);
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0f)
                .add(Attributes.MOVEMENT_SPEED, 20) // 移動速度
                .add(Attributes.ATTACK_DAMAGE, 99) // 攻撃力
                .add(Attributes.MAX_HEALTH, 200) // 最大HP
                .add(Attributes.KNOCKBACK_RESISTANCE, 9999); // ノックバック耐性
    }
    @Override
    public void die(@NotNull DamageSource source)
    {
        this.spawnAtLocation(ItemRegister_Maxwell.TERRAPRISM.get());
    }
    private static final float SPEED = 1.35F; // ✅ 突進速度
    private static final float DAMAGE = 999999999; // ✅ ダメージ量
    private static final int CHARGE_DURATION = 26; // ✅ 突進時間 (2秒)
    private static final int COOLDOWN_DURATION = 13; // ✅ クールダウン時間 (3秒 = 60 ticks)

    private int chargeTime = 0;
    private int cooldownTime = 0;
    private Player targetPlayer;



    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide) {
            if (cooldownTime > 0) {
                cooldownTime--; // ✅ クールダウン中は何もしない
                return;
            }

            if (targetPlayer == null || targetPlayer.isDeadOrDying()) {
                targetPlayer = level().getNearestPlayer(this, 20); // ✅ 20ブロック以内のプレイヤーをターゲット
            }

            if (targetPlayer != null) {
                chargeAtPlayer(targetPlayer);
            }

            // ✅ 突進時間が終了したら停止し、クールダウン開始
            if (chargeTime > CHARGE_DURATION) {
                setDeltaMovement(0, 0, 0);
                chargeTime = 0;
                cooldownTime = COOLDOWN_DURATION; // ✅ クールダウン開始
            }
        }
    }

    private void chargeAtPlayer(Player player) {
        Vec3 direction = new Vec3(player.getX() - this.getX(), player.getY() - this.getY(), player.getZ() - this.getZ()).normalize();
        setDeltaMovement(direction.scale(SPEED)); // ✅ プレイヤーの方向に突進
        chargeTime++;

        // ✅ 突進方向を取得し、視線を合わせる
        Vec3 movement = getDeltaMovement();
        if (!movement.equals(Vec3.ZERO)) {
            double horizontalDistance = Math.sqrt(movement.x * movement.x + movement.z * movement.z);
            this.setYRot((float) (Math.toDegrees(Math.atan2(movement.x, movement.z)))); // ✅ Y軸回転を移動方向に設定
            this.setXRot((float) (-Math.toDegrees(Math.atan2(movement.y, horizontalDistance)))); // ✅ X軸回転を移動方向に設定
        }

        // ✅ 進行方向にあるブロックを破壊
        destroyBlocksInPath();

        // ✅ プレイヤーに衝突したらダメージを与え、クールダウン開始
        if (this.getBoundingBox().intersects(player.getBoundingBox())) {
            player.hurt(damageSources().mobAttack(this), DAMAGE);
            chargeTime = CHARGE_DURATION; // ✅ 衝突後、突進を停止
            cooldownTime = COOLDOWN_DURATION; // ✅ クールダウン開始
        }
    }

    private void destroyBlocksInPath() {
        Vec3 movement = getDeltaMovement();
        BlockPos currentPos = this.blockPosition();
        for (int i = 1; i <= 3; i++) { // ✅ 3ブロック先までチェック
            BlockPos targetPos = currentPos.offset((int) (movement.x * i), (int) (movement.y * i), (int) (movement.z * i));
            BlockState blockState = level().getBlockState(targetPos);
            if (!blockState.isAir()) { // ✅ 空気ブロックでないなら破壊
                level().destroyBlock(targetPos, true);
            }
        }
    }
}
