package net.kenji.kenjiscombatforms.entity.custom.noAiEntities;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

import java.util.List;

public class EnderEntity extends PathfinderMob {
    private static EnderEntity INSTANCE;

    public EnderEntity(EntityType<? extends PathfinderMob> pathfinderMob, Level level) {
        super(pathfinderMob, level);
        if (INSTANCE == null) {
            INSTANCE = this;
        }
    }

    @Override
    public void onAddedToWorld() {
        this.setOpacity(0.7F);
        super.onAddedToWorld();
    }



    public static EnderEntity getInstance(){
        return INSTANCE;
    }

    private static final EntityDataAccessor<Float> DATA_OPACITY = SynchedEntityData.defineId(EnderEntity.class, EntityDataSerializers.FLOAT);

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


    private Vec3 movementInput = Vec3.ZERO;
    private boolean isMoving;


    private ServerPlayer controllingPlayer;

    // This method should be called when you receive input from the client
    public void setMovementInput(ServerPlayer player, boolean forward, boolean backward, boolean left, boolean right, boolean up, boolean down) {
        this.controllingPlayer = player;
        float forwardInput = forward ? 1 : (backward ? -1 : 0);
        float strafeInput = left ? 1 : (right ? -1 : 0);  // This is correct: right is positive, left is negative
        float verticalInput = up ? 1 : (down ? -1 : 0);
        this.movementInput = new Vec3(strafeInput, verticalInput, forwardInput);
    }


