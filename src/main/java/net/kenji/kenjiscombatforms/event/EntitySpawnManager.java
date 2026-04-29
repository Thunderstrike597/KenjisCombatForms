package net.kenji.kenjiscombatforms.event;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.entity.ModEntities;
import net.kenji.kenjiscombatforms.entity.custom.traders.ScrollTraderEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jline.utils.Log;

@Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntitySpawnManager {

    @SubscribeEvent
    public static void attemptSpawnScrollTrader(EntityJoinLevelEvent event){

        if(event.getEntity().getType() != EntityType.WANDERING_TRADER)return;
        if(event.getLevel() instanceof ServerLevel serverLevel) {
            if (ScrollTraderEntity.checkSpawnRules(serverLevel, event.getEntity().blockPosition())) {
                event.setCanceled(true);
                ModEntities.SCROLL_TRADER.get().spawn(serverLevel, event.getEntity().blockPosition(), MobSpawnType.NATURAL);
            }
        }
    }
}
