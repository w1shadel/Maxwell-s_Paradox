package com.maxwell.MiniBosses.entity.custom;

import com.maxwell.MiniBosses.DamagetypeRegister;
import com.maxwell.MiniBosses.event.BossInfoServer;
import com.maxwell.MiniBosses.register.ItemRegister_Maxwell;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.Level;
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

import java.util.Objects;

public class herobrine extends Monster implements GeoEntity {
    private final BossInfoServer bossEvent2 = new BossInfoServer(this.getDisplayName(), BossEvent.BossBarColor.PURPLE, true, 11);

    public herobrine(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public void stopSeenByPlayer(@NotNull ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent2.removePlayer(player);
    }

    public void startSeenByPlayer(@NotNull ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent2.addPlayer(player);
    }

    protected static final RawAnimation MOVE = RawAnimation.begin().thenLoop("move");
    protected static final RawAnimation ATK1 = RawAnimation.begin().thenPlay("atk");
    protected static final RawAnimation ATK2 = RawAnimation.begin().thenPlay("atk2");
    protected static final RawAnimation ATK3 = RawAnimation.begin().thenPlay("atk3");

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Movecontroler", 0, this::movecontroler));
        controllers.add(new AnimationController<>(this, "HerobrineAttacktrriger", 0, this::AttackController)
                .triggerableAnim("atk", ATK1)
                .triggerableAnim("atk2", ATK2)
                .triggerableAnim("atk3", ATK3));
    }

