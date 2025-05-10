package com.maxwell.MiniBosses.entity.custom;

import com.maxwell.MiniBosses.DamagetypeRegister;
import com.maxwell.MiniBosses.event.BossInfoServer;
import com.maxwell.MiniBosses.register.ItemRegister_Maxwell;
import com.maxwell.MiniBosses.register.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.security.PublicKey;
import java.util.Objects;
import java.util.Random;

public class PhantomKing extends Monster implements GeoEntity {
    public PhantomKing(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    protected static final RawAnimation SUMMON = RawAnimation.begin().thenLoop("summon");
    protected static final RawAnimation ATK1 = RawAnimation.begin().thenPlay("atk1");
    protected static final RawAnimation ATK2 = RawAnimation.begin().thenPlay("atk2");
    protected static final RawAnimation ATK3 = RawAnimation.begin().thenPlay("atk3");
    protected static final RawAnimation ATK4 = RawAnimation.begin().thenPlay("atk4");
    protected static final RawAnimation LASER = RawAnimation.begin().thenPlay("laser");
    protected static final RawAnimation ONDEAD = RawAnimation.begin().thenPlay("Ondead");
    private final BossInfoServer bossEvent2 = new BossInfoServer(this.getDisplayName(), BossEvent.BossBarColor.PURPLE, true, 11);
    public void stopSeenByPlayer(@NotNull ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent2.removePlayer(player);
    }

    public void startSeenByPlayer(@NotNull ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent2.addPlayer(player);
    }
    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Attacktrriger", 0, this::AttackController)
                .triggerableAnim("atk1", ATK1)
                .triggerableAnim("atk2", ATK2)
                .triggerableAnim("atk3", ATK3)
                .triggerableAnim("summon", SUMMON)
                .triggerableAnim("laser", LASER)
                .triggerableAnim("Ondead", ONDEAD)
                .triggerableAnim("atk4", ATK4));
    }

    protected <E extends PhantomKing> PlayState AttackController(final AnimationState<E> event) {
        return switch (attackType) {
            case 1 -> event.setAndContinue(ATK4); // Swing Attack
            default -> PlayState.STOP; // アニメーションを停止
        };
    }
    public int attackTimer;
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3, new SwordCombo(this));
        this.goalSelector.addGoal(1, new Sword3Combo(this));
        this.goalSelector.addGoal(2, new ChaseTargetGoal(this,0.5f,50));
        this.goalSelector.addGoal(2, new Sword2Combo(this));
        this.goalSelector.addGoal(1, new SummonGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }
    public int attackType;
