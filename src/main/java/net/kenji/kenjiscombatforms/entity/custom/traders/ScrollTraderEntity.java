package net.kenji.kenjiscombatforms.entity.custom.traders;

import net.kenji.kenjiscombatforms.block.ModBlocks;
import net.kenji.kenjiscombatforms.config.KenjisCombatFormsCommon;
import net.kenji.kenjiscombatforms.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class ScrollTraderEntity extends WanderingTrader implements NeutralMob {
    public ScrollTraderEntity(EntityType<? extends WanderingTrader> p_35843_, Level p_35844_) {
        super(p_35843_, p_35844_);
    }

    private BlockPos wanderTarget;
    @Nullable private Boolean isAngryCache = false;
    private Boolean isAngryCacheLock = true;
    private boolean hasBeenProvoked = false;
    private static final Random RANDOM = new Random();

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("hasBeenProvoked", hasBeenProvoked);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        hasBeenProvoked = compound.getBoolean("hasBeenProvoked");
    }



    @Override
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {

            boolean randomBool = getRandom().nextBoolean();

            String message = "";
            String name = this.getName().getString();
            String blueName = Component.literal(name).withStyle(ChatFormatting.AQUA).getString();
            if (this.level().isClientSide && this.getTarget() == null && !this.isAngry()) {
                // Ignore interaction on the client side
                return InteractionResult.SUCCESS;
            } else if(this.getTarget() != null || this.isAngry() || hasBeenProvoked) {
                return InteractionResult.PASS; // Prevents interaction when angry or has target
            }

            if (pHand != InteractionHand.MAIN_HAND) {
                return InteractionResult.PASS;
            }

            if (this.isAlive() && !this.isTrading() && !this.isBaby()) {
                if (!this.getOffers().isEmpty()) {
                        this.setTradingPlayer(pPlayer);
                        this.openTradingScreen(pPlayer, this.getDisplayName(), 1);
                        if (randomBool) {
                            message = "How can I help you?";
                        } else if (!randomBool) {
                            message = "How's your day my good sir?";
                    }
                    String whiteMessage = "§f" + message;
                    pPlayer.sendSystemMessage(Component.literal("§b[" + blueName + "§b]" + whiteMessage));
                    return InteractionResult.sidedSuccess(this.level().isClientSide);
                }
            }

        return super.mobInteract(pPlayer, pHand);
    }


    @Override
    public float getSpeed() {
        if (this.getTarget() != null || this.isAngry()) {
            return 0.15f;
        }

        return 0.13f;
    }

    @Override
    protected void registerGoals() {
        traderGoals();
    }

    @Override
    public MerchantOffers getOffers() {
        MerchantOffers offers = new MerchantOffers();
        offers.add(createTrade2(Items.EMERALD, 35, ModBlocks.ESSENCE_CHANNELING_STATION.get().asItem(), 1,ModBlocks.SCROLL_FORMING_STATION.get().asItem(), 1));
        offers.add(createTrade2(Items.EMERALD, 16, Items.PAPER, 12, ModItems.BLANK_FORM_SCROLL.get(), 1));
        offers.add(createTrade2(Items.EMERALD, 12, Items.PAPER, 6, ModItems.BLANK_ABILITY_SCROLL.get(), 1));
        offers.add(createTrade(Items.EMERALD, 3, Items.GLOW_INK_SAC, 2));
        offers.add(createTrade2(Items.EMERALD, 64, ModItems.POWERESSENCE_TIER2.get(), 2, ModItems.ABILITY_ESSENCE.get(), 1));

        return offers;
    }


    protected void traderGoals(){
        this.goalSelector.addGoal(1, new LookAtTradingPlayerGoal(this));
        this.goalSelector.addGoal(2, new WanderToPositionGoal(this, 2.0, 0.35));
        this.goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, 0.35));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 0.35));
        this.goalSelector.addGoal(9, new InteractGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.goalSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class,10,  true, false, this::isAngryAt));
    }

    public void setWanderTarget(@javax.annotation.Nullable BlockPos p_35884_) {
        this.wanderTarget = p_35884_;
    }

    BlockPos getWanderTarget() {
        return this.wanderTarget;
    }

    class WanderToPositionGoal extends Goal {
        final ScrollTraderEntity trader;
        final double stopDistance;
        final double speedModifier;

        WanderToPositionGoal(ScrollTraderEntity p_35899_, double p_35900_, double p_35901_) {
            this.trader = p_35899_;
            this.stopDistance = p_35900_;
            this.speedModifier = p_35901_;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public void stop() {
            this.trader.setWanderTarget((BlockPos)null);
            ScrollTraderEntity.this.navigation.stop();
        }

        public boolean canUse() {
            BlockPos $$0 = this.trader.getWanderTarget();
            return $$0 != null && this.isTooFarAway($$0, this.stopDistance);
        }

        public void tick() {
            BlockPos $$0 = this.trader.getWanderTarget();
            if ($$0 != null && ScrollTraderEntity.this.navigation.isDone()) {
                if (this.isTooFarAway($$0, 10.0)) {
                    Vec3 $$1 = (new Vec3((double)$$0.getX() - this.trader.getX(), (double)$$0.getY() - this.trader.getY(), (double)$$0.getZ() - this.trader.getZ())).normalize();
                    Vec3 $$2 = $$1.scale(10.0).add(this.trader.getX(), this.trader.getY(), this.trader.getZ());
                    ScrollTraderEntity.this.navigation.moveTo($$2.x, $$2.y, $$2.z, this.speedModifier);
                } else {
                    ScrollTraderEntity.this.navigation.moveTo((double)$$0.getX(), (double)$$0.getY(), (double)$$0.getZ(), this.speedModifier);
                }
            }

        }

        private boolean isTooFarAway(BlockPos p_35904_, double p_35905_) {
            return !p_35904_.closerToCenterThan(this.trader.position(), p_35905_);
        }
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.getEntity() instanceof Player player) {
            this.setTarget((LivingEntity) pSource.getEntity());
            boolean randomBool = getRandom().nextBoolean();
            boolean randomBool2 = getRandom().nextBoolean();

            hasBeenProvoked = true;

            String message = "";

            String name = this.getName().getString();
            String redName = Component.literal(name).withStyle(ChatFormatting.RED).getString();

            if (Boolean.FALSE.equals(isAngryCache) && randomBool && this.getHealth() > 50) {
                isAngryCache = true;
                isAngryCacheLock = true;
                message = "Tf did I do to you?!";
            }
            if (Boolean.FALSE.equals(isAngryCache) && !randomBool && this.getHealth() > 50) {
                isAngryCache = true;
                isAngryCacheLock = true;
                message = "I would have helped you...";
            }

            if (isAngryCacheLock && this.getHealth() > 50) {
                String whiteMessage = "§f" + message;
                player.sendSystemMessage(Component.literal("§c[" + redName + "§c]" + whiteMessage));
                isAngryCacheLock = false;
            }
        }
        return super.hurt(pSource, pAmount);
    }



    @Override
    public boolean isAngryAt(LivingEntity pTarget) {
        return NeutralMob.super.isAngryAt(pTarget);
    }

    @Override
    public void setTarget(@Nullable LivingEntity pTarget) {
        super.setTarget(pTarget);

        if (pTarget instanceof Player) {
            this.setLastHurtByMob(pTarget);
            this.setPersistentAngerTarget(pTarget.getUUID());
            this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
        }
    }

    MerchantOffer createTrade2(Item buyItem, int buyCount, Item buyItem2, int buyCount2, Item sellItem, int sellCount){
return new MerchantOffer(
        new ItemStack(buyItem2, buyCount2),
    new ItemStack(buyItem, buyCount),
        new ItemStack(sellItem, sellCount),
        2,
        3,
        0.5f
        );
  }

    MerchantOffer createTrade(Item buyItem,  int buyCount, Item sellItem, int sellCount){
        return new MerchantOffer(
                new ItemStack(buyItem, buyCount),
                new ItemStack(sellItem, sellCount),
                2,
                3,
                0.5f
        );
    }



    public static boolean checkSpawnRules(EntityType<? extends ScrollTraderEntity> entityType, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        double chance = RANDOM.nextDouble();
        if (level instanceof ServerLevel && ((ServerLevel)level).dimension() == Level.OVERWORLD){
                if(level.getBrightness(LightLayer.BLOCK, pos) >= 8) {
                    if(level.getBrightness(LightLayer.SKY, pos) < 8){
                        return false;
                    }

                    // Check for nearby entities of the same type
                    int playerSearchRadius = KenjisCombatFormsCommon.ABILITY_TRADER_PLAYER_SPAWNDIST.get();
                    int traderSearchRadius = KenjisCombatFormsCommon.ABILITY_TRADER_SPAWNDIST.get();// Adjust this value to change the minimum distance between spawns
                    List<Player> nearbyPlayers = level.getEntitiesOfClass(Player.class,
                            new AABB(pos).inflate(playerSearchRadius),
                            e -> true);

                    List<ScrollTraderEntity> nearbyTraders = level.getEntitiesOfClass(ScrollTraderEntity.class,
                            new AABB(pos).inflate(traderSearchRadius),
                            e -> true);

                    if(chance < KenjisCombatFormsCommon.ABILITY_TRADER_SPAWN_CHANCE.get()) {
                        if (!nearbyPlayers.isEmpty()) {
                            if (nearbyTraders.isEmpty()) {
                                return true; // Allow spawn if no nearby entities found
                            }
                        }
                    }
                }
        }
        return false;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public boolean isEffectiveAi() {
        return super.isEffectiveAi();
    }

    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    private int remainingPersistentAngerTime;
    @Nullable
    private UUID persistentAngerTarget;

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.remainingPersistentAngerTime;
    }

    @Override
    public void setRemainingPersistentAngerTime(int pTime) {
        this.remainingPersistentAngerTime = pTime;
    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID pTarget) {
        this.persistentAngerTarget = pTarget;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }


}
