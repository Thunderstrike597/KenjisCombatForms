package net.kenji.kenjiscombatforms.entity.custom.SenseiEntities;

import net.kenji.kenjiscombatforms.config.KenjisCombatFormsCommon;
import net.kenji.kenjiscombatforms.event.LootTableModifier;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class UndeadSenseiEntity extends Monster implements Enemy {

    public UndeadSenseiEntity(EntityType<? extends Monster> p_34368_, Level p_34369_) {
        super(p_34368_, p_34369_);

    }

    public double chance;
    public double chance2;


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
    public void addAdditionalSaveData(CompoundTag compound) {
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
           Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(KenjisCombatFormsCommon.UNDEAD_SENSEI_HEALTH.get());
           if (!hasBeenDamaged) {
               setHealth(this.getMaxHealth());
           }
       } else {
           Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(KenjisCombatFormsCommon.COMPAT_MODE_UNDEAD_SENSEI_HEALTH.get());
           if (!hasBeenDamaged) {
               setHealth(this.getMaxHealth());
           }
       }
    }


    @Override
    protected boolean isSunBurnTick() {
        return true;
    }

    @Override
    public float getSpeed() {
        return 0.1f;
    }

    private static final Random RANDOM = new Random();

    @Override
    public boolean canPickUpLoot() {
        return false;
    }

    @Override
    public boolean canTakeItem(ItemStack p_21522_) {
        return false;
    }


    @Override
    public void setLastHurtByMob(@Nullable LivingEntity entity) {
        double chance = RANDOM.nextDouble();

        if (entity != null) {
            if (chance < 0.25) {
                entity.addEffect(new MobEffectInstance(MobEffects.HUNGER, 180, 0, true, true));
            }
        }
        super.setLastHurtByMob(entity);
    }

    @Override
    public void setLastHurtMob(@Nullable Entity entity) {
        double chance = RANDOM.nextDouble();

        LivingEntity lEntity = (LivingEntity) entity;
        if(entity != null){
            if (chance < 0.15) {
                lEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 180, 0, true, true));
            }
        }
        assert entity != null;
        super.setLastHurtMob(entity);
    }

    public void deathLootChanceRolls(){
        if(this.getHealth() < 5) {
            chance = RANDOM.nextDouble();
            chance2 = RANDOM.nextDouble();
        }
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
        }else {
            this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        }

            this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, ExiledSenseiEntity.class, true));
    }


    @Override
    public void tick() {
        super.tick();
        damageDetect();
        deathLootChanceRolls();
    }

    @Override
    public boolean isEffectiveAi() {
        return super.isEffectiveAi();
    }

    @Override
    public boolean isAlwaysTicking() {
        return super.isAlwaysTicking();
    }

    public static boolean checkSpawnRules(EntityType<? extends UndeadSenseiEntity> entityType, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        double chance = RANDOM.nextDouble();
        if (level instanceof ServerLevel && ((ServerLevel)level).dimension() == Level.OVERWORLD && level.getDifficulty() != Difficulty.PEACEFUL) {
            if (level.getBlockState(pos.below()).isSolid() && level.getBlockState(pos).isAir() && level.getBlockState(pos.above()).isAir() && level.getBrightness(LightLayer.BLOCK, pos) < 8){
    if(level.getBrightness(LightLayer.SKY, pos) < 6){
        return false;
    }

                // Check for nearby entities of the same type
                int searchRadius = KenjisCombatFormsCommon.UNDEAD_SENSEI_SPAWNDIST.get(); // Adjust this value to change the minimum distance between spawns
                List<UndeadSenseiEntity> nearbyEntities = level.getEntitiesOfClass(UndeadSenseiEntity.class,
                        new AABB(pos).inflate(searchRadius),
                        e -> true);
                if(chance < KenjisCombatFormsCommon.UNDEAD_SENSEI_SPAWN_CHANCE.get()) {
                    if (nearbyEntities.isEmpty()) {
                        return true; // Allow spawn if no nearby entities found
                    }
                }
            }
        }
        return false;
    }



}
