package net.kenji.kenjiscombatforms.entity.custom.noAiEntities;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WitherMinionEntity extends TamableAnimal {


    public WitherMinionEntity(EntityType<? extends TamableAnimal> p_27403_, Level p_27404_) {
        super(p_27403_, p_27404_);
    }

    @Override
    public void onAddedToWorld() {
        this.setOpacity(0.7F);
        super.onAddedToWorld();
    }


    private static final EntityDataAccessor<Float> DATA_OPACITY = SynchedEntityData.defineId(WitherMinionEntity.class, EntityDataSerializers.FLOAT);


    @Override
    public float getSpeed() {
        if (getTarget() != null) {
            return 0.05f;
        }
        return 0.2f;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_OPACITY, 1.0F);
    }

    public void setOpacity(float opacity) {
        this.entityData.set(DATA_OPACITY, Mth.clamp(opacity, 0.0F, 1.0F));
    }

    public float getOpacity() {
        return this.entityData.get(DATA_OPACITY);
    }

    @Override
    public void tick() {
        super.tick();
        followOwnerGoal();
        minionGoals();
    }

    @Override
    public boolean isEffectiveAi() {
        return super.isEffectiveAi();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }

    private boolean isValidTarget(LivingEntity target) {
        if (target != this.getOwner() || target.getTeam() != this.getTeam()) return true;
        if(target instanceof TamableAnimal tamable){
                return tamable.getOwner() != this.getOwner();
            }
        if(this.getOwner() != null && this.getOwner().getLastAttacker() != null) {
            return this.getOwner().getLastAttacker() != null;
        }
        return this.getOwner().getLastHurtMob() != null;
    }

void minionGoals(){
    this.goalSelector.addGoal(3, new FollowOwnerGoal(this, 1, 3.0f, 6, true));
    if(this.getOwner() != null && this.getOwner().getLastAttacker() != null) {
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, this.getOwner().getLastAttacker().getClass(), 8, true, false, this::isValidTarget));
    }
}

    void followOwnerGoal(){
        if(this.getOwner() != null){
            this.goalSelector.addGoal(3, new FollowOwnerGoal(this, 1.15, 3.0f, 1, true));
        }
    }
}
