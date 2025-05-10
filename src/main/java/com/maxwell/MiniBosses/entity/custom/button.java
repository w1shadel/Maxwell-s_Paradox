package com.maxwell.MiniBosses.entity.custom;

import com.maxwell.MiniBosses.register.ModEntities;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class button extends Monster implements GeoEntity {
    public button(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

    }
    public int isAlive;
    public int getValue() { // ゲッターを通じて値を取得
        return isAlive;
    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0)
                .add(Attributes.MOVEMENT_SPEED, 0)
                .add(Attributes.ATTACK_DAMAGE, 0)
                .add(Attributes.MAX_HEALTH, 200)
                .add(Attributes.ARMOR, 10)
                .add(Attributes.KNOCKBACK_RESISTANCE, 9999);
    }
    private float customScale = 1.3F;
    public float getCustomScale() {
        return this.customScale;
    }
    public void setCustomScale(float scale) {
        this.customScale = scale;
    }
    @Override
    public void tick() {
        super.tick();
        isAlive++;
        List<LivingEntity> entities = this.level().getEntitiesOfClass(
                LivingEntity.class, this.getBoundingBox().inflate(200) // 範囲を指定
        );
        LivingEntity targetEntity = entities.stream()
                .filter(entity -> entity.getType().equals(ModEntities.BOUNDINGBOX.get()))
                .findFirst()
                .orElse(null);
        if (targetEntity != null) {
            double offsetX = 3;
            double offsetY = 1.0;
            double offsetZ = 1.0;
            this.setPos(targetEntity.getX() + offsetX, targetEntity.getY() + offsetY, targetEntity.getZ() + offsetZ);
        }
        this.addEffect(new MobEffectInstance(MobEffects.GLOWING,100,1));
    }

}



