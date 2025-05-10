package com.maxwell.MiniBosses.entity.custom;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class CustomPhantom extends Phantom implements GeoEntity {
    public CustomPhantom(EntityType<? extends Phantom> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
    }
    @Override
    public boolean hurt(DamageSource source, float amount)
    {
        return false;
    }
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0f)
                .add(Attributes.MOVEMENT_SPEED, 0F) // 移動速度
                .add(Attributes.ATTACK_DAMAGE, 0) // 攻撃力
                .add(Attributes.MAX_HEALTH, 20) // 最大HP
                .add(Attributes.KNOCKBACK_RESISTANCE, 9999); // ノックバック耐性
    }
}
