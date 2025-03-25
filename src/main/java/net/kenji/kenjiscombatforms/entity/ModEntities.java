package net.kenji.kenjiscombatforms.entity;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.entity.custom.SenseiEntities.ExiledDevilEntity;
import net.kenji.kenjiscombatforms.entity.custom.SenseiEntities.ExiledSenseiEntity;
import net.kenji.kenjiscombatforms.entity.custom.SenseiEntities.UndeadSenseiEntity;
import net.kenji.kenjiscombatforms.entity.custom.noAiEntities.ShadowPlayerEntity;
import net.kenji.kenjiscombatforms.entity.custom.noAiEntities.EnderEntity;
import net.kenji.kenjiscombatforms.entity.custom.noAiEntities.WitherMinionEntity;
import net.kenji.kenjiscombatforms.entity.custom.noAiEntities.WitherPlayerEntity;
import net.kenji.kenjiscombatforms.entity.custom.traders.ScrollTraderEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITYTYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, KenjisCombatForms.MOD_ID);

    public static final RegistryObject<EntityType<ExiledDevilEntity>> EXILED_DEVIL = ENTITYTYPES.register("exiled_devil", () -> EntityType.Builder.of(ExiledDevilEntity::new, MobCategory.AMBIENT)
            .sized(0.6f, 1.95f).build("exiled_devil"));

    public static final RegistryObject<EntityType<ExiledSenseiEntity>> EXILED_SENSEI = ENTITYTYPES.register("exiled_sensei", () -> EntityType.Builder.of(ExiledSenseiEntity::new, MobCategory.MONSTER)
            .sized(0.6f, 1.95f).build("exiled_sensei"));

    public static final RegistryObject<EntityType<UndeadSenseiEntity>> UNDEAD_SENSEI = ENTITYTYPES.register("undead_sensei", () -> EntityType.Builder.of(UndeadSenseiEntity::new, MobCategory.MONSTER)
            .sized(0.6f, 1.95f).build("undead_sensei"));
    public static final RegistryObject<EntityType<ShadowPlayerEntity>> SHADOW_PLAYER = ENTITYTYPES.register("shadow_player", () -> EntityType.Builder.of(ShadowPlayerEntity::new, MobCategory.MONSTER)
            .sized(0.6f, 1.95f).build("shadow_player"));

    public static final RegistryObject<EntityType<EnderEntity>> ENDER_PLAYER = ENTITYTYPES.register("ender_entity", () -> EntityType.Builder.of(EnderEntity::new, MobCategory.AMBIENT)
            .sized(0.6f, 1.95f).build("ender_entity"));
    public static final RegistryObject<EntityType<WitherPlayerEntity>> WITHER_PLAYER = ENTITYTYPES.register("wither_player", () -> EntityType.Builder.of(WitherPlayerEntity::new, MobCategory.AMBIENT)
            .sized(0.6f, 1.95f).build("wither_player"));


    public static final RegistryObject<EntityType<WitherMinionEntity>> WITHER_MINION = ENTITYTYPES.register("wither_minion", () -> EntityType.Builder.of(WitherMinionEntity::new, MobCategory.AMBIENT)
            .sized(0.6f, 1.95f).build("wither_minion"));


    public static final RegistryObject<EntityType<ScrollTraderEntity>> ABILITY_TRADER = ENTITYTYPES.register("scroll_trader", () -> EntityType.Builder.of(ScrollTraderEntity::new, MobCategory.AMBIENT)
            .sized(0.6f, 1.95f).build("scroll_trader"));



    public static void register(IEventBus eventBus) {
        ENTITYTYPES.register(eventBus);
    }
}
