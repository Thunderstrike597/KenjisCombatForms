package net.kenji.kenjiscombatforms.entity.custom.SenseiEntities;

import net.kenji.kenjiscombatforms.config.KenjisCombatFormsCommon;
import net.kenji.kenjiscombatforms.entity.ModEntities;
import net.kenji.kenjiscombatforms.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class ExiledSenseiEntity extends TamableAnimal implements NeutralMob{

    public ExiledSenseiEntity(EntityType<? extends TamableAnimal> p_34368_, Level p_34369_) {
        super(p_34368_, p_34369_);
    }


    @Override
    public int getRemainingPersistentAngerTime() {
        return 0;
    }

    @Override
    public void setRemainingPersistentAngerTime(int i) {

    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return null;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID uuid) {

    }

    @Override
    public void startPersistentAngerTimer() {

    }




    @Override
    public void playerDied(@NotNull Player p_21677_) {
        NeutralMob.super.playerDied(p_21677_);
    }


    @Override
    public boolean canHoldItem(ItemStack stack) {
        return false;
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.PILLAGER_AMBIENT;
    }

    @Override
    public SoundEvent getHurtSound(DamageSource p_34327_) {
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
    public boolean canPickUpLoot() {
        return false;
    }

    @Override
    public boolean canTakeItem(ItemStack p_21522_) {
        return false;
    }

    @Override
    public boolean isAngryAt(@NotNull LivingEntity livingEntity) {
        Player player = (Player) livingEntity;
        return NeutralMob.super.isAngryAt(player);
    }

// Variables

    boolean isStaying = false;
    boolean canWander = false;
    boolean isDefensive = false;


    int healDelayThresh = 120;


    @Nullable private Boolean isAfraidCache = false;
    @Nullable private Boolean isAngryCache = false;
    private Boolean isAngryCacheLock = true;

    int waitNumb = 0;
    boolean hasInteracted = false;

    private boolean canInteract = true;
    private long lastInteractionTime = 0;
    private static final long INTERACTION_COOLDOWN = 250; // 1 second cooldown
    private int interactionCooldownTicks = 0;
    int healTimeInt = 0;
    int healDelayInt = 0;
    boolean healTickAllow;
    boolean canRegen = true;
    boolean hasBeenDamaged = false;


    //Goal Vars
    private final RandomStrollGoal wanderGoal = new RandomStrollGoal(this, 0.5, 1);
    private final FollowOwnerGoal followGoal = new FollowOwnerGoal(this, 1.25, 5.0f, 3.0f, true);
    private final SitWhenOrderedToGoal stayGoal =  new SitWhenOrderedToGoal(this);
    private final OwnerHurtTargetGoal targetOwnerAttacker = new OwnerHurtTargetGoal(this);
    private final AvoidEntityGoal<Player> avoidPlayerGoal = new AvoidEntityGoal<>(this, Player.class, 6.0F, 1.4, 1.0);
            //

    // NBT Compound tags

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("interactionState", interactionState);
        compound.putInt("interactionState2", interactionState2);
        compound.putBoolean("isDefensive", isDefensive);
        compound.putBoolean("isStaying", isStaying);
        compound.putBoolean("canWander", canWander);
        compound.putBoolean("hasBeenDamaged", hasBeenDamaged);
        compound.putString("lastMessage", lastMessage);
        compound.putLong("lastMessageTime", lastMessageTime);
       if (Boolean.TRUE.equals(isAfraidCache)) {
           compound.putBoolean("isAfraidCache", isAfraidCache);
           }
        }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        interactionState = compound.getInt("interactionState");
        interactionState2 = compound.getInt("interactionState2");
        isDefensive = compound.getBoolean("isDefensive");
        isStaying = compound.getBoolean("isStaying");
        canWander = compound.getBoolean("canWander");
        hasBeenDamaged = compound.getBoolean("hasBeenDamaged");
        lastMessage = compound.getString("lastMessage");
        lastMessageTime = compound.getLong("lastMessageTime");
        isAfraidCache = compound.getBoolean("isAfraidCache");
    }

    public void resetState() {
        isStaying = false;
        canWander = false;
        lastInteractionTime = 0;
        // Reset other state variables
    }


    private static boolean isCompatDifficulty(){
        return KenjisCombatFormsCommon.DIFFICULTY_COMPAT_MODE.get();
    }

    @Override
    public void onAddedToWorld() {
        if (this.isTame()) {
            Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(KenjisCombatFormsCommon.TAMED_EXILED_SENSEI_HEALTH.get());
        }else if (!this.isTame()) {
            Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(KenjisCombatFormsCommon.EXILED_SENSEI_HEALTH.get());
        } if(!hasBeenDamaged){
            setHealth(this.getMaxHealth());
        }

        super.onAddedToWorld();
    }

