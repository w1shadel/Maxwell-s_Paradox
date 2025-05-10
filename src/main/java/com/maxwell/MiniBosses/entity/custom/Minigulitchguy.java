package com.maxwell.MiniBosses.entity.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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

public class Minigulitchguy extends Vindicator implements GeoEntity {
    public int attackType;
    protected static final RawAnimation DEAD = RawAnimation.begin().thenLoop("dead");
    protected static final RawAnimation SUMMONING = RawAnimation.begin().thenLoop("summon");

    public Minigulitchguy(EntityType<? extends Vindicator> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "AttackSelector", 4, this::unifiedAttackController)
                .triggerableAnim("dead", DEAD)
                .triggerableAnim("summon", SUMMONING));

    }
    private float customScale = 0.5F;
    public float getCustomScale() {
        return this.customScale;
    }
    public void setCustomScale(float scale) {
        this.customScale = scale;
    }
    protected <E extends Minigulitchguy> PlayState unifiedAttackController(final AnimationState<E> event) {
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
    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 1f)
                .add(Attributes.MOVEMENT_SPEED, 0.6F)
                .add(Attributes.ATTACK_DAMAGE, 8)
                .add(Attributes.ARMOR,2)
                .add(Attributes.MAX_HEALTH, 5)
                .add(Attributes.KNOCKBACK_RESISTANCE, 2);
    }
}
