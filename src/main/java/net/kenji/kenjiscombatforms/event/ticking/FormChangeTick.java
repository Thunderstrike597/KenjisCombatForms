package net.kenji.kenjiscombatforms.event.ticking;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.handlers.CommonEventHandler;
import net.kenji.kenjiscombatforms.api.handlers.FormChangeHandler;
import net.kenji.kenjiscombatforms.api.handlers.LevelHandler;
import net.kenji.kenjiscombatforms.api.handlers.power_data.EnderPlayerDataSets;
import net.kenji.kenjiscombatforms.api.handlers.power_data.WitherPlayerDataSets;
import net.kenji.kenjiscombatforms.api.interfaces.form.AbstractFormData;
import net.kenji.kenjiscombatforms.api.managers.FormLevelManager;
import net.kenji.kenjiscombatforms.api.managers.forms.*;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.basic_form.BasicFist2Item;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.basic_form.BasicFist3Item;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.basic_form.BasicFistItem;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.power_form.PowerFist2Item;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.power_form.PowerFist3Item;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.power_form.PowerFistItem;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.swift_form.SwiftFist2Item;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.swift_form.SwiftFist3Item;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.swift_form.SwiftFistItem;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.void_form.VoidFist2Item;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.void_form.VoidFistItem;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.wither_form.WitherFist2Item;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.wither_form.WitherFist3Item;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.wither_form.WitherFistItem;
import net.kenji.kenjiscombatforms.item.custom.forms.BaseFormClass;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FormChangeTick {

    private static final Set<UUID> playersWithOpenGui = new HashSet<>();
    private static final Map<UUID, Boolean> playerGuiStates = new ConcurrentHashMap<>();
    private static final Map<UUID, Boolean> playerCombatStates = new ConcurrentHashMap<>();


    public static boolean isGuiOpen(Player player) {
        return playerGuiStates.getOrDefault(player.getUUID(), false);
    }

    public static void setGuiOpen(UUID playerUUID, boolean value) {
        playerGuiStates.put(playerUUID, value);
    }

    public static boolean isHandCombat(Player player) {
        return playerCombatStates.getOrDefault(player.getUUID(), true);
    }

    public static void setHandCombat(UUID playerUUID, boolean value) {
        playerCombatStates.put(playerUUID, value);
    }



    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Level level = event.level;
            LevelHandler levelHandler = LevelHandler.getInstance();
            BasicFistItem defaultFistItem = BasicFistItem.getInstance();


            for (Player player : level.players()) {
                WitherPlayerDataSets.WitherFormPlayerData wData = WitherPlayerDataSets.getInstance().getOrCreateWitherFormPlayerData(player);
                EnderPlayerDataSets.EnderFormPlayerData eData = EnderPlayerDataSets.getInstance().getOrCreateEnderFormPlayerData(player);
                boolean isWitherActive = wData.isAbilityActive();
                boolean isEnderActive = eData.isAbilityActive();

                boolean areFinalActive = isWitherActive || isEnderActive;


                if (player instanceof ServerPlayer serverPlayer) {
                    List<ItemEntity> itemEntities = level.getEntitiesOfClass(ItemEntity.class, player.getBoundingBox().inflate(5.0));
                    for (ItemEntity entity : itemEntities) {
                        ItemStack itemStack = entity.getItem();
                        if (itemStack.getItem() instanceof BaseFormClass) {
                            entity.kill();
                        }
                    }



                    if (isGuiOpen(player) || !isHandCombat(player) || areFinalActive || CommonEventHandler.getInstance().getIsNearItem(player)) {
                        // GUI is open, remove fist form
                        playersWithOpenGui.add(player.getUUID());
                        CommonEventHandler.removeFistFormFromInventory(player);
                    } else if (isHandCombat(player) ) {
                        // GUI is closed, set fist form if necessary
                        playersWithOpenGui.remove(player.getUUID());
                        if (basicSelected(player)) {
                            setBasicFistForm(serverPlayer, levelHandler);
                        } else if (voidSelected(player)) {
                            setVoidFistForm(serverPlayer, levelHandler);
                        } else if (witherSelected(player)) {
                            setWitherFistForm(serverPlayer, levelHandler);
                        }  else if (swiftSelected(player)) {
                            setSwiftFistForm(serverPlayer, levelHandler);
                        } else if (powerSelected(player)) {
                            setPowerFistForm(serverPlayer, levelHandler);
                        }
                    }
                }
            }
        }
    }

    private static boolean basicSelected(Player player){
        return FormChangeHandler.getInstance().getBasicSelected(player);
    }
    private static boolean voidSelected(Player player){
        return FormChangeHandler.getInstance().getVoidSelected(player);
    }
    private static boolean witherSelected(Player player){
        return FormChangeHandler.getInstance().getWitherSelected(player);
    }
    private static boolean swiftSelected(Player player){
        return FormChangeHandler.getInstance().getSwiftSelected(player);
    }
    private static boolean powerSelected(Player player){
        return FormChangeHandler.getInstance().getPowerSelected(player);
    }


    private static boolean isNearItem(Player player){
        return CommonEventHandler.getInstance().getIsNearItem(player);
    }



    private static boolean isGuiClosed(Player player){
        return !playersWithOpenGui.contains(player.getUUID());
    }

    private static void setBasicFistForm(ServerPlayer player, LevelHandler levelHandler){
        BasicFistItem basicFistItem = BasicFistItem.getInstance();
        BasicFist2Item basicFist2Item = BasicFist2Item.getInstance();
        BasicFist3Item basicFist3Item = BasicFist3Item.getInstance();

        AbstractFormData basicFormData = BasicForm.getInstance().getFormData(player.getUUID());

        if (basicFormData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL1) {
            basicFistItem.setDefaultFormMainHand(player);
        }
        else if (basicFormData.getCurrentFormLevel()  == FormLevelManager.FormLevel.LEVEL2) {
            basicFist2Item.setFormMainHand(player);
        }
        else if (basicFormData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL3) {
            basicFist3Item.setFormMainHand(player);
        }
    }

    private static void setVoidFistForm(ServerPlayer player, LevelHandler levelHandler){
        VoidFistItem voidFistItem = VoidFistItem.getInstance();
        VoidFist2Item voidFist2Item = VoidFist2Item.getInstance();
        VoidFist2Item voidFist3Item = VoidFist2Item.getInstance();

        AbstractFormData voidFormData = VoidForm.getInstance().getFormData(player.getUUID());

        if (voidFormData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL1) {
            voidFistItem.setVoidFormMainHand(player);
        }
        else if (voidFormData.getCurrentFormLevel()  == FormLevelManager.FormLevel.LEVEL2) {
            voidFist2Item.setVoidFormMainHand(player);
        }
        else if (voidFormData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL3) {
            voidFist3Item.setVoidFormMainHand(player);
        }
    }
    private static void setWitherFistForm(ServerPlayer player, LevelHandler levelHandler){
        WitherFistItem witherFistItem = WitherFistItem.getInstance();
        WitherFist2Item witherFist2Item = WitherFist2Item.getInstance();
        WitherFist3Item witherFist3Item = WitherFist3Item.getInstance();

        AbstractFormData witherFormData = WitherForm.getInstance().getFormData(player.getUUID());

        if (witherFormData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL1) {
            witherFistItem.setWitherFormMainHand(player);
        }
        else if (witherFormData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL2) {
            witherFist2Item.setWitherFormMainHand(player);
        }
        else if (witherFormData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL3) {
            witherFist3Item.setWitherFormMainHand(player);
        }
    }
    private static void setSwiftFistForm(ServerPlayer player, LevelHandler levelHandler){
        SwiftFistItem fistItem = SwiftFistItem.getInstance();
        SwiftFist2Item fist2Item = SwiftFist2Item.getInstance();
        SwiftFist3Item fist3Item = SwiftFist3Item.getInstance();

        AbstractFormData swiftFormData = SwiftForm.getInstance().getFormData(player.getUUID());

        if (swiftFormData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL1) {
            fistItem.setFormMainHand(player);
        }
        else if (swiftFormData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL2) {
            fist2Item.setFormMainHand(player);
        }
        else if (swiftFormData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL3) {
            fist3Item.setFormMainHand(player);
        }
    }
    private static void setPowerFistForm(ServerPlayer player, LevelHandler levelHandler){
        PowerFistItem fistItem = PowerFistItem.getInstance();
        PowerFist2Item fist2Item = PowerFist2Item.getInstance();
        PowerFist3Item fist3Item = PowerFist3Item.getInstance();

        AbstractFormData powerFormData = PowerForm.getInstance().getFormData(player.getUUID());

        if (powerFormData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL1) {
            fistItem.setFormMainHand(player);
        }
        else if (powerFormData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL2) {
            fist2Item.setFormMainHand(player);
        }
        else if (powerFormData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL3) {
            fist3Item.setFormMainHand(player);
        }
    }
}