//Functions
    // Healing

    public boolean canHeal() {
        if (healDelayInt >= healDelayThresh) {
            canRegen = true;
            return true;
        } else {
            canRegen = false;
            return false;
        }
    }

    public void healFunc() {
        if (canHeal() && canRegen && this.isTame()) {
            this.setHealth(this.getHealth() + 0.08f); // Heal by 0.5 health (1/4 heart)
        }
    }



    void resetHealing(){
        float currentHealth = this.getHealth();
        if (currentHealth < lastHealth) {
            healDelayInt = 0;
            tameProgress = 0;
            regenSwitch();
            resetTameProgress();
            hasBeenDamaged = true;
        }
        lastHealth = currentHealth;
    }

    public void healDelayTime(){
        if (healDelayInt <= healDelayThresh) {
            healDelayInt++;
        }
    }

    boolean isAfraidCacheLock = false;

    void regenSwitch(){
        canRegen = false;
    }
    void resetTameProgress(){
        canRegen = false;
    }

//Other Functions
void isAfraidFunc(){
    double chance = RANDOM.nextDouble();
        float currentHealth = this.getHealth();
       if(getLastAttacker() != null) {
           if (currentHealth < KenjisCombatFormsCommon.IS_AFRAID_MIN_HEALTH.get() && !isTame() && !isAfraidCacheLock && getLastAttacker().is(playerEntity)) {
               isAfraidCacheLock = true;
               if (chance < KenjisCombatFormsCommon.IS_AFRAID_CHANCE.get()) {
                   isAfraidCache = true;
                   System.out.println("Scared Success");
               } else if(chance > KenjisCombatFormsCommon.IS_AFRAID_CHANCE.get()){
                   isAfraidCache = false;
                   System.out.println("Scared Failed");
               }
           }
       }
    lastHealth = currentHealth;
       if (Boolean.TRUE.equals(this.isAfraidCache)){
           this.addEffect(new MobEffectInstance(MobEffects.GLOWING, 5, 1, true, true));
       }
}



    //GoalFunctions

    protected void registerGoals() {
        randomGoals();
        commandGoals();
       attackingGoals();
       naturalAggroGoals();
    }


    void commandGoals(){
        sitGoalFunc();
        followGoalFunc();
        wanderGoalFunc();
        attackOwnerAttacker();
    }

    void interactDelay(){
        if (interactionCooldownTicks > 0) {
            interactionCooldownTicks--;
            if (interactionCooldownTicks == 0) {
                canInteract = true;
            }
        }
    }

public void removeAfraid(){
        if(tameProgress != 0 && !isTame()){
            this.goalSelector.removeGoal(avoidPlayerGoal);
        } if (tameProgress >= 1 && !isTame()){
            this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 5));
    }

}

//Checks



    Boolean isStayingAction(){
        return isStaying && this.isTame();
    }


    Boolean isFollowing(){
        return !isStaying && !canWander && this.isTame();
    }

    Boolean isWandering(){
        return canWander && this.isTame();
    }

    public void resetAfraidState() {
        isAfraidCache = false;
    }

    private float lastHealth;


//Ticking
@Override
public void tick() {
    super.tick();

    resetHealing();
    removeAfraid();

    interactDelay();
    commandGoals();
    healDelayTime();
    canHeal();
    healFunc();
    isAfraidFunc();
}
//



    @Override
    public boolean isAngry() {
        if (Boolean.TRUE.equals(isAfraidCache)) {
            return false;
        }
       else{
           return true;
       }
    }

    @Override
    public boolean isTame() {
        return super.isTame();
    }

    public static final Random RANDOM = new Random();
   Player playerEntity;
   Entity undeadSensei;

    //Effect Overrides
