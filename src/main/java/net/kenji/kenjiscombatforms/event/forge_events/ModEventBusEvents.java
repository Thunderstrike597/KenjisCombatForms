package net.kenji.kenjiscombatforms.event.forge_events;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.entity.ModEntities;
import net.kenji.kenjiscombatforms.entity.custom.SenseiEntities.ExiledDevilEntity;
import net.kenji.kenjiscombatforms.entity.custom.SenseiEntities.ExiledSenseiEntity;
import net.kenji.kenjiscombatforms.entity.custom.SenseiEntities.UndeadSenseiEntity;
import net.kenji.kenjiscombatforms.entity.custom.noAiEntities.ShadowPlayerEntity;
import net.kenji.kenjiscombatforms.entity.custom.noAiEntities.EnderEntity;
import net.kenji.kenjiscombatforms.entity.custom.noAiEntities.WitherMinionEntity;
import net.kenji.kenjiscombatforms.entity.custom.noAiEntities.WitherPlayerEntity;
import net.kenji.kenjiscombatforms.entity.custom.traders.ScrollTraderEntity;
import net.kenji.kenjiscombatforms.particles.ModParticles;
import net.kenji.kenjiscombatforms.particles.VoidRiftParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public class ModEventBusEvents {




        @SubscribeEvent
        public static void registerAttributes(EntityAttributeCreationEvent event) {
            event.put(ModEntities.EXILED_DEVIL.get(), ExiledDevilEntity.createMobAttributes().build());
            event.put(ModEntities.EXILED_SENSEI.get(), ExiledSenseiEntity.createMobAttributes().build());
            event.put(ModEntities.UNDEAD_SENSEI.get(), UndeadSenseiEntity.createMobAttributes().build());
            event.put(ModEntities.SHADOW_PLAYER.get(), ShadowPlayerEntity.createMobAttributes().build());
            event.put(ModEntities.ENDER_PLAYER.get(), EnderEntity.createMobAttributes().build());
            event.put(ModEntities.WITHER_PLAYER.get(), WitherPlayerEntity.createMobAttributes().build());
            event.put(ModEntities.WITHER_MINION.get(), WitherMinionEntity.createMobAttributes().build());

            event.put(ModEntities.SCROLL_TRADER.get(), ScrollTraderEntity.createMobAttributes().build());
        }

        @SubscribeEvent
        public static void existingEntityAttributes(EntityAttributeModificationEvent event){

               event.add(ModEntities.EXILED_DEVIL.get(), Attributes.MAX_HEALTH, 140);
               event.add(ModEntities.EXILED_DEVIL.get(), Attributes.ATTACK_DAMAGE, 4.5);

               event.add(ModEntities.EXILED_SENSEI.get(), Attributes.MAX_HEALTH, 80);
               event.add(ModEntities.EXILED_SENSEI.get(), Attributes.ATTACK_DAMAGE, 4);

               event.add(ModEntities.UNDEAD_SENSEI.get(), Attributes.MAX_HEALTH, 40);
               event.add(ModEntities.UNDEAD_SENSEI.get(), Attributes.ATTACK_DAMAGE, 3);

               event.add(ModEntities.SCROLL_TRADER.get(), Attributes.MAX_HEALTH, 85);
               event.add(ModEntities.SCROLL_TRADER.get(), Attributes.ATTACK_DAMAGE, 7);


            event.add(ModEntities.WITHER_MINION.get(), Attributes.MAX_HEALTH, 25);
            event.add(ModEntities.WITHER_MINION.get(), Attributes.ATTACK_DAMAGE, 2.5);

            event.add(ModEntities.ENDER_PLAYER.get(), Attributes.MAX_HEALTH, 50);
            event.add(ModEntities.ENDER_PLAYER.get(), Attributes.ATTACK_DAMAGE, 4);

            event.add(ModEntities.WITHER_PLAYER.get(), Attributes.MAX_HEALTH, 50);
            event.add(ModEntities.WITHER_PLAYER.get(), Attributes.ATTACK_DAMAGE, 6);

        }


        @SubscribeEvent
        @OnlyIn(Dist.CLIENT)
        public static void registerParticleFactory(final RegisterParticleProvidersEvent event){
            Minecraft.getInstance().particleEngine.register(ModParticles.VOID_RIFT_PARTICLE.get(),
                    VoidRiftParticle.Provider::new);
        }

}
