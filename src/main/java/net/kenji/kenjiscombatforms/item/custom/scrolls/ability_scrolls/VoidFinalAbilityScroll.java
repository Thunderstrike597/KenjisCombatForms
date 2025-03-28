package net.kenji.kenjiscombatforms.item.custom.scrolls.ability_scrolls;

import net.kenji.kenjiscombatforms.api.handlers.AbilityChangeHandler;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.api.managers.forms.VoidForm;
import net.kenji.kenjiscombatforms.api.interfaces.form.FormAbilityStrategy;
import net.kenji.kenjiscombatforms.api.powers.VoidPowers.EnderFormAbility;
import net.kenji.kenjiscombatforms.api.powers.power_powers.StrengthBoost;
import net.kenji.kenjiscombatforms.event.sound.SoundManager;
import net.kenji.kenjiscombatforms.item.custom.scrolls.BaseUseItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Objects;
import java.util.function.Consumer;

public class VoidFinalAbilityScroll extends BaseUseItem {
    private static VoidFinalAbilityScroll INSTANCE;
    private static final int CHARGE_DURATION = 40; // Ticks required to charge the item
    private static final int MAX_USE_DURATION = 72000; // 1 hour in ticks
    private final String ability = EnderFormAbility.getInstance().getName();
    public VoidFinalAbilityScroll(Properties properties) {
        super(properties.durability(2));
        if(INSTANCE == null){
            INSTANCE = this;
        }
    }

    private void setFormAbility(Player player){
        if(player instanceof ServerPlayer serverPlayer) {
                FormAbilityStrategy formAbilityStrategy;
                formAbilityStrategy = new VoidForm.CurrentFormAbilityStrategy();
                AbilityChangeHandler.getInstance().setFormAbility3(serverPlayer);

                formAbilityStrategy.setLearnedFinalAbility(player, ability, true);
            }
    }


    public static VoidFinalAbilityScroll getInstance(){
        return INSTANCE;
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return MAX_USE_DURATION;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return true;
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return super.damageItem(stack, amount, entity, onBroken);
    }

    @Override
    public boolean canBeDepleted() {
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(itemStack);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseDuration) {
        if (entity instanceof Player player) {
            int useTime = getUseDuration(stack) - remainingUseDuration;
            // You can add visual or sound effects here to indicate charging
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (entity instanceof Player player) {
            int useTime = getUseDuration(stack) - timeLeft;
            FormManager.PlayerFormData formData = FormManager.getInstance().getFormData(player);

            if (Objects.equals(formData.selectedForm, VoidForm.getInstance().getName())) {
                if (useTime >= CHARGE_DURATION) {


                    setFormAbility(player);
                    if (player.level().isClientSide) {
                        SoundManager.playBasicFormChooseSound(player);
                    }
                    this.setDamage(stack, this.getDamage(stack) + 1);
                    if (this.getDamage(stack) == this.getMaxDamage(stack)) {
                        if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == this) {
                            player.getItemInHand(InteractionHand.MAIN_HAND).setCount(0);
                        }
                    }
                }
            }
        }
    }
}