@Override
public void setLastHurtByMob(@Nullable LivingEntity entity) {
    double chance = RANDOM.nextDouble();

    if (entity instanceof Player) {
        playerEntity = (Player) entity;
    }
    if(entity instanceof UndeadSenseiEntity){
        undeadSensei = (UndeadSenseiEntity) entity;
    }
    if (entity != null) {
        if (chance < 0.3) {
        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 180, 0, true, true));
        }
        if (chance < 0.12 && !this.isTame() && entity instanceof UndeadSenseiEntity){
            this.convertTo(ModEntities.UNDEAD_SENSEI.get(), true);
        }
    }


    super.setLastHurtByMob(entity);
}
//


    @Override
    public void setLastHurtByPlayer(@Nullable Player player) {
        boolean randomBool = getRandom().nextBoolean();
        boolean randomBool2 = getRandom().nextBoolean();


        String message = "";

if(player instanceof Player) {
    String name = this.getName().getString();
    String redName = Component.literal(name).withStyle(ChatFormatting.RED).getString();

    if (Boolean.FALSE.equals(isAngryCache) && randomBool && this.getHealth() > 50 && !isTame()) {
        isAngryCache = true;
        isAngryCacheLock = true;
        message = "How dare you!";
    }
    if (Boolean.FALSE.equals(isAngryCache) && !randomBool && this.getHealth() > 50 && !isTame()) {
        isAngryCache = true;
        isAngryCacheLock = true;
        message = "You will die.";
    }

    if (isAngryCacheLock && this.getHealth() > 50 && !isTame()) {
        String whiteMessage = "§f" + message;
        player.sendSystemMessage(Component.literal("§c[" + redName + "§c]" + whiteMessage));
        isAngryCacheLock = false;
    }

    if(!isTame()){
        this.tameProgress = 0;
    }
}
    super.setLastHurtByPlayer(player);

    }

    @Override
public void setLastHurtMob(@Nullable Entity entity) {
        double chance = RANDOM.nextDouble();
    LivingEntity lEntity = (LivingEntity) entity;

    if(entity != null){
        if (chance < 0.2) {
            lEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 180, 0, true, true));
        }
    }
    assert entity != null;
    super.setLastHurtMob(entity);
}
///



    @Override
    protected void markHurt() {
        healDelayInt = 0;
        regenSwitch();
        this.removeEffect(MobEffects.REGENERATION);
        super.markHurt();
    }


//Behaviour Goals

        protected void randomGoals(){
            this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, UndeadSenseiEntity.class, 6.0F, 1.4, 1.0));
            if(!isTame()) {
                this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
                this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
                if (Boolean.FALSE.equals(isAfraidCache)) {
                      this.goalSelector.addGoal(2, new RandomStrollGoal(this, 0.5));
                }
            }
        }

        protected void attackingGoals(){
    if (!isTame()) {
              this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, new Class[0])).setAlertOthers(new Class[0]));
              if(this.getLastAttacker() != null && Boolean.FALSE.equals(isAfraidCache)) {
                  this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, this.getLastAttacker().getClass(), 10, true, false, this::isAngryAt));
              }
          }else if (isTame() && this.getOwner() != null) {
              this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Objects.requireNonNull(Objects.requireNonNull(this.getOwner()).getLastAttacker()).getClass(), 10, true, false, this::isAngryAt));
          }
        }

