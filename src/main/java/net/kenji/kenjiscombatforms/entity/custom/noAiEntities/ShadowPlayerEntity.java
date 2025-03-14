package net.kenji.kenjiscombatforms.entity.custom.noAiEntities;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ShadowPlayerEntity extends TamableAnimal {


    public ShadowPlayerEntity(EntityType<? extends TamableAnimal> p_27403_, Level p_27404_) {
        super(p_27403_, p_27404_);
    }

    @Override
    public float getSpeed() {
        return 0.05f;

    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        decoyGoals();
    }



    private void decoyGoals(){
           this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class,  10, true, true, this::isValidTarget));
    }

    private boolean isValidTarget(LivingEntity target) {
        if (target == this.getOwner() || target.getTeam() == this.getTeam()) return false;
        if(target instanceof TamableAnimal tamable){
            return tamable.getOwner() != this.getOwner();
        }

        return true;
    }


    private void decoyAttractTick(){
        double radius = 30.0;
        AABB searchArea = new AABB(this.getOnPos()).inflate(radius);
        TargetingConditions targetingConditions = TargetingConditions.forCombat();

        List<Monster> nearbyEntities = this.level().getNearbyEntities(Monster.class, targetingConditions, this, searchArea);

        for (Monster monster : nearbyEntities) {
            if (monster.getTarget() != null) {
                monster.setTarget(null);
                monster.targetSelector.removeGoal(new NearestAttackableTargetGoal<>(monster, LivingEntity.class, true));
                monster.targetSelector.removeGoal(new NearestAttackableTargetGoal<>(monster, Player.class, true));
                monster.targetSelector.addGoal(1,new NearestAttackableTargetGoal<>(monster, ShadowPlayerEntity.class, true));
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        decoyAttractTick();
    }

    @Override
    public boolean isEffectiveAi() {
        return super.isEffectiveAi();
    }



    @Override
    public void setTarget(@Nullable LivingEntity pTarget) {
        super.setTarget(pTarget);
    }



    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }
}
