package net.kenji.kenjiscombatforms.screen.abilities_gui.form_abilities_new;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.handlers.power_data.EnderPlayerDataSets;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.managers.client_data.ClientFistData;
import net.kenji.kenjiscombatforms.network.voidform.ClientVoidData;
import net.kenji.kenjiscombatforms.network.witherform.ClientWitherData;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, value = Dist.CLIENT)
public class NewVoidAbility2CooldownGui {

    private static final SimpleAbilityHandler simpleAbilityHandler = new SimpleAbilityHandler();


    private static float prevCooldown = 0f;
    private static float currentCooldown = 0f;

    public static void updateCooldown() {
        prevCooldown = currentCooldown;
        currentCooldown = (float) ClientVoidData.getCooldown();
    }


    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGuiOverlayEvent event) {


        Minecraft mcInstance = Minecraft.getInstance();
        Player player = mcInstance.player;


        ResourceLocation abilityBackgroundResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/ability2_gui1.png");
        ResourceLocation abilityResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/ability2_gui2.png");
        ResourceLocation abilityOverlayResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/ability2_gui3.png");

        ResourceLocation ability4BackgroundResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/ability5_gui1.png");
        ResourceLocation ability4Resource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/ability5_gui2.png");
        ResourceLocation ability4OverlayResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/ability5_gui3.png");


        if (player != null) {



            EnderPlayerDataSets dataSets = EnderPlayerDataSets.getInstance();
            EnderPlayerDataSets.VoidRiftPlayerData abilityData = dataSets.getOrCreateVoidRiftPlayerData(player);
            EnderPlayerDataSets.VoidGrabPlayerData gData = dataSets.getOrCreateVoidGrabPlayerData(player);


            int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

            int imageWidth = 128;
            int imageHeight = 128;
            int abilityX = screenWidth - imageWidth;
            int abilityY = screenHeight - imageHeight;



            int abilityCooldown = ClientVoidData.getCooldown2();
            int ability5Cooldown = ClientVoidData.getGrabCooldown();
            boolean isWitherActive = ClientWitherData.getIsWitherActive();
            boolean isEnderActive = ClientVoidData.getIsEnderActive();

            int abilityFullHeight = 38;  // Maximum cooldown bar height
            int abilityMaxCooldown = abilityData.getMAX_COOLDOWN();  // Total cooldown time
            float abilityElapsedCooldown = abilityMaxCooldown - abilityCooldown;  // Remaining cooldown
            float abilityCooldownProgress = abilityElapsedCooldown / (float) abilityMaxCooldown;  // Normalize to 0-1
            int abilityBarHeight = (int) (abilityFullHeight * abilityCooldownProgress);


            int ability5FullHeight = 38;  // Maximum cooldown bar height
            int ability5MaxCooldown = gData.getMAX_COOLDOWN();  // Total cooldown time
            float ability5ElapsedCooldown = ability5MaxCooldown - ability5Cooldown;  // Remaining cooldown
            float ability5CooldownProgress = ability5ElapsedCooldown / (float) ability5MaxCooldown;  // Normalize to 0-1
            int ability5BarHeight = (int) (ability5FullHeight * ability5CooldownProgress);

            int voidAbility5MaxCooldown = gData.getMAX_COOLDOWN();
            int maxCooldown = abilityData.getMAX_COOLDOWN();


            int simpleAbilityU = 0;
            int simpleAbilityV = 0;
            int simpleAbilityWidth = 38;
            int simpleAbilityHeight = 38;
            int iconX = abilityX + 50;
            int iconY = abilityY + 32;
            boolean areFinalsActive = isWitherActive || isEnderActive;



            if (AbilityManager.getInstance().getPlayerAbilityData(player).chosenAbility2 == AbilityManager.AbilityOption2.VOID_ABILITY2 ||
                    ClientFistData.getChosenAbility2() == AbilityManager.AbilityOption2.VOID_ABILITY2) {

                if (!areFinalsActive) {
                    simpleAbilityHandler.drawAbility2Icon(event,  abilityResource, abilityBackgroundResource, abilityOverlayResource, iconX, iconY, simpleAbilityU, simpleAbilityV, simpleAbilityWidth, simpleAbilityHeight, abilityBarHeight, maxCooldown, abilityCooldown);
                }
            }
            if (isEnderActive) {
                simpleAbilityHandler.drawAbility5Icon(event,  ability4Resource, ability4BackgroundResource, ability4OverlayResource, iconX, iconY, simpleAbilityU, simpleAbilityV, simpleAbilityWidth, simpleAbilityHeight, ability5BarHeight, voidAbility5MaxCooldown, ability5Cooldown);
            }
        }
    }
}
