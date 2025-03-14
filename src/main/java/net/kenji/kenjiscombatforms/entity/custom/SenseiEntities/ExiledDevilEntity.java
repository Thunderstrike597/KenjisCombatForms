package net.kenji.kenjiscombatforms.entity.custom.SenseiEntities;

import net.kenji.kenjiscombatforms.config.KenjisCombatFormsCommon;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ExiledDevilEntity extends PathfinderMob {

    public ExiledDevilEntity(EntityType<? extends PathfinderMob> p_34368_, Level p_34369_) {
        super(p_34368_, p_34369_);
    }

    boolean hasBeenDamaged = false;
    private float lastHealth;

    void damageDetect(){
        float currentHealth = this.getHealth();
        if (currentHealth < lastHealth) {
            hasBeenDamaged = true;
        }
        lastHealth = currentHealth;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("hasBeenDamaged", hasBeenDamaged);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        hasBeenDamaged = compound.getBoolean("hasBeenDamaged");
    }

    private static boolean isCompatDifficulty(){
        return KenjisCombatFormsCommon.DIFFICULTY_COMPAT_MODE.get();
    }

    @Override
    public void onAddedToWorld() {
        if(!isCompatDifficulty()) {

            Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(KenjisCombatFormsCommon.EXILED_DEVIL_HEALTH.get());

            if (!hasBeenDamaged) {
                setHealth(this.getMaxHealth());
            }
        }else {
            Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(KenjisCombatFormsCommon.COMPAT_MODE_EXILED_DEVIL_HEALTH.get());

            if (!hasBeenDamaged) {
                setHealth(this.getMaxHealth());
            }
        }
    }

    public static final Random RANDOM = new Random();

    @Override
    public void setLastHurtByMob(@Nullable LivingEntity entity) {
        double chance = RANDOM.nextDouble();
        if (entity != null) {
            if (chance < 0.15) {
                entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 45, 0, true, true));
                entity.addEffect(new MobEffectInstance(MobEffects.POISON, 55, 0, true, true));
            }
        }
        super.setLastHurtByMob(entity);
    }

    @Override
    public void setLastHurtMob(@Nullable Entity entity) {
        double chance = RANDOM.nextDouble();
        LivingEntity lEntity = (LivingEntity) entity;
        if (lEntity != null) {
            if (chance > 0.25 && chance < 0.35) {
                entity.setSecondsOnFire(4);
            } else if (chance < 0.25) {
                lEntity.addEffect(new MobEffectInstance(MobEffects.WITHER, 80, 0, true, true));
                lEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 160, 0, true, true));
            } else {
                entity.setSecondsOnFire(0);
            }
        }
        assert entity != null;
        super.setLastHurtMob(entity);
    }

    @Override
    public boolean canHoldItem(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.PILLAGER_AMBIENT;
    }

    @Override
    public SoundEvent getHurtSound(@NotNull DamageSource p_34327_) {
        return SoundEvents.PILLAGER_HURT;
    }

    @Override
    public SoundEvent getDeathSound() {
        return SoundEvents.PILLAGER_DEATH;
    }

    @Override
    public float getSpeed() {
        if (getTarget() != null) {
            return 0.13f;
        }
        return 0.1f;

    }

    @Override
    protected boolean isSunBurnTick() {
        return false;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.addBehaviourGoals();
    }

    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(1, new RandomStrollGoal(this, 1));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        if(this.getLastAttacker() != null) {
            this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, getLastAttacker().getClass(), true));
        }
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));

    }

    @Override
    public void setItemInHand(@NotNull InteractionHand p_21009_, @NotNull ItemStack p_21010_) {
        super.setItemInHand(p_21009_, p_21010_);
    }

    @Override
    public void tick() {
        super.tick();
        damageDetect();
    }

    @Override
    public boolean isEffectiveAi() {
        return super.isEffectiveAi();
    }

    @Override
    public boolean isAlwaysTicking() {
        return super.isAlwaysTicking();
    }


    public static boolean checkSpawnRules(EntityType<? extends ExiledDevilEntity> entityType, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        double chance = RANDOM.nextDouble();
        if (level.dimensionType().ultraWarm() && level.getDifficulty() != Difficulty.PEACEFUL) {
            if (level.getBlockState(pos.below()).isSolid() && level.getBlockState(pos).isAir() && level.getBlockState(pos.above()).isAir()) {
                // Check for nearby entities of the same type
                int searchRadius = KenjisCombatFormsCommon.EXILED_DEVIL_SPAWNDIST.get(); // Adjust this value to change the minimum distance between spawns
                List<ExiledDevilEntity> nearbyEntities = level.getEntitiesOfClass(ExiledDevilEntity.class,
                        new AABB(pos).inflate(searchRadius),
                        e -> true);

                if(chance < KenjisCombatFormsCommon.EXILED_DEVIL_SPAWN_CHANCE.get()) {
                    if (nearbyEntities.isEmpty()) {
                        return true; // Allow spawn if no nearby entities found
                    }
                }
            }
        }
        return false;
    }
}