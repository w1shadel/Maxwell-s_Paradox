package com.maxwell.MiniBosses.entity.custom;

import com.maxwell.MiniBosses.DamagetypeRegister;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Objects;

public class notch extends Mob implements GeoEntity {
    public notch(EntityType<? extends Mob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    protected static final RawAnimation MOVE = RawAnimation.begin().thenLoop("move");
    protected static final RawAnimation ATK2 = RawAnimation.begin().thenPlay("atk2");
    protected static final RawAnimation ATK3 = RawAnimation.begin().thenPlay("atk3");
    protected static final RawAnimation ATK4 = RawAnimation.begin().thenPlay("atk4");

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Movecontroler", 0, this::movecontroler));
        controllers.add(new AnimationController<>(this, "NotchAttacktrriger", 0, this::AttackController)
                .triggerableAnim("atk2", ATK2)
                .triggerableAnim("atk3", ATK3)
                .triggerableAnim("atk4", ATK4));
    }

    protected <E extends notch> PlayState movecontroler(final AnimationState<E> event) {

        if (event.isMoving()) {
            return event.setAndContinue(MOVE);
        }
        return PlayState.STOP;
    }

    public int attackType;
public boolean hurt(@NotNull DamageSource source, float amount)
{
    Entity attacker = source.getEntity();
    if (attacker instanceof LivingEntity) {
        double distance = this.distanceTo(attacker);

        if (distance > 10.0) { // 10ブロック以上ならダメージ無効
            return false;
        }
    }
    return super.hurt(source,amount);
}
    protected <E extends notch> PlayState AttackController(final AnimationState<E> event) {
        return switch (attackType) {
            case 1 -> event.setAndContinue(ATK4); // Swing Attack
            case 2 -> event.setAndContinue(ATK2); // Throw Attack
            case 3 -> event.setAndContinue(ATK3); // Flying Attack
            default -> PlayState.STOP; // アニメーションを停止
        };
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0)
                .add(Attributes.MOVEMENT_SPEED, 1)
                .add(Attributes.ATTACK_DAMAGE, 12)
                .add(Attributes.ARMOR,9999)
                .add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0);
    }
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(1, new TargetSpecificEnemyGoal(this, herobrine.class));
        this.goalSelector.addGoal(1, new SwingSwordAttack(this));
        this.goalSelector.addGoal(1, new FlyingAttackGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static class TargetSpecificEnemyGoal extends TargetGoal {
        private final notch mob;
        private final Class<? extends LivingEntity> targetClass;
        private LivingEntity target;

        public TargetSpecificEnemyGoal(notch mob, Class<? extends LivingEntity> targetClass) {
            super(mob, false);
            this.mob = mob;
            this.targetClass = targetClass;
        }

        @Override
        public boolean canUse() {
            // 指定の敵を検索（半径15ブロック以内）
            List<LivingEntity> enemies = (List<LivingEntity>) this.mob.level().getEntitiesOfClass(targetClass, this.mob.getBoundingBox().inflate(15));
            if (!enemies.isEmpty()) {
                this.target = enemies.get(0); // 最も近いターゲットを選択
                return true;
            }
            return false;
        }

        @Override
        public void start() {
            this.mob.setTarget(target);
            super.start();
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
            if (this.target != null && this.target.isAlive() && this.mob.distanceTo(this.target) <= 10) {
                this.mob.getNavigation().moveTo(this.target, 0.4);
                this.mob.lookAt(this.target, 30.0F, 30.0F); // ターゲットを注視
            } else {
                // ターゲットが範囲外に出た場合停止
                stop();
            }
        }
    }
    public class SwingSwordAttack extends Goal {
        private final notch mob;
        private int cooldownTimer;
        public int attackallticks;

        public SwingSwordAttack(notch mob) {
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
            herobrine target = (herobrine) this.mob.getTarget();
            double attackDamage = Objects.requireNonNull(mob.getAttribute(Attributes.ATTACK_DAMAGE)).getValue() + Objects.requireNonNull(target).getHealth() / 4;
            if (this.mob.distanceTo(Objects.requireNonNull(target)) <= 9.0) {
                if (this.cooldownTimer <= 0 && !Isflyingattack) {
                    this.attackallticks++;
                    if (this.attackallticks == 1) {
                        this.mob.triggerAnim("NotchAttacktrriger", "atk2");
                    }
                    if (this.attackallticks == 8) {
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
                        target.hurt(DamagetypeRegister.causeSwordDamage(this.mob), (float) attackDamage);
                    }
                    if (this.attackallticks == 13) {
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
                        target.hurt(DamagetypeRegister.causeSwordDamage(this.mob), (float) attackDamage);
                    }
                    if (this.attackallticks > 28) {
                        this.cooldownTimer = 12;
                        this.attackallticks = 0;
                    }
                }
            } else {
                this.mob.stopTriggeredAnimation("NotchAttacktrriger", "atk2");
                this.attackallticks = 0;
                this.cooldownTimer = 3;
            }
        }
    }
    public class FlyingAttackGoal extends Goal {
        private final notch mob;
        private int cooldownTimer;
        public int attackallticks;

        public FlyingAttackGoal(notch mob) {
            this.mob = mob;
            this.cooldownTimer = 3;
        }
        @Override
        public boolean canUse() {
            if (this.mob == null || this.mob.getTarget() == null) {
                return false; // nullの場合は実行不可
            }
            return this.mob.distanceTo(this.mob.getTarget()) < 30;
        }
        private void teleportBehindPlayer() {
            herobrine target = (herobrine) this.mob.getTarget();
            if (target != null) {
                double angle = Math.toRadians(target.getYRot()); // 向きの角度
                double dx = -Math.sin(angle) * 2.0;
                double dz = Math.cos(angle) * 2.0;

                double newX = target.getX() + dx;
                double newY = target.getY();
                double newZ = target.getZ() + dz;

                mob.teleportTo(newX, newY, newZ);
            }
        }
        @Override
        public void tick() {
            this.cooldownTimer--;
            herobrine target = (herobrine) this.mob.getTarget();
            double attackDamage = Objects.requireNonNull(mob.getAttribute(Attributes.ATTACK_DAMAGE)).getValue() + Objects.requireNonNull(target).getHealth() / 4;
            if (this.mob.distanceTo(Objects.requireNonNull(target)) <= 30) {
                if (this.cooldownTimer <= 0) {
                    Isflyingattack = true;
                    this.attackallticks++;
                    if (this.attackallticks == 1) {
                        this.mob.triggerAnim("NotchAttacktrriger", "atk4");
                        this.mob.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY,24,255,true,true));
                    }
                    if (this.attackallticks == 26) {
                        ItemStack offhandItem = target.getOffhandItem();
                        ItemStack mainhanditem = target.getMainHandItem();
                        if (offhandItem.getItem() instanceof ShieldItem) {
                            target.stopUsingItem();
                            offhandItem.hurtAndBreak(5, target, (entity) -> entity.broadcastBreakEvent(InteractionHand.OFF_HAND));
                            target.level().playSound(null, target.blockPosition(), SoundEvents.SHIELD_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
                            teleportBehindPlayer();
                        }
                        if (mainhanditem.getItem() instanceof ShieldItem) {
                            target.stopUsingItem();
                            offhandItem.hurtAndBreak(5, target, (entity) -> entity.broadcastBreakEvent(InteractionHand.OFF_HAND));
                            target.level().playSound(null, target.blockPosition(), SoundEvents.SHIELD_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
                        }
                        target.hurt(DamagetypeRegister.causeSwordDamage(this.mob), (float) attackDamage + 30);
                    }
                    if (this.attackallticks > 40) {
                        this.cooldownTimer = 40;
                        this.attackallticks = 0;
                        Isflyingattack = false;
                    }
                }
            } else {
                this.mob.stopTriggeredAnimation("NotchAttacktrriger", "atk4");
                this.attackallticks = 0;
                this.cooldownTimer = 3;
                Isflyingattack = false;
            }

        }

    }
    public boolean Isflyingattack;
}