    protected <E extends herobrine> PlayState movecontroler(final AnimationState<E> event) {

        if (event.isMoving() ) {
            return event.setAndContinue(MOVE);
        }
        return PlayState.STOP;
    }
    public int attackType;
    protected <E extends herobrine> PlayState AttackController(final AnimationState<E> event) {
        return switch (attackType) {
            case 1 -> event.setAndContinue(ATK1); // Swing Attack
            case 2 -> event.setAndContinue(ATK2); // Throw Attack
            case 3 -> event.setAndContinue(ATK3); // Flying Attack
            default -> PlayState.STOP; // アニメーションを停止
        };
    }
    @Override
    public void die(@NotNull DamageSource source) {
        this.spawnAtLocation(ItemRegister_Maxwell.GHOST_INGOT.get());
        this.spawnAtLocation(ItemRegister_Maxwell.GHOST_INGOT.get());
        this.spawnAtLocation(ItemRegister_Maxwell.GHOST_INGOT.get());
        this.spawnAtLocation(ItemRegister_Maxwell.GHOST_INGOT.get());
        this.spawnAtLocation(ItemRegister_Maxwell.GHOST_INGOT.get());
        this.spawnAtLocation(ItemRegister_Maxwell.GHOST_INGOT.get());
        this.spawnAtLocation(ItemRegister_Maxwell.GHOST_INGOT.get());
        this.spawnAtLocation(ItemRegister_Maxwell.GHOST_INGOT.get());

    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0)
                .add(Attributes.MOVEMENT_SPEED, 1)
                .add(Attributes.ATTACK_DAMAGE, 12)
                .add(Attributes.ARMOR, 9999)
                .add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.KNOCKBACK_RESISTANCE, 99999);
    }

    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (amount >= 1) {
            amount = 1;
        }
        Entity attacker = source.getEntity();
        if (attacker instanceof LivingEntity) {
            double distance = this.distanceTo(attacker);

            if (distance > 10.0) { // 10ブロック以上ならダメージ無効
                this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY,99999,255,true,true));
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
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new UPtoDownAttack(this));
        this.goalSelector.addGoal(1, new StabAttack(this));
        this.goalSelector.addGoal(1, new ShadowStrikeGoal(this));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.9F));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new ChaseTargetGoal(this, 0.6, 15));
    }

    public static class ChaseTargetGoal extends Goal {
        private final herobrine mob;
        private LivingEntity target;
        private final double speedModifier; // 追尾速度
        private final float chaseRange;    // 追尾範囲

        public ChaseTargetGoal(herobrine mob, double speedModifier, float chaseRange) {
            this.mob = mob;
            this.speedModifier = speedModifier;
            this.chaseRange = chaseRange;
        }

        @Override
        public boolean canUse() {
            LivingEntity currentTarget = this.mob.getTarget();
            if (currentTarget != null && currentTarget.isAlive() && this.mob.distanceTo(currentTarget) <= chaseRange) {
                this.target = currentTarget;
                return true;
            }
            return false;
        }

        @Override
        public void start() {
            // 追尾を開始
            if (this.target != null) {
                this.mob.getNavigation().moveTo(this.target, speedModifier);
            }
        }

        @Override
        public void stop() {
            // 追尾を停止
            this.target = null;
            this.mob.getNavigation().stop();
        }

        @Override
        public void tick() {
            // 毎ティックでターゲットに向かって移動
            if (this.target != null && this.target.isAlive() && this.mob.distanceTo(this.target) <= chaseRange) {
                this.mob.getNavigation().moveTo(this.target, speedModifier);
                this.mob.lookAt(this.target, 30.0F, 30.0F); // ターゲットを注視
            } else {
                stop();
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.target != null && this.target.isAlive() && this.mob.distanceTo(this.target) <= chaseRange;
        }
    }

    public class UPtoDownAttack extends Goal {
        private final herobrine mob;
        private int cooldownTimer; // クールダウンタイマー// クールダウンの長さ
        public int attackallticks;

        public UPtoDownAttack(herobrine mob) {
            this.mob = mob;
            this.cooldownTimer = 3;
        }

        @Override
        public boolean canUse() {
            if (this.mob == null || this.mob.getTarget() == null) {
                return false; // nullの場合は実行不可
            }
            return this.mob.distanceTo(this.mob.getTarget()) < 20.0;
        }


        @Override
        public void tick() {
            this.cooldownTimer--;
            LivingEntity target = this.mob.getTarget();
            double attackDamage = Objects.requireNonNull(mob.getAttribute(Attributes.ATTACK_DAMAGE)).getValue() + Objects.requireNonNull(target).getHealth() / 4;
            if (this.mob.distanceTo(Objects.requireNonNull(target)) <= 9.0 ) {
                if (this.cooldownTimer <= 0) {
                    this.attackallticks++;
                    if (this.attackallticks == 1) {
                        this.mob.triggerAnim("HerobrineAttacktrriger", "atk");
                    }
                    if (this.attackallticks == 24) {
                        ItemStack offhandItem = target.getOffhandItem();
                        ItemStack mainhanditem = target.getMainHandItem();// Offhandのアイテムを取得
                        if (offhandItem.getItem() instanceof ShieldItem) {
                            target.stopUsingItem();
                            offhandItem.hurtAndBreak(5, target, (entity) -> entity.broadcastBreakEvent(InteractionHand.OFF_HAND));
                            target.level().playSound(null, target.blockPosition(), SoundEvents.SHIELD_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
                        }
                        if (mainhanditem.getItem() instanceof ShieldItem) {
                            target.stopUsingItem();
                            offhandItem.hurtAndBreak(5, target, (entity) -> entity.broadcastBreakEvent(InteractionHand.OFF_HAND));
                            target.level().playSound(null, target.blockPosition(), SoundEvents.SHIELD_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
                        }
                        target.hurt(DamagetypeRegister.causeSwordDamage(this.mob), (float) attackDamage + 6);
                    }
                    if (this.attackallticks > 40) {
                        this.cooldownTimer = 60;
                        this.attackallticks = 0;
                    }
                }
            } else {
                this.mob.stopTriggeredAnimation("HerobrineAttacktrriger", "atk");
                this.attackallticks = 0;
                this.cooldownTimer = 3;
            }
        }
    }
    public class StabAttack extends Goal {
        private final herobrine mob;
        private int cooldownTimer; // クールダウンタイマー// クールダウンの長さ
        public int attackallticks;

        public StabAttack(herobrine mob) {
            this.mob = mob;
            this.cooldownTimer = 3;
        }

        @Override
        public boolean canUse() {
            if (this.mob == null || this.mob.getTarget() == null) {
                return false; // nullの場合は実行不可
            }
            return this.mob.distanceTo(this.mob.getTarget()) < 20.0;
        }


        @Override
        public void tick() {
            this.cooldownTimer--;
            LivingEntity target = this.mob.getTarget();
            double attackDamage = Objects.requireNonNull(mob.getAttribute(Attributes.ATTACK_DAMAGE)).getValue() + Objects.requireNonNull(target).getHealth() / 4;
            if (this.mob.distanceTo(Objects.requireNonNull(target)) <= 9.0) {
                if (this.cooldownTimer <= 0) {
                    this.attackallticks++;
                    if (this.attackallticks == 1) {
                        this.mob.triggerAnim("HerobrineAttacktrriger", "atk2");
                    }
                    if (this.attackallticks == 17) {
                        ItemStack offhandItem = target.getOffhandItem();
                        ItemStack mainhanditem = target.getMainHandItem();
                        if (offhandItem.getItem() instanceof ShieldItem) {
                            target.stopUsingItem();
                            offhandItem.hurtAndBreak(5, target, (entity) -> entity.broadcastBreakEvent(InteractionHand.OFF_HAND));
                            target.level().playSound(null, target.blockPosition(), SoundEvents.SHIELD_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
                        }
                        if (mainhanditem.getItem() instanceof ShieldItem) {
                            target.stopUsingItem();
                            offhandItem.hurtAndBreak(5, target, (entity) -> entity.broadcastBreakEvent(InteractionHand.OFF_HAND));
                            target.level().playSound(null, target.blockPosition(), SoundEvents.SHIELD_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
                        }
                        target.hurt(DamagetypeRegister.causeSwordDamage(this.mob), (float) attackDamage +3);
                    }
                    if (this.attackallticks > 20) {
                        this.cooldownTimer = 40;
                        this.attackallticks = 0;
                    }
                }
            } else {
                this.mob.stopTriggeredAnimation("HerobrineAttacktrriger", "atk2");
                this.attackallticks = 0;
                this.cooldownTimer = 3;
            }
        }
    }
    public static class ShadowStrikeGoal extends Goal {
        private final herobrine mob;
        private Player targetPlayer;
        private int tickCounter = 0;

        public ShadowStrikeGoal(herobrine mob) {
            this.mob = mob;
        }

        @Override
        public boolean canUse() {
            this.targetPlayer = mob.level().getNearestPlayer(mob, 20);
            return this.targetPlayer != null;
        }

        @Override
        public void start() {
            mob.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 60));
            tickCounter = 0;
        }

        @Override
        public void tick() {
            tickCounter++;
            if (tickCounter == 200) {
                Vec3 behindPosition = targetPlayer.position().add(-2, 0, -2);
                mob.teleportTo(behindPosition.x, behindPosition.y, behindPosition.z);
                tickCounter = 0;
            }
        }
    }
}