public boolean Isdead = false;
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (this.getHealth() <= 250) {
            Isdead = true;
            this.goalSelector.addGoal(1, new DEAD(this));
            this.triggerAnim("Attacktrriger", "Ondead");
            return false;
        }
        Entity attacker = source.getEntity();
        if (attacker instanceof LivingEntity) {
            double distance = this.distanceTo(attacker);
            if (distance > 10.0) {
                return false;
            }
        }
        return super.hurt(source, amount);
    }

    @Override
    public void tick() {
        super.tick();
        attackTimer++;

        if (attackTimer >= 80) { // ✅ 40tickごとに攻撃を変更
            attackType = random.nextInt(3) + 1; // ✅ 1～3の範囲でランダム選択
            attackTimer = 0; // ✅ タイマーをリセット
            if (Isdead)
            {
                attackTimer = 1;
                attackType = 666;
            }
        }
    }
    public static class ChaseTargetGoal extends Goal {
        private final PhantomKing mob;
        private LivingEntity target;
        private final double speedModifier; // 追尾速度
        private final float chaseRange;    // 追尾範囲

        public ChaseTargetGoal(PhantomKing mob, double speedModifier, float chaseRange) {
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

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0)
                .add(Attributes.MOVEMENT_SPEED, 1)
                .add(Attributes.ATTACK_DAMAGE, 0)
                .add(Attributes.MAX_HEALTH, 450)
                .add(Attributes.ARMOR, 30)
                .add(Attributes.KNOCKBACK_RESISTANCE, 9999);
    }

    public static class DEAD extends Goal {
        private final PhantomKing mob;
        public int attackallticks;
        private Player target;

        public DEAD(PhantomKing mob) {
            this.mob = mob;
        }

        @Override
        public boolean canUse() {
            return true;
        }

        @Override
        public void tick() {
            this.attackallticks++;
            if (this.attackallticks == 32) {
                this.mob.discard();
                this.mob.spawnAtLocation(ItemRegister_Maxwell.PHANTOM_INGOT.get());
                this.mob.spawnAtLocation(ItemRegister_Maxwell.PHANTOM_INGOT.get());
                this.mob.spawnAtLocation(ItemRegister_Maxwell.PHANTOM_INGOT.get());
                this.mob.spawnAtLocation(ItemRegister_Maxwell.PHANTOM_INGOT.get());
                this.mob.spawnAtLocation(ItemRegister_Maxwell.FALLINGSTAREVENTITEM.get());
            }
        }
    }

    public class SwordCombo extends Goal {
        private final PhantomKing mob;
        public int attackallticks;
        public int cooldown =12;
        private Player target;
        public SwordCombo(PhantomKing mob) {
            this.mob = mob;
        }

        @Override
        public boolean canUse() {
            this.target = mob.level().getNearestPlayer(mob, 20);
            return this.target != null;
        }

        @Override
        public void tick() {
            if (cooldown > 0 && !Isdead && attackTimer ==1)
            {
                cooldown--;
            }
            if (cooldown == 0)
            {

            }
            else {
                this.attackallticks++;
                if (this.attackallticks == 0 && this.mob.distanceTo(Objects.requireNonNull(target)) <= 4) {
                    this.mob.triggerAnim("Attacktrriger", "atk1");
                }
                if (this.attackallticks == 34 && this.mob.distanceTo(Objects.requireNonNull(target)) <= 4)
                {
                    Objects.requireNonNull(target).hurt(DamagetypeRegister.causeNotchDamage(this.mob), 4);
                    this.mob.triggerAnim("Attacktrriger", "atk2");
                }
                if (this.attackallticks == 46 && this.mob.distanceTo(Objects.requireNonNull(target)) <= 7)
                {
                    Objects.requireNonNull(target).hurt(DamagetypeRegister.causeNotchDamage(this.mob), 5);
                }
                if (attackallticks == 47)
                {
                    this.attackallticks = 0;
                    this.cooldown = 12;
                }
            }
        }
    }
    public class Sword2Combo extends Goal {
        private final PhantomKing mob;
        public int attackallticks;
        public int cooldown =24;
        private Player target;
        public Sword2Combo(PhantomKing mob) {
            this.mob = mob;
        }

        @Override
        public boolean canUse() {
            this.target = mob.level().getNearestPlayer(mob, 20);
            return this.target != null;
        }

        @Override
        public void tick() {
            if (cooldown > 0 && !Isdead&& attackTimer ==2)
            {
                System.out.println(cooldown);
                cooldown--;
            }
            else {
                this.attackallticks++;
                if (this.attackallticks == 1 && this.mob.distanceTo(Objects.requireNonNull(target)) <= 4) {
                    this.mob.triggerAnim("Attacktrriger", "atk1");
                }
                if (this.attackallticks == 35 && this.mob.distanceTo(Objects.requireNonNull(target)) <= 4)
                {
                    Objects.requireNonNull(target).hurt(DamagetypeRegister.causeNotchDamage(this.mob), 4);
                    this.mob.triggerAnim("Attacktrriger", "atk4");
                }
                if (this.attackallticks == 46 && this.mob.distanceTo(Objects.requireNonNull(target)) <= 4)
                {
                    Objects.requireNonNull(target).hurt(DamagetypeRegister.causeNotchDamage(this.mob), 7);
                    this.mob.triggerAnim("Attacktrriger", "atk2");
                }
                if (this.attackallticks == 52 && this.mob.distanceTo(Objects.requireNonNull(target)) <= 4)
                {
                    Objects.requireNonNull(target).hurt(DamagetypeRegister.causeNotchDamage(this.mob), 5);
                }
                if (attackallticks == 54)
                {
                    this.attackallticks = 0;
                    this.cooldown = 14;
                }
            }
        }
    }
    public class Sword3Combo extends Goal {
        private final PhantomKing mob;
        public int attackallticks;
        public int cooldown = 24;
        private Player target;
        public Sword3Combo(PhantomKing mob) {
            this.mob = mob;
        }

        @Override
        public boolean canUse() {
            this.target = mob.level().getNearestPlayer(mob, 20);
            return this.target != null;
        }

        @Override
        public void tick() {
            if (cooldown > 0 && !Isdead&& attackTimer ==3)
            {
                cooldown--;
            }
            else {
                this.attackallticks++;
                if (this.attackallticks == 0 && this.mob.distanceTo(Objects.requireNonNull(target)) <= 4) {
                    this.mob.triggerAnim("Attacktrriger", "atk4");
                }
                if (this.attackallticks == 6 && this.mob.distanceTo(Objects.requireNonNull(target)) <= 4)
                {
                    Objects.requireNonNull(target).hurt(DamagetypeRegister.causeNotchDamage(this.mob), 4);
                    this.mob.triggerAnim("Attacktrriger", "atk2");
                }
                if (this.attackallticks == 11 && this.mob.distanceTo(Objects.requireNonNull(target)) <= 4)
                {
                    Objects.requireNonNull(target).hurt(DamagetypeRegister.causeNotchDamage(this.mob), 4);
                    this.mob.triggerAnim("Attacktrriger", "atk3");
                }
                if (this.attackallticks == 15 && this.mob.distanceTo(Objects.requireNonNull(target)) <= 4)
                {
                    Objects.requireNonNull(target).hurt(DamagetypeRegister.causeNotchDamage(this.mob), 7);
                }
                if (this.attackallticks == 25 && this.mob.distanceTo(Objects.requireNonNull(target)) <= 4)
                {
                    Objects.requireNonNull(target).hurt(DamagetypeRegister.causeNotchDamage(this.mob), 9);
                }
                if (attackallticks == 47)
                {
                    this.attackallticks = 0;
                    this.cooldown = 12;
                }
                ;
            }
        }
    }
    public class SummonGoal extends Goal {
        private final PhantomKing mob;
        public int attackallticks;
        private Player target;

        public SummonGoal(PhantomKing mob) {
            this.mob = mob;
        }

        @Override
        public boolean canUse() {
            this.target = mob.level().getNearestPlayer(mob, 20);
            return this.target != null;
        }
        @Override
        public void tick() {
            attackallticks++;
            if (attackallticks == 1)
            {
                BlockPos mobPosition = mob.blockPosition();
                Direction facingDirection = mob.getDirection();
                BlockPos summonPosition = mobPosition.relative(facingDirection, 0);
                EntityType.PHANTOM.spawn((ServerLevel) level(), summonPosition, MobSpawnType.TRIGGERED);
            }
            if (this.attackallticks == 200) {
                attackallticks = 0;
            }
        }
    }
}
