package net.kenji.kenjiscombatforms.api.basegameassets.condition;

import com.p1nero.invincible.api.skill.ComboNode;
import com.p1nero.invincible.capability.InvincibleCapabilities;
import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.interfaces.condition.ICooldown;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.data.conditions.Condition;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(
        modid = KenjisCombatForms.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.FORGE,
        value = Dist.CLIENT)
public class CooldownCounterCondition implements Condition<PlayerPatch<?>>, ICooldown {
    public final int cooldown;
    public final ComboNode comboAnim;
    public int currentCooldown = 0;

    public static List<CooldownCounterCondition> conditions = new ArrayList<>();

    public CooldownCounterCondition(ComboNode comboNode, int cooldown){
        this.cooldown = cooldown;
        this.comboAnim = comboNode;
        conditions.add(this);
    }
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        for (CooldownCounterCondition iCooldown : conditions){
            iCooldown.depleteCooldown();

            PlayerPatch<?> playerPatch = EpicFightCapabilities.getPlayerPatch(event.player);
            if ((InvincibleCapabilities.getPlayerCap(event.player))
                    .getCurrentDataNode() == iCooldown.comboAnim) {
                iCooldown.resetCooldown();
            }
        }
    }

        public Condition<PlayerPatch<?>> read(CompoundTag compoundTag) {
        return this;
    }

    public CompoundTag serializePredicate() {
        return new CompoundTag();
    }

    public boolean predicate(PlayerPatch<?> playerPatch) {
        return currentCooldown <= 0;
    }

    public List<ParameterEditor> getAcceptingParameters(Screen screen) {
        return null;
    }

    @Override
    public void depleteCooldown() {
        if(currentCooldown > 0)
            currentCooldown--;
    }

    @Override
    public void resetCooldown() {
        currentCooldown = cooldown;
    }
}