    @Override
    public void travel(Vec3 travelVector) {
        this.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).ifPresent(cap -> {
            if (cap instanceof LivingEntityPatch<?> livingEntityPatch) {
                if (!livingEntityPatch.getEntityState().attacking()) {
                    if (this.controllingPlayer != null && !this.movementInput.equals(Vec3.ZERO)) {
                        double speed = 0.1; // Adjust this value to change movement speed
                        // Use the controlling player's orientation
                        float yaw = this.controllingPlayer.getYRot();
                        float pitch = this.controllingPlayer.getXRot();

                        // Set the entity's rotation to match the player's
                        this.setYRot(yaw);
                        this.setXRot(pitch);

                        // Calculate motion based on input and player's orientation
                        double motionX = -Math.sin(yaw * Math.PI / 180.0) * this.movementInput.z + Math.cos(yaw * Math.PI / 180.0) * this.movementInput.x;
                        double motionZ = Math.cos(yaw * Math.PI / 180.0) * this.movementInput.z + Math.sin(yaw * Math.PI / 180.0) * this.movementInput.x;
                        double motionY = this.movementInput.y; // Direct vertical control

                        Vec3 motion = new Vec3(motionX, motionY, motionZ).normalize().scale(speed);

                        // Set the entity's motion
                        this.setDeltaMovement(motion);
                        this.move(MoverType.SELF, this.getDeltaMovement());

                    } else {
                        // Apply air resistance when no input is given
                        Vec3 currentMotion = this.getDeltaMovement();
                        double airResistance = 0.98;
                        this.setDeltaMovement(currentMotion.multiply(airResistance, airResistance, airResistance));

                        // Stop completely if moving very slowly
                        if (this.getDeltaMovement().lengthSqr() < 0.0001) {
                            this.setDeltaMovement(Vec3.ZERO);
                        }
                    }
                }
                else if(livingEntityPatch.getEntityState().attacking()){
                    this.currentVelocity = Vec3.ZERO;
                }
            }
        });

    }
    @Override
    public boolean isNoGravity() {
        return true;
    }

    private static final float MAX_SPEED = 0.2F;
    private static final float ACCELERATION = 0.05F;
    private static final float DECELERATION = 0.03F;
    private Vec3 currentVelocity = Vec3.ZERO;
    private double currentVerticalVelocity = 0;
    private static final double VERTICAL_ACCELERATION = 0.05;
    private static final double VERTICAL_DECELERATION = 0.03;

    private static final double START_WALKING_THRESHOLD = 0.08;
    private static final double STOP_WALKING_THRESHOLD = 0.1;
    private boolean isWalking = false;


    // ... other fields and methods ...

    @Override
    public void tick() {
        super.tick();
        handleMovementAnimations();
        this.travel(Vec3.ZERO);
        updateMovement();
        monsterAttractTick();
    }

    private void updateMovement() {
        this.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).ifPresent(cap -> {
            if (cap instanceof LivingEntityPatch<?> livingEntityPatch) {
                if (!livingEntityPatch.getEntityState().attacking()) {
                    if (this.controllingPlayer != null) {

                        // Calculate target velocity

                        float yaw = this.controllingPlayer.getYRot();
                        float pitch = this.controllingPlayer.getXRot();

                        double motionX = -Math.sin(yaw * Math.PI / 180.0) * this.movementInput.z + Math.cos(yaw * Math.PI / 180.0) * this.movementInput.x;
                        double motionZ = Math.cos(yaw * Math.PI / 180.0) * this.movementInput.z + Math.sin(yaw * Math.PI / 180.0) * this.movementInput.x;
                        double targetVerticalVelocity = this.movementInput.y * MAX_SPEED;

                        Vec3 targetHorizontalVelocity = new Vec3(motionX, 0, motionZ).normalize().scale(MAX_SPEED);

                        // Smoothly interpolate horizontal velocity
                        Vec3 newHorizontalVelocity = new Vec3(
                                this.currentVelocity.x + (targetHorizontalVelocity.x - this.currentVelocity.x) * ACCELERATION,
                                0,
                                this.currentVelocity.z + (targetHorizontalVelocity.z - this.currentVelocity.z) * ACCELERATION
                        );

                        // Smoothly interpolate vertical velocity
                        if (Math.abs(targetVerticalVelocity) > 0.001) {
                            this.currentVerticalVelocity += (targetVerticalVelocity - this.currentVerticalVelocity) * VERTICAL_ACCELERATION;
                        } else {
                            this.currentVerticalVelocity *= (1 - VERTICAL_DECELERATION);
                        }

                        // Combine horizontal and vertical velocities
                        this.currentVelocity = new Vec3(newHorizontalVelocity.x, this.currentVerticalVelocity, newHorizontalVelocity.z);

                        // Apply movement
                        this.setDeltaMovement(this.currentVelocity);
                        this.move(MoverType.SELF, this.getDeltaMovement());

                        // Update rotation
                        this.setYRot(yaw);
                        this.setXRot(pitch);
                    } else {
                        // Apply deceleration when no input
                        this.currentVelocity = this.currentVelocity.scale(1 - DECELERATION);
                        this.currentVerticalVelocity *= (1 - VERTICAL_DECELERATION);

                        if (this.currentVelocity.lengthSqr() < 0.0001 && Math.abs(this.currentVerticalVelocity) < 0.0001) {
                            this.currentVelocity = Vec3.ZERO;
                            this.currentVerticalVelocity = 0;
                        }

                        this.setDeltaMovement(this.currentVelocity.add(0, this.currentVerticalVelocity, 0));
                        this.move(MoverType.SELF, this.getDeltaMovement());
                    }
                }
                else if(livingEntityPatch.getEntityState().attacking()){
                    this.currentVelocity = Vec3.ZERO;
                }
            }
        });
    }



    private void handleMovementAnimations() {
        Vec3 forward = Vec3.directionFromRotation(0, this.getYRot());
        double forwardMotion = this.getDeltaMovement().dot(forward);
                this.updateWalkAnimation(0);
                    if (forwardMotion > START_WALKING_THRESHOLD) {
                        isWalking = true;
                        this.updateWalkAnimation(1);
                    }
                    if(isWalking && forwardMotion < STOP_WALKING_THRESHOLD){
                        this.updateWalkAnimation(0);
                    }
                    if(forwardMotion == 0.075){
                        isWalking = false;
                    }
            }


    private void monsterAttractTick(){
        double radius = 30.0;
        AABB searchArea = new AABB(this.getOnPos()).inflate(radius);
        TargetingConditions targetingConditions = TargetingConditions.forCombat();

        List<Monster> nearbyEntities = this.level().getNearbyEntities(Monster.class, targetingConditions, this, searchArea);

        for (Monster monster : nearbyEntities) {
            if(monsterTypeCheck(monster)) {
                monster.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(monster, EnderEntity.class, 8, true, true, this::isAlive));
            }
        }
    }
    private boolean monsterTypeCheck(Monster monster){
        return !(monster instanceof Endermite) && !(monster instanceof EnderMan) && !(monster instanceof Creeper);
    }

    private boolean isAlive(LivingEntity livingEntity) {
        return this.isAlive();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypes.MOB_ATTACK)||
                source.is(DamageTypes.PLAYER_ATTACK)||
                source.is(DamageTypes.PLAYER_EXPLOSION)) {
            float reducedDamage = amount * 0.65f;
            float finalDamage = Math.min(reducedDamage, 10f);
            return super.hurt(source, finalDamage);
            }
        return false;
    }

    @Override
    public boolean canBeAffected(MobEffectInstance effectInstance) {
        return false;
    }
}
