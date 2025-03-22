package net.kenji.kenjiscombatforms.api.handlers;

import com.google.common.base.Optional;
import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.capabilities.ExtraContainerCapability;
import net.kenji.kenjiscombatforms.api.handlers.power_data.EnderPlayerDataSets;
import net.kenji.kenjiscombatforms.api.handlers.power_data.WitherPlayerDataSets;
import net.kenji.kenjiscombatforms.api.managers.FormLevelManager;
import net.kenji.kenjiscombatforms.api.powers.WitherPowers.WitherFormAbility;
import net.kenji.kenjiscombatforms.api.utils.SkillUtils;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseFinalFormClass;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseFistClass;
import net.kenji.kenjiscombatforms.network.NetworkHandler;
import net.kenji.kenjiscombatforms.network.playerData.SkillPlayerDataPacket;
import net.kenji.kenjiscombatforms.network.slots.RemoveItemPacket;
import net.kenji.kenjiscombatforms.network.slots.SwitchItemPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import org.slf4j.event.LoggingEvent;
import reascer.wom.gameasset.WOMSkills;
import yesman.epicfight.gameasset.EpicFightSkills;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.skill.dodge.DodgeSkill;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import java.util.List;

@Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonEventHandler {

    int originalSlot = -1;

    public int getOriginalSlot(){
        return originalSlot;
    }

    public void setOriginalSlot(int slotIndex){
        originalSlot = slotIndex;
    }

    private FormLevelManager.PlayerFormLevelData getOrCreateLevelData(ServerPlayer player){
        return LevelHandler.getInstance().getOrCreatePlayerLevelData(player);
    }

    private static final CommonEventHandler INSTANCE = new CommonEventHandler();
    public static CommonEventHandler getInstance(){
        return INSTANCE;
    }

    public boolean getIsNearItem(Player player){
        return getInstance().isNearItem(player);
    }

    public boolean getIsHoldingFistForm(Player player){
        return isHoldingFistForm(player);
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        CompoundTag nbt = player.getPersistentData();

        if(player.getMainHandItem().getItem() instanceof BaseFinalFormClass){
            if (!nbt.contains("storedItem")) {
                player.getInventory().setItem(getInstance().originalSlot, ItemStack.EMPTY);
            }
        }
        if (nbt.contains("storedItem")) {
            ItemStack storedItem = ItemStack.of(nbt);

            player.getInventory().setItem(getInstance().originalSlot, storedItem);
            NetworkHandler.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
                    new RemoveItemPacket(getInstance().originalSlot, storedItem)
            );
            nbt.remove("storedItem");
        }
        if (nbt.contains("storedDodgeSkill")) {
            String skillName = nbt.getString("storedDodgeSkill");

            Skill skillToSet = SkillUtils.getSkillByName(skillName);
            if (skillToSet != null) {
                event.getEntity().getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).ifPresent(cap -> {
                    if (cap instanceof PlayerPatch<?> playerPatch) {
                        if(getInstance().getSkillValid(playerPatch)) {
                            playerPatch.getSkill(SkillSlots.DODGE).setSkill(skillToSet);
                            nbt.remove("storedDodgeSkill");
                            System.out.println("storeDodgeSkill" + skillName);
                        }
                    }
                });
            }
        }
    }
    boolean getSkillValid(PlayerPatch<?> playerPatch){
        return playerPatch.getSkill(SkillSlots.DODGE).getSkill() == WOMSkills.SHADOWSTEP ||
                playerPatch.getSkill(SkillSlots.DODGE).getSkill() == WOMSkills.ENDERSTEP ||
                playerPatch.getSkill(SkillSlots.DODGE).getSkill() == null;
    }





    @SubscribeEvent
    public static void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide) {

            int selectedSlot = player.getInventory().selected;
            ItemStack currentItem = player.getInventory().getItem(selectedSlot);

            if (player.getMainHandItem().getItem() instanceof BaseFistClass) {
                player.getInventory().setItem(player.getInventory().selected, Items.AIR.getDefaultInstance());

                player.getInventory().setChanged();
            }

            player.getCapability(ExtraContainerCapability.EXTRA_CONTAINER_CAP).ifPresent(container -> {
                ItemStack storedItem = container.getStoredItem();


                if (!container.getStoredItem().isEmpty()) {
                    player.getInventory().setItem(getInstance().getOriginalSlot(), storedItem);

                    NetworkHandler.INSTANCE.send(
                            PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
                            new SwitchItemPacket(getInstance().getOriginalSlot(), storedItem)
                    );
                    container.setStoredItem(ItemStack.EMPTY);
                    player.getInventory().setChanged();
                }

            });
        }
    }


    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();

        if(entity instanceof Player player) {

            int selectedSlot = player.getInventory().selected;
        ItemStack currentItem = player.getInventory().getItem(selectedSlot);


            player.getCapability(ExtraContainerCapability.EXTRA_CONTAINER_CAP).ifPresent(container -> {
                ItemStack storedItem = container.getStoredItem();
                if (!container.getStoredItem().isEmpty()) {
                    player.getInventory().setItem(getInstance().getOriginalSlot(), storedItem);
                    container.setStoredItem(ItemStack.EMPTY);
                    //NetworkHandler.INSTANCE.sendToServer(new RemoveItemPacket(getInstance().getOriginalSlot(), storedItem));

                    //getInstance().setOriginalSlot(-1);
                    player.getInventory().setChanged();
                }
            });
        }
    }

        @SubscribeEvent
        public void itemTossEvent(ItemTossEvent event) {
            if (isHoldingFistForm(event.getPlayer())) {

                event.setCanceled(true);
            }
        }

    @SubscribeEvent
    public static void onGuiOpen(PlayerContainerEvent.Open event) {
        Player player = event.getEntity();
            //removeFistFormFromInventory(player);
    }

    private static boolean isHoldingFistForm(Player player){
        Item handItem = player.getMainHandItem().getItem();
        return handItem instanceof BaseFistClass;
    }


    private boolean isNearItem(Player player) {
      final double ITEM_DETECTION_RANGE = player.getPickRadius();
                AABB detectionBox = player.getBoundingBox().inflate(ITEM_DETECTION_RANGE);
        List<ItemEntity> nearbyItems = player.level().getEntitiesOfClass(ItemEntity.class, detectionBox);
        return !nearbyItems.isEmpty();
    }
}