protected void naturalAggroGoals(){
    if(!isStaying) {
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Pillager.class, true, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Vindicator.class, true, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Evoker.class, true, false));
    }
}


        //Goal Functions



       void attackOwnerAttacker(){
        if (this.isTame() && isDefensive){
            this.targetSelector.addGoal(2, targetOwnerAttacker);
        }
        else  if (this.isTame() && !isDefensive){
            this.goalSelector.removeGoal(targetOwnerAttacker);
        }
       }


    void sitGoalFunc(){
        if(isStayingAction() && this.isTame()){
            this.goalSelector.addGoal(2, stayGoal);
                this.goalSelector.removeGoal(wanderGoal);
                this.goalSelector.removeGoal(followGoal);
                this.setOrderedToSit(true);
        }
    }

       void followGoalFunc(){
           if(isFollowing() && this.isTame()){
               this.goalSelector.addGoal(1, followGoal);
                   this.goalSelector.removeGoal(wanderGoal);
                   this.goalSelector.removeGoal(stayGoal);
                    this.setOrderedToSit(false);
           }
       }

      void wanderGoalFunc(){
          if (isWandering() && this.isTame()) {
              this.goalSelector.addGoal(2, wanderGoal);
                  this.goalSelector.removeGoal(followGoal);
                  this.goalSelector.removeGoal(stayGoal);
          }
      }



    // 1 second cooldown



    private int interactionState = 0;
    private int interactionState2 = 0;




    Player interactPlayer;



    int tameProgress = 0;


    private String lastMessage = "";
    private long lastMessageTime = 0;

    void deleteFormItem(Player player){
        player.getMainHandItem().setCount(0);
    }

    @Override
    public InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand interactionHand) {
        return mobInteract2(player, interactionHand, this.level(), this.blockPosition());

    }

    void playTameSoundEvent(Level level){
        level.playSound(null, this.blockPosition(), SoundEvents.VILLAGER_YES, SoundSource.AMBIENT, 4.0F, 0.65F);
    }

    void playTamingProcessEvent(Level level){
        level.playSound(null, this.blockPosition(), SoundEvents.VILLAGER_TRADE, SoundSource.AMBIENT, 4.0F, 0.65F);
    }
    void playCommandSoundEvent(Level level, Player player){
        level.playSound(null, this.blockPosition(), SoundEvents.PILLAGER_AMBIENT, SoundSource.AMBIENT, 4.0F, 1.25F);
    }



    //Large Overrides
        //Mob Interaction Override


    public InteractionResult mobInteract2(Player player, InteractionHand hand, Level level, BlockPos pos) {

        if (this.level().isClientSide) {
            // Ignore interaction on the client side
            return InteractionResult.SUCCESS;
        }
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }


        boolean emeraldItem = player.getMainHandItem().is(Items.EMERALD);
        boolean formItem = player.getMainHandItem().is(KenjisCombatFormsCommon.getSenseiTameItem());
        int itemCount = player.getMainHandItem().getCount();

        interactPlayer = player;
        long currentTime = System.currentTimeMillis();
            if (interactPlayer != null) {
                if (!this.level().isClientSide) {
                    if (emeraldItem && itemCount >= KenjisCombatFormsCommon.TAMING_EMERALDS_MIN.get() && tameProgress == 0 && Boolean.TRUE.equals(isAfraidCache) && !this.isTame() && KenjisCombatFormsCommon.IS_SENSEI_TAMABLE.get()) {
                        player.getMainHandItem().setCount(itemCount - KenjisCombatFormsCommon.TAMING_EMERALDS_MIN.get());
                        tameProgress = 1;
                        playTamingProcessEvent(level);
                        return InteractionResult.SUCCESS;
                    }
                }
            }

        if (Boolean.TRUE.equals(isAfraidCache) && !this.isTame() && formItem && tameProgress == 1 && KenjisCombatFormsCommon.IS_SENSEI_TAMABLE.get()) {
            String messageTameString = "messages.kenjiscombatforms.tame_message";
            this.tame(player);
            deleteFormItem(player);
            tameProgress = 2;
            isStaying = true;
            setOrderedToSit(true);
            canWander = false;
            resetAfraidState();
            tameMessage(player, messageTameString);
            lastInteractionTime = currentTime;
            Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(KenjisCombatFormsCommon.TAMED_EXILED_SENSEI_HEALTH.get());
            this.setHealth(this.getMaxHealth());
           this.removeEffect(MobEffects.GLOWING);
            playTameSoundEvent(level);
            return InteractionResult.SUCCESS;
        }

        if (currentTime - lastInteractionTime < INTERACTION_COOLDOWN) {
            return InteractionResult.PASS;
        }



        lastInteractionTime = currentTime;

        // Cycle through states
        interactionState = (interactionState + 1) % 3;
        interactionState2 = (interactionState2 + 1) % 2;

        String message = null;
        if (isTame() && !player.isCrouching()) {
            switch (interactionState) {
                case 0:
                    isStaying = true;
                    canWander = false;
                    playCommandSoundEvent(level, player);
                    message = "messages.kenjiscombatforms.stay_message";
                    break;
                case 1:
                    isStaying = false;
                    canWander = false;
                    playCommandSoundEvent(level, player);
                    message = "messages.kenjiscombatforms.follow_message";
                    break;
                case 2:
                    isStaying = false;
                    canWander = true;
                    playCommandSoundEvent(level, player);
                    message = "messages.kenjiscombatforms.wander_message";
                    break;
            }
        }
        if (isTame() && player.isCrouching()) {
            switch (interactionState2) {
                case 0:
                    isDefensive = true;
                    playCommandSoundEvent(level, player);
                    message = "messages.kenjiscombatforms.defend_message";
                    break;
                case 1:
                    isDefensive = false;
                    playCommandSoundEvent(level, player);
                    message = "messages.kenjiscombatforms.passive_message";
                    break;
            }
        }

        if (!message.isEmpty()) {

            sendUniqueMessage(player, message);
        }

        return InteractionResult.SUCCESS;
    }

    private void resetInteraction() {
        canInteract = true;
    }


    //Messages
    //Send Unique Messages
    private void sendUniqueMessage(Player player, String translationKey) {
        // Create the translated message component
        Component translatedMessage = Component.translatable(translationKey).withStyle(ChatFormatting.WHITE);

        // Create the name component with green formatting
        String name = this.getName().getString();
        Component greenName = Component.literal(name).withStyle(ChatFormatting.GREEN);

        // Construct the full message component
        Component fullMessage = Component.literal("§a [")
                .append(greenName)
                .append(Component.literal("§a]: "))
                .append(translatedMessage);

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastMessageTime > INTERACTION_COOLDOWN) { // 1 second cooldown between messages
            player.sendSystemMessage(fullMessage);
            lastMessageTime = currentTime;
        }
    }

    //Send Tame Messages
    private void tameMessage(Player player, String translationKey) {
        Component translatedMessage = Component.translatable(translationKey).withStyle(ChatFormatting.WHITE);

        String name = this.getName().getString();
        String greenName = Component.literal(name).withStyle(ChatFormatting.GREEN).getString();


        Component fullMessage = Component.literal("§a [")
                .append(greenName)
                .append(Component.literal("§a]: "))
                .append(translatedMessage);

        player.sendSystemMessage(fullMessage);
    }

    //Set Target Override
    @Override
    public void setTarget(@Nullable LivingEntity lEntity) {
        if(getLastHurtByMob() != null) {
            if (this.getHealth() > KenjisCombatFormsCommon.IS_AFRAID_MIN_HEALTH.get() && !isStaying && !getLastHurtByMob().is(undeadSensei) && Boolean.FALSE.equals(isAfraidCache)) {
                this.setLastHurtByMob(lEntity);
            }else if(this.getHealth() > KenjisCombatFormsCommon.IS_AFRAID_MIN_HEALTH.get() && !isStaying && getLastHurtByMob().is(undeadSensei)){
                this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, UndeadSenseiEntity.class, 6.0F, 1.4, 1.0));
            }
        }
        if (Boolean.TRUE.equals(isAfraidCache) && !isTame() && !isStaying) {
            lEntity = null;
            this.addEffect(new MobEffectInstance(MobEffects.GLOWING, 300, 0, true, true));
           if(tameProgress == 0) {
               this.goalSelector.addGoal(1, avoidPlayerGoal);
           }
        }

        else if (isTame() && isDefensive && this.getOwner() != null){
           this.getOwner().setLastHurtByMob(lEntity);
        }else if (isTame() && !isDefensive){
            lEntity = null;
        }
        super.setTarget(lEntity);
    }
 //

    public static boolean checkSpawnRules(EntityType<? extends ExiledSenseiEntity> entityType, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        double chance = RANDOM.nextDouble();
        if (level instanceof ServerLevel && ((ServerLevel)level).dimension() == Level.OVERWORLD && level.getDifficulty() != Difficulty.PEACEFUL) {
            if (level.getBlockState(pos.below()).isSolid() && level.getBlockState(pos).isAir() && level.getBlockState(pos.above()).isAir() && level.getBrightness(LightLayer.BLOCK, pos) > 8){
                if(level.getBrightness(LightLayer.SKY, pos) < 8){
                    return false;
                }

                // Check for nearby entities of the same type
                int searchRadius = KenjisCombatFormsCommon.EXILED_SENSEI_SPAWNDIST.get(); // Adjust this value to change the minimum distance between spawns
                List<ExiledSenseiEntity> nearbyEntities = level.getEntitiesOfClass(ExiledSenseiEntity.class,
                        new AABB(pos).inflate(searchRadius),
                        e -> true);

                if(chance < KenjisCombatFormsCommon.EXILED_SENSEI_SPAWN_CHANCE.get()) {
                    if (nearbyEntities.isEmpty()) {
                        return true; // Allow spawn if no nearby entities found
                    }
                }
            }
        }
        return false;
}


    //Misc

        @Override
        public boolean isEffectiveAi() {
            return super.isEffectiveAi();
        }

        @Override
        public boolean isAlwaysTicking() {
            return super.isAlwaysTicking();
        }

    @Override
    protected void actuallyHurt(@NotNull DamageSource p_21240_, float p_21241_) {
        super.actuallyHurt(p_21240_, p_21241_);
    }

    @Override
    public void setItemInHand(@NotNull InteractionHand p_21009_, @NotNull ItemStack p_21010_) {
        super.setItemInHand(p_21009_, p_21010_);
    }


    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
        return null;
    }
}
