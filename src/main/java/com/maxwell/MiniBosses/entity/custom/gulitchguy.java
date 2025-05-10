package com.maxwell.MiniBosses.entity.custom;

import com.maxwell.MiniBosses.register.ItemRegister_Maxwell;
import com.maxwell.MiniBosses.register.ModEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
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

import java.util.Objects;

public class gulitchguy extends Monster implements GeoEntity {
    public int attackType;
    public gulitchguy(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    protected static final RawAnimation DEAD = RawAnimation.begin().thenLoop("dead");
    protected static final RawAnimation SUMMONING = RawAnimation.begin().thenLoop("summon");
    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "GlitchAttacktrriger", 0, this::unifiedAttackController)
                .triggerableAnim("dead", DEAD)
                .triggerableAnim("summon", SUMMONING));
    }
    @Override
    public void die(@NotNull DamageSource source)
    {
        this.spawnAtLocation(ItemRegister_Maxwell.NULLSPEC_INGOT.get());
        this.spawnAtLocation(ItemRegister_Maxwell.NULLSPEC_INGOT.get());
        if (Math.random() < 0.15) { // ✅ 15%の確率で追加ドロップ
            this.spawnAtLocation(ItemRegister_Maxwell.NULLSPEC_INGOT.get());
            this.spawnAtLocation(ItemRegister_Maxwell.NULLSPEC_INGOT.get());
            this.spawnAtLocation(ItemRegister_Maxwell.FALLINGSTAREVENTITEM.get());
        }
    }
    @Override
    protected void tickDeath() {
        this.setYRot(this.yBodyRot);
        this.triggerAnim("GlitchAttacktrriger", "dead");
        if (this.deathTime < 20) {
            this.deathTime++;
        } else {
            this.remove(RemovalReason.KILLED);
        }
    }
    protected <E extends gulitchguy> PlayState unifiedAttackController(final AnimationState<E> event) {
        return switch (attackType) {
            case 1 -> event.setAndContinue(DEAD);
            case 2 -> event.setAndContinue(SUMMONING);
            default -> PlayState.STOP;
        };
    }
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.0f)
                .add(Attributes.ATTACK_DAMAGE, 5)
                .add(Attributes.ARMOR,20)
                .add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.KNOCKBACK_RESISTANCE, 2);
    }
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SelfReconstructionGoal(this));
        this.goalSelector.addGoal(1, new BugSwarmGoal(this));
        this.goalSelector.addGoal(1, new SpatialDisruptionGoal(this));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.9F));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }
    public static class SpatialDisruptionGoal extends Goal {
        private final LivingEntity entity; // Glitchエンティティ
        private int cooldownTimer = 0; // クールダウンタイマー
        private final double range = 30.0; // ランダムワープの範囲

        public SpatialDisruptionGoal(LivingEntity entity) {
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
            cooldownTimer = 200;
            if (!(entity.getCommandSenderWorld() instanceof ServerLevel serverLevel)) return;
            serverLevel.playSound(null, entity.blockPosition(), SoundEvents.ENDER_PEARL_THROW, SoundSource.HOSTILE, 1.0f, 0.8f);
            serverLevel.sendParticles(ParticleTypes.PORTAL, entity.getX(), entity.getY(), entity.getZ(), 50, 1.0, 1.0, 1.0, 0.5);
            serverLevel.getEntities(entity, entity.getBoundingBox().inflate(range)).forEach(e -> {
                if (e instanceof Player target) {
                    double randomX = target.getX() + (serverLevel.random.nextDouble() * 20 - 10);
                    double randomY = target.getY() + (serverLevel.random.nextDouble() * 10 - 5);
                    double randomZ = target.getZ() + (serverLevel.random.nextDouble() * 20 - 10);

                    target.teleportTo(randomX, randomY, randomZ); // ランダムワープ
                    serverLevel.playSound(null, target.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.HOSTILE, 1.0f, 1.0f);
                }
            });
        }
        public void tick()
        {
            if (cooldownTimer > 0) {
                cooldownTimer--;
            }
            else
            {
                this.start();
            }
        }
    }
    public static class SelfReconstructionGoal extends Goal {
        private final LivingEntity entity; // Glitchエンティティ
        private int cooldownTicks = 100; // クールダウン30秒
        public SelfReconstructionGoal(LivingEntity entity) {
            this.entity = entity;
        }

        @Override
        public boolean canUse() {
            return true;
        }

        public void tick()
        {
            if (cooldownTicks > 0) {
                cooldownTicks--;
            }
            else
            {
                if (entity.getCommandSenderWorld() instanceof ServerLevel serverLevel) {
                    serverLevel.playSound(null, entity.blockPosition(), SoundEvents.GLASS_BREAK, SoundSource.HOSTILE, 1.5f, 1.0f);
                    serverLevel.sendParticles(ParticleTypes.SMOKE, entity.getX(), entity.getY(), entity.getZ(), 100, 1.5, 1.5, 1.5, 0.3);
                    entity.setHealth(entity.getHealth() + 4.0f); // HP回復
                    serverLevel.sendParticles(ParticleTypes.HEART, entity.getX(), entity.getY() + 1, entity.getZ(), 10, 0.5, 0.5, 0.5, 0.2);
                }
                cooldownTicks = 100;
            }
        }
    }
    public static class BugSwarmGoal extends Goal {
        private final gulitchguy mob;
        private int cooldownTimer = 100; // クールダウンタイマー

        public BugSwarmGoal(gulitchguy mob) {
            this.mob = mob;
        }
        @Override
        public boolean canUse() {
            return true;
        }
        @Override
        public void start() {
            this.mob.triggerAnim("GlitchAttacktrriger", "summon");
            cooldownTimer = 200;
            if (!(mob.getCommandSenderWorld() instanceof ServerLevel serverLevel)) return;
            serverLevel.playSound(null, mob.blockPosition(), SoundEvents.BEE_HURT, SoundSource.HOSTILE, 1.0f, 0.6f);
            serverLevel.sendParticles(ParticleTypes.SONIC_BOOM, mob.getX(), mob.getY() + 1, mob.getZ(), 30, 1.0, 1.0, 1.0, 0.2);
            for (int i = 0; i < 5; i++) {
                ModEntities.MINIGLITHGUY.get().spawn(serverLevel, null, (Player) null, mob.blockPosition().offset(1, 0, i),
                        MobSpawnType.EVENT, true, false);
            }
                double randomX = this.mob.getX() + (serverLevel.random.nextDouble() * 10 - 5);
                double randomY = this.mob.getY() + (serverLevel.random.nextDouble() * 2);
                double randomZ = this.mob.getZ() + (serverLevel.random.nextDouble() * 10 - 5);
                this.mob.teleportTo(randomX, randomY, randomZ);
                serverLevel.playSound(null, mob.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.HOSTILE, 3.0f, 1.0f);
            cooldownTimer = 100;
        }
        public void tick()
        {
            if (cooldownTimer > 0) {
                cooldownTimer--;
                this.mob.stopTriggeredAnimation("GlitchAttacktrriger", "summon");
            }
            else
            {
                this.start();
            }
        }
    }

}
