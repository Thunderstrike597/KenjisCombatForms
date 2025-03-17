package net.kenji.kenjiscombatforms.screen.abilities_gui.form_abilities_new;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.handlers.power_data.EnderPlayerDataSets;
import net.kenji.kenjiscombatforms.api.handlers.power_data.WitherPlayerDataSets;
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
public class NewWitherAbility1CooldownGui {

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


        ResourceLocation abilityBackgroundResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/ability1_gui1.png");
        ResourceLocation abilityResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/ability1_gui2.png");
        ResourceLocation abilityOverlayResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/ability1_gui3.png");

        ResourceLocation ability4BackgroundResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/ability4_gui1.png");
        ResourceLocation ability4Resource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/ability4_gui2.png");
        ResourceLocation ability4OverlayResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/ability4_gui3.png");


        if (player != null) {


            WitherPlayerDataSets dataSets = WitherPlayerDataSets.getInstance();
            WitherPlayerDataSets.WitherDashPlayerData abilityData = dataSets.getOrCreateDashPlayerData(player);
            WitherPlayerDataSets.WitherMinionPlayerData mData = dataSets.getOrCreateMinionPlayerData(player);


            int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

            int imageWidth = 128;
            int imageHeight = 128;
            int abilityX = screenWidth - imageWidth;
            int abilityY = screenHeight - imageHeight;



            int abilityCooldown = ClientWitherData.getCooldown();
            int ability4Cooldown = ClientWitherData.getMinionCooldown();

            boolean areMinionsActive = ClientWitherData.getMinionsActive();
            boolean isWitherActive = ClientWitherData.getIsWitherActive();
            boolean isEnderActive = ClientVoidData.getIsEnderActive();

            int abilityFullHeight = 32;  // Maximum cooldown bar height
            int abilityMaxCooldown = abilityData.getMAX_COOLDOWN();  // Total cooldown time
            float abilityElapsedCooldown = abilityMaxCooldown - abilityCooldown;  // Remaining cooldown
            float abilityCooldownProgress = abilityElapsedCooldown / (float) abilityMaxCooldown;  // Normalize to 0-1
            int abilityBarHeight = (int) (abilityFullHeight * abilityCooldownProgress);


            int ability4FullHeight = 32;  // Maximum cooldown bar height
            int ability4MaxCooldown = mData.getMAX_COOLDOWN();  // Total cooldown time
            float ability4ElapsedCooldown = ability4MaxCooldown - ability4Cooldown;  // Remaining cooldown
            float ability4CooldownProgress = ability4ElapsedCooldown / (float) ability4MaxCooldown;  // Normalize to 0-1
            int ability4BarHeight = (int) (ability4FullHeight * ability4CooldownProgress);

            int maxCooldown = abilityData.getMAX_COOLDOWN();


            int simpleAbilityU = 0;
            int simpleAbilityV = 0;
            int simpleAbilityWidth = 32;
            int simpleAbilityHeight = 32;
            int iconX = abilityX + 30;
            int iconY = abilityY + 50;

            boolean areFinalsActive = isWitherActive || isEnderActive;



            if (AbilityManager.getInstance().getPlayerAbilityData(player).chosenAbility1 == AbilityManager.AbilityOption1.WITHER_ABILITY1 ||
                    ClientFistData.getChosenAbility1() == AbilityManager.AbilityOption1.WITHER_ABILITY1) {

                if (!areFinalsActive) {
                    simpleAbilityHandler.drawAbility1Icon(event,  abilityResource, abilityBackgroundResource, abilityOverlayResource, iconX, iconY, simpleAbilityU, simpleAbilityV, simpleAbilityWidth, simpleAbilityHeight, abilityBarHeight, maxCooldown, abilityCooldown);
                }
            }
            if (isWitherActive || areMinionsActive) {
                simpleAbilityHandler.drawAbility4Icon(event,  ability4Resource, ability4BackgroundResource, ability4OverlayResource, iconX, iconY, simpleAbilityU, simpleAbilityV, simpleAbilityWidth, simpleAbilityHeight, ability4BarHeight, ability4MaxCooldown, ability4Cooldown);
            }
        }
    }
}
