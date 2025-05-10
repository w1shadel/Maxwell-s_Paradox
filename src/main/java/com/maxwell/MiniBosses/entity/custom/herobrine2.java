package com.maxwell.MiniBosses.entity.custom;

import com.maxwell.MiniBosses.DamagetypeRegister;
import com.maxwell.MiniBosses.event.BossInfoServer;
import com.maxwell.MiniBosses.register.ItemRegister_Maxwell;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
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

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class herobrine2 extends Monster implements GeoEntity {
    private final BossInfoServer bossEvent3 = new BossInfoServer(this.getDisplayName(), BossEvent.BossBarColor.PURPLE, true, 11);
    public herobrine2(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    public void stopSeenByPlayer(@NotNull ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent3.removePlayer(player);
    }
    public void startSeenByPlayer(@NotNull ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent3.addPlayer(player);
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0)
                .add(Attributes.MOVEMENT_SPEED, 1)
                .add(Attributes.ATTACK_DAMAGE, 12)
                .add(Attributes.ARMOR, 666)
                .add(Attributes.MAX_HEALTH, 666)
                .add(Attributes.KNOCKBACK_RESISTANCE, 99999);
    }
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (this.getHealth() == 1)
        {
            this.triggerAnim("HerobrineAttacktrriger", "theend2");
            this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,999999999,255,true,true));
            this.goalSelector.addGoal(1, new deadGoal(this));
            return false;
        }
        Entity attacker = source.getEntity();
        if (attacker instanceof LivingEntity) {
            double distance = this.distanceTo(attacker);
            if (distance > 10.0) {
                this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY,99999,255,true,true));
                return false;
            }
            else
            {
                this.removeAllEffects();
            }
        }
        this.bossEvent3.setProgress(this.getHealth() / this.getMaxHealth());
        return super.hurt(source, amount);
    }
    @Override
    public void die(@NotNull DamageSource source)
    {
        this.spawnAtLocation(ItemRegister_Maxwell.STEAVEN_INGOT.get());
        this.spawnAtLocation(ItemRegister_Maxwell.STEAVEN_INGOT.get());
        this.spawnAtLocation(ItemRegister_Maxwell.STEAVEN_INGOT.get());
        this.spawnAtLocation(ItemRegister_Maxwell.STEAVEN_INGOT.get());
        this.spawnAtLocation(ItemRegister_Maxwell.STEAVEN_INGOT.get());
        this.spawnAtLocation(ItemRegister_Maxwell.STEAVEN_INGOT.get());
        this.spawnAtLocation(ItemRegister_Maxwell.STEAVEN_INGOT.get());
        this.spawnAtLocation(ItemRegister_Maxwell.STEAVEN_INGOT.get());
        this.spawnAtLocation(ItemRegister_Maxwell.STEAVEN_INGOT.get());
    }
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new AbyssJudgmentGoal(this));
        this.goalSelector.addGoal(1, new ShadowStrikeGoal(this));
        this.goalSelector.addGoal(1, new SilenceJudgmentGoal(this));
        this.goalSelector.addGoal(1, new PhantomStrikeGoal(this));
        this.goalSelector.addGoal(1, new DarkWaveGoal(this));
        this.goalSelector.addGoal(1, new PhantomJumpGoal(this));
        this.goalSelector.addGoal(1, new PhantomLightningGoal(this));
        this.goalSelector.addGoal(1, new DarkDomainGoal(this));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.9F));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    protected static final RawAnimation SUMMON = RawAnimation.begin().thenPlay("summon");
    protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    protected static final RawAnimation MOVE = RawAnimation.begin().thenLoop("move");
    protected static final RawAnimation TELEPORT = RawAnimation.begin().thenPlay("teleport");
    protected static final RawAnimation LIGHTNING = RawAnimation.begin().thenPlay("ligtning");
    protected static final RawAnimation THEEND1 = RawAnimation.begin().thenPlay("theend");
    protected static final RawAnimation THEEND2 = RawAnimation.begin().thenPlay("theend2");
    protected static final RawAnimation THEEND3 = RawAnimation.begin().thenLoop("theend_long");

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Movecontroler", 6, this::movecontroler).triggerableAnim("summon", SUMMON));
        controllers.add(new AnimationController<>(this, "HerobrineAttacktrriger", 0, this::AttackController)
                .triggerableAnim("teleport", TELEPORT)
                .triggerableAnim("ligtning", LIGHTNING)
                .triggerableAnim("theend2", THEEND2)
                .triggerableAnim("theend_long", THEEND3)
                .triggerableAnim("theend", THEEND1));
    }
    @Override
    public void onAddedToWorld() {
        this.triggerAnim("Movecontroler", "summon");
        this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 45,255,true,true)); // 一時的に透明化
    }
    protected <E extends herobrine2> PlayState movecontroler(final AnimationState<E> event) {
        if (event.isMoving()) {
            return event.setAndContinue(MOVE);
        } else {
            return event.setAndContinue(IDLE);
        }
    }
    public int attackType;
    protected <E extends herobrine2> PlayState AttackController(final AnimationState<E> event) {
        return switch (attackType) {
            case 1 -> event.setAndContinue(TELEPORT); // Swing Attack
            case 2 -> event.setAndContinue(LIGHTNING); // Throw Attack
            case 3 -> event.setAndContinue(THEEND1); // Flying Attack
            case 4 -> event.setAndContinue(THEEND2); // Flying Attack
            case 5 -> event.setAndContinue(THEEND3); // Flying Attack
            default -> PlayState.STOP; // アニメーションを停止
        };
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
    public static class DarkWaveGoal extends Goal {
        private final herobrine2 mob;
        private int tickCounter = 0;

        public DarkWaveGoal(herobrine2 mob) {
            this.mob = mob;
        }

        @Override
        public boolean canUse() {
            return mob.getTarget() != null && mob.getTarget().distanceTo(mob) < 4;
        }

        @Override
        public void tick() {
            tickCounter++;

            if (tickCounter == 20) { // 1秒ごとに発動
                List<LivingEntity> entities = mob.level().getEntitiesOfClass(LivingEntity.class, mob.getBoundingBox().inflate(4));

                for (LivingEntity entity : entities) {
                    if (entity != mob) {
                        Vec3 knockback = entity.position().subtract(mob.position()).normalize().scale(0.5); // 軽い押し出し
                        entity.setDeltaMovement(knockback);
                    }
                }
            }
        }
    }
    public static class PhantomJumpGoal extends Goal {
        private final herobrine2 mob;

        public PhantomJumpGoal(herobrine2 mob) {
            this.mob = mob;
        }

        @Override
        public boolean canUse() {
            return mob.getHealth() < mob.getMaxHealth() * 0.7;
        }

        @Override
        public void start() {
            Vec3 newPos = mob.position().add((Math.random() - 0.5) * 6, 0, (Math.random() - 0.5) * 6);
            mob.teleportTo(newPos.x, newPos.y, newPos.z);
        }
    }
    public static class deadGoal extends Goal {
        private final herobrine2 mob;
        private Player targetPlayer;
        private int tickCounter = 0;
        private int cooldown;

        public deadGoal(herobrine2 mob) {
            this.mob = mob;
        }

        @Override
        public boolean canUse() {
            return true;
        }


        @Override
        public void tick() {
            tickCounter++;
            if (tickCounter == 25) {
                this.mob.triggerAnim("HerobrineAttacktrriger", "theend_long");
            }
            if (tickCounter == 40) {
                {
                  this.mob.discard();
                }
            }
        }
    }
    public class ShadowStrikeGoal extends Goal {
        private final herobrine2 mob;
        private Player targetPlayer;
        private int tickCounter = 0;
        private int cooldown;

        public ShadowStrikeGoal(herobrine2 mob) {
            this.mob = mob;
        }

        @Override
        public boolean canUse() {
            this.targetPlayer = mob.level().getNearestPlayer(mob, 20);
            return this.targetPlayer != null;
        }


        @Override
        public void tick() {

            if (cooldown > 0) {
                cooldown--;
            } else {
                tickCounter++;
                if (tickCounter == 1) {
                    this.mob.triggerAnim("HerobrineAttacktrriger", "teleport");
                }
                if (tickCounter == 17) {
                    {
                        mob.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 43)); // 一時的に透明化
                    }
                    if (tickCounter == 60) { // 3秒後に背後へワープ
                        Vec3 behindPosition = targetPlayer.position().add(-2, 0, -2);
                        mob.teleportTo(behindPosition.x, behindPosition.y, behindPosition.z);
                        targetPlayer.hurt(damageSources().mobAttack(mob), 12.0F);
                        cooldown = 180;
                    }
                }
            }
        }
    }
    public class PhantomStrikeGoal extends Goal {
        private final herobrine2 mob;
        private final int attackDurationTicks = 160; // 8秒間滞留
        private int tickCounter = 0;

        private final Set<LivingEntity> affectedEntities = new HashSet<>(); // ダメージ適用対象
        private Vec3 teleportDirection;

        public PhantomStrikeGoal(herobrine2 mob) {
            this.mob = mob;
        }

        @Override
        public boolean canUse() {
            return mob.getTarget() != null;
        }

        @Override
        public void start() {
            teleportDirection = new Vec3(-mob.getLookAngle().x * 0.5, 0, -mob.getLookAngle().z * 0.5); // 後退方向設定
            createAttackZone(); // 攻撃判定の作成
        }

        @Override
        public void tick() {
            tickCounter++;

            // ヘロブラインを徐々に後退させる
            if (tickCounter % 20 == 0) { // 毎秒1回
                Vec3 newPosition = mob.position().add(teleportDirection);
                mob.teleportTo(newPosition.x, newPosition.y, newPosition.z);
            }

            // 影の攻撃判定（ダメージ適用）
            if (tickCounter < attackDurationTicks) {
                applyAttackDamage();
            }
        }

        @Override
        public boolean canContinueToUse() {
            return tickCounter < attackDurationTicks;
        }
        private void createAttackZone() {
            AABB attackArea = mob.getBoundingBox().inflate(5); // 半径5ブロック
            affectedEntities.addAll(mob.level().getEntitiesOfClass(LivingEntity.class, attackArea));
        }
        private void applyAttackDamage() {
            AABB attackArea = mob.getBoundingBox().inflate(5); // 半径5ブロックの範囲
            List<LivingEntity> entities = mob.level().getEntitiesOfClass(LivingEntity.class, attackArea);

            for (LivingEntity entity : entities) {
                if (entity != mob) { // ヘロブライン自身を除外
                    entity.hurt(damageSources().mobAttack(mob), 4.0F); // シンプルなダメージ処理
                }
            }
        }

    }
    public class SilenceJudgmentGoal extends Goal {
        private final herobrine2 mob;
        private Player targetPlayer;
        private int tickCounter = 0;
        public SilenceJudgmentGoal(herobrine2 mob) {
            this.mob = mob;
        }
        @Override
        public boolean canUse() {
            this.targetPlayer = mob.level().getNearestPlayer(mob, 20);
            return this.targetPlayer != null;
        }
        @Override
        public void start() {
            mob.level().playSound(null, mob.getX(), mob.getY(), mob.getZ(), SoundEvents.AMBIENT_CAVE.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
            tickCounter = 0;
        }
        @Override
        public void tick() {
            tickCounter++;
            if (tickCounter == 50 && !Isjudged) {
                this.mob.triggerAnim("HerobrineAttacktrriger", "theend");
                targetPlayer.displayClientMessage(Component.literal("死を受け入れよ"), true);
                this.mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 399, 255, true, true));
            }
            if (tickCounter == 400 && !Isjudged) {
                targetPlayer.hurt(DamagetypeRegister.causeSwordDamage(mob), 9999F);
                this.mob.removeAllEffects();
                Isjudged = true;
            }
        }
    }
    public boolean Isjudged;
    public class PhantomLightningGoal extends Goal {
        private final herobrine2 mob;
        private Player targetPlayer;
        private int tickCounter = 0;
        private int cooldown;

        public PhantomLightningGoal(herobrine2 mob) {
            this.mob = mob;
        }
        @Override
        public boolean canUse() {
            this.targetPlayer = mob.level().getNearestPlayer(mob, 15);
            return this.targetPlayer != null;
        }
        @Override
        public void tick() {
            if (cooldown > 0) {
                cooldown--;
            } else {
                tickCounter++;
                if (tickCounter == 1) {
                    this.mob.triggerAnim("HerobrineAttacktrriger", "ligtning");
                    IsLightning = true;
                }
                if (tickCounter == 100) {
                    LightningBolt fakeBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, mob.level());
                    fakeBolt.setPos(targetPlayer.getX(), targetPlayer.getY(), targetPlayer.getZ());
                    mob.level().addFreshEntity(fakeBolt); // フェイク雷をスポーン
                } else if (tickCounter == 120) { // 1秒後に本物の雷
                    LightningBolt realBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, mob.level());
                    realBolt.setPos(targetPlayer.getX(), targetPlayer.getY(), targetPlayer.getZ());
                    mob.level().addFreshEntity(realBolt);
                    targetPlayer.hurt(damageSources().lightningBolt(), 10.0F);
                    tickCounter = 0;
                    cooldown = 100;
                    IsLightning = false;
                }
            }
        }
        public boolean IsLightning;
    }
    public static class DarkDomainGoal extends Goal {
        private final herobrine2 mob;
        private Player targetPlayer;

        public DarkDomainGoal(herobrine2 mob) {
            this.mob = mob;
        }

        @Override
        public boolean canUse() {
            this.targetPlayer = mob.level().getNearestPlayer(mob, 15);
            return this.targetPlayer != null;
        }

        @Override
        public void start() {
            if (!mob.level().isClientSide) { // サーバー側でのみ実行
                ServerLevel serverLevel = Objects.requireNonNull(mob.getServer()).getLevel(Level.OVERWORLD);
                Objects.requireNonNull(serverLevel).setDayTime(13000);
                serverLevel.playSound(null, mob.getX(), mob.getY(), mob.getZ(), SoundEvents.WITHER_SPAWN, SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }
        @Override
        public void tick() {
            BlockPos pos = targetPlayer.blockPosition().offset((int) (Math.random() * 4 - 2), 0, (int) (Math.random() * 4 - 2));
            mob.level().removeBlock(pos, false);
        }
    }
    public static class AbyssJudgmentGoal extends Goal {
        private final herobrine2 mob;
        private int tickCounter = 0;


        public AbyssJudgmentGoal(herobrine2 mob) {
            this.mob = mob;
        }

        @Override
        public boolean canUse() {
            return this.mob.getHealth() == 1;
        }
        @Override
        public void start() {
            tickCounter = 0;
            this.mob.triggerAnim("HerobrineAttacktrriger", "theend");
        }
        @Override
        public void tick() {
            tickCounter++;
            System.out.println(tickCounter);
            if (!(mob.getCommandSenderWorld() instanceof ServerLevel serverLevel)) return;
            if (tickCounter % 40 == 0) {
                BlockPos pos = mob.blockPosition().offset((int) (Math.random() * 5 - 2), -1, (int) (Math.random() * 5 - 2));
                mob.level().removeBlock(pos, false);
            }
            if (tickCounter == 400) {
                mob.level().explode(mob, mob.getX(), mob.getY(), mob.getZ(), 10.0F, Level.ExplosionInteraction.NONE);
            applyWaveEffect(serverLevel);
            }
            if (tickCounter == 401)
            {
                this.mob.triggerAnim("HerobrineAttacktrriger", "theend");
                this.mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 999999999,9999,true,true));
            }
            if (tickCounter == 427)
            {
                this.mob.triggerAnim("HerobrineAttacktrriger", "theend_long");
            }
        }
        private void applyWaveEffect(ServerLevel serverLevel) {
            AABB waveArea = mob.getBoundingBox().inflate(20);
            // 範囲内のエンティティにダメージ
            serverLevel.getEntities(mob, waveArea).forEach(e -> {
                if (e instanceof LivingEntity target) {
                    target.hurt(DamagetypeRegister.causePunchDamage(this.mob), (float) 8);
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
        }
    }
}
