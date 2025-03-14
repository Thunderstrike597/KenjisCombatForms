package net.kenji.kenjiscombatforms.api.handlers;


import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.interfaces.ability.AbilityDamageGainStrategy;
import net.kenji.kenjiscombatforms.api.interfaces.ability.NoOpDamageGainStrategy;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.api.powers.VoidPowers.EnderFormAbility;
import net.kenji.kenjiscombatforms.api.powers.VoidPowers.VoidAnchorRift;
import net.kenji.kenjiscombatforms.api.powers.WitherPowers.SoulDrift;
import net.kenji.kenjiscombatforms.api.powers.WitherPowers.WitherFormAbility;
import net.kenji.kenjiscombatforms.api.powers.power_powers.PowerEffectInflict;
import net.kenji.kenjiscombatforms.api.powers.swift_powers.SwiftEffectInflict;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GlobalAbilityStrategyHandler {

    private static final GlobalAbilityStrategyHandler INSTANCE = new GlobalAbilityStrategyHandler();

   public static GlobalAbilityStrategyHandler getInstance(){
       return INSTANCE;
   }


    @SubscribeEvent
    public static void onHurtEvent(LivingHurtEvent event){
        if(event.getSource().getEntity() instanceof ServerPlayer player) {
            if (event.getEntity() instanceof Monster || event.getEntity() instanceof Player) {
                FormManager.PlayerFormData formData = FormManager.getInstance().getFormData(player);
                AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getOrCreatePlayerAbilityData(player);
                Entity entity = event.getEntity();
                AbilityDamageGainStrategy ability2Strategy = switch (abilityData.chosenAbility2) {
                    case VOID_ABILITY2 -> new VoidAnchorRift.CurrentDamageGainStrategy();
                    case WITHER_ABILITY2 -> new SoulDrift.CurrentDamageGainStrategy();
                    case SWIFT_ABILITY2 -> new SwiftEffectInflict.CurrentDamageGainStrategy();
                    case POWER_ABILITY2 -> new PowerEffectInflict.CurrentDamageGainStrategy();

                    default -> new NoOpDamageGainStrategy();
                };
                ability2Strategy.fillDamageCooldown(player);

                AbilityDamageGainStrategy ability3Strategy = switch (abilityData.chosenFinal) {
                    case VOID_FINAL -> new EnderFormAbility.CurrentDamageGainStrategy();
                    case WITHER_FINAL -> new WitherFormAbility.CurrentDamageGainStrategy();

                    default -> new NoOpDamageGainStrategy();
                };
                ability3Strategy.fillDamageCooldown(player);
            }
        }
    }
}
