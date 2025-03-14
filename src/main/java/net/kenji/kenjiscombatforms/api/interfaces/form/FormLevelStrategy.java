package net.kenji.kenjiscombatforms.api.interfaces.form;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public interface FormLevelStrategy {
    boolean isHoldingForm(ServerPlayer player);
    void gainFormXp(ServerPlayer player, Entity entity);
}
