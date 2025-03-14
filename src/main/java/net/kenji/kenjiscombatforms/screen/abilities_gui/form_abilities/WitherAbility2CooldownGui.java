package net.kenji.kenjiscombatforms.screen.abilities_gui.form_abilities;

import com.mojang.blaze3d.systems.RenderSystem;
import net.kenji.kenjiscombatforms.KenjisCombatForms;
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
public class WitherAbility2CooldownGui {


    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGuiOverlayEvent event) {


        int abilityCooldown = ClientWitherData.getCooldown2();
        int ability5Cooldown = ClientWitherData.getImplodeCooldown();

        Minecraft mcInstance = Minecraft.getInstance();
        Player player = mcInstance.player;


        ResourceLocation abilityResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/wither_abilities/wither_ability2_gui.png");
        ResourceLocation abilityBgResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/wither_abilities/wither_ability2_bg-gui.png");
        ResourceLocation abilityOverlayResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/wither_abilities/wither_ability2_gui_overlay.png");

        ResourceLocation ability5Resource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/second_abilities/wither_implode_gui.png");
        ResourceLocation ability5BgResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/second_abilities/wither_implode_bg-gui.png");
        ResourceLocation ability5OverlayResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/second_abilities/wither_implode_gui_overlay.png");

        ResourceLocation simpleAbilityResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/simple_ability_bars.png");


        ResourceLocation emptyResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/item/forms/fist.png");

        boolean isWitherActive = ClientWitherData.getIsWitherActive();
        boolean isEnderActive = ClientVoidData.getIsEnderActive();
        int currentImplodeCooldown = ClientWitherData.getImplodeCooldown();
        int imageWidth = 100;
        int imageHeight = 50;

        int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        int abilityX = screenWidth - imageWidth;
        int abilityY = screenHeight - imageHeight;


        if (player != null) {

            WitherPlayerDataSets dataSets = WitherPlayerDataSets.getInstance();
            WitherPlayerDataSets.SoulDriftPlayerData sData = dataSets.getOrCreateSoulDriftPlayerData(player);
            WitherPlayerDataSets.WitherImplodePlayerData iData = dataSets.getOrCreateWitherImplodePlayerData(player);

            int witherAbility2FullHeight = 34;
            int witherAbility2MaxCooldown = sData.getMAX_COOLDOWN();
            int witherAbility2ElapsedCooldown = witherAbility2MaxCooldown - abilityCooldown;
            float witherAbility2CooldownProgress = (float) witherAbility2ElapsedCooldown / witherAbility2MaxCooldown;
            int abilityBarHeight = (int) (witherAbility2FullHeight * witherAbility2CooldownProgress);

            int abilityU = 0;
            int abilityV = 34;
            int abilityWidth = 37;
            int abilityHeight = 34;


            int witherImplodeFullHeight = 34;
            int witherImplodeMaxCooldown = iData.getMAX_COOLDOWN();
            int witherImplodeElapsedCooldown = witherImplodeMaxCooldown - currentImplodeCooldown;
            float witherImplodeCooldownProgress = (float) witherImplodeElapsedCooldown / witherImplodeMaxCooldown;
            int ability5BarHeight = (int) (witherImplodeFullHeight * witherImplodeCooldownProgress);

            int simpleAbilityU = 0;
            int simpleAbilityV = 0;
            int simpleAbilityWidth = 107;
            int simpleAbilityHeight = 5;
            int simpleAbilityWidth2 = 5;
            int simpleAbilityHeight2 = 62;

            int voidAbilityFullHeight2 = 62;
            int witherAbility5MaxCooldown = iData.getMAX_COOLDOWN();

            int abilitySimpleMaxCooldown = sData.getMAX_COOLDOWN();
            float abilitySimpleElapsedCooldown = abilitySimpleMaxCooldown - abilityCooldown;
            float ability1SimpleCooldownProgress = (float) abilitySimpleElapsedCooldown / abilitySimpleMaxCooldown;
            int abilitySimpleBarWidth = (int) (simpleAbilityWidth * ability1SimpleCooldownProgress);




            float voidAbilityElapsedCooldown2 = witherAbility5MaxCooldown - ability5Cooldown;
            float voidAbilityCooldownProgress2 = (float) voidAbilityElapsedCooldown2 / witherAbility5MaxCooldown;
            int simpleBarHeight2 = (int) (voidAbilityFullHeight2 * voidAbilityCooldownProgress2);


            boolean areFinalsActive = isWitherActive || isEnderActive;


            if (AbilityManager.getInstance().getPlayerAbilityData(player).chosenAbility2 == AbilityManager.AbilityOption2.WITHER_ABILITY2 ||
                    ClientFistData.getChosenAbility2() == AbilityManager.AbilityOption2.WITHER_ABILITY2) {
                if (!areFinalsActive) {
                    SimpleAbilityBarHandler.drawAbility2Icon(event, false, abilityCooldown, abilityCooldown, emptyResource, simpleAbilityResource, emptyResource, emptyResource, emptyResource, emptyResource, abilityX, abilityY, simpleAbilityU, simpleAbilityV, simpleAbilityWidth, simpleAbilityHeight, abilitySimpleBarWidth, simpleAbilityHeight);
                }
            }
            if (isWitherActive) {
                SimpleAbilityBarHandler.drawAbility5Icon(event, simpleAbilityResource, abilityX, abilityY, simpleAbilityU, simpleAbilityV, simpleAbilityWidth2, simpleAbilityHeight2, simpleBarHeight2);
            }
        }
    }
}
