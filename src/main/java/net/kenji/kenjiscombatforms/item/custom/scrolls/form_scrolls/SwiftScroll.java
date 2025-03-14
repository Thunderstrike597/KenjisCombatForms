package net.kenji.kenjiscombatforms.item.custom.scrolls.form_scrolls;

import net.kenji.kenjiscombatforms.api.handlers.FormChangeHandler;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.event.sound.SoundManager;
import net.kenji.kenjiscombatforms.item.custom.scrolls.BaseUseItem;
import net.kenji.kenjiscombatforms.network.NetworkHandler;
import net.kenji.kenjiscombatforms.network.fist_forms.form_swap.FormToSwapPacket;
import net.kenji.kenjiscombatforms.screen.form_menu.FormChooseMenu;
import net.kenji.kenjiscombatforms.screen.form_menu.FormSwapMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Consumer;

public class SwiftScroll extends BaseUseItem {
    private static SwiftScroll INSTANCE;
    private static final FormManager formManager = FormManager.getInstance();
    private static final int CHARGE_DURATION = 40; // Ticks required to charge the item
    private static final int MAX_USE_DURATION = 72000; // 1 hour in ticks
    private static final FormManager.FormSelectionOption swiftOption = FormManager.FormSelectionOption.SWIFT;


    public SwiftScroll(Properties properties) {
        super(properties.durability(1));
        if(INSTANCE == null){
            INSTANCE = this;
        }
    }

    public static SwiftScroll getInstance(){
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
        FormManager.PlayerFormData data = formManager.getFormData(player);

        if (data.form1 != swiftOption && data.form2 != swiftOption && data.form3 != swiftOption) {
            return InteractionResultHolder.consume(itemStack);
        }
        return InteractionResultHolder.fail(itemStack);
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
            FormManager.PlayerFormData data = formManager.getFormData(player);

            if (data.form1 != swiftOption && data.form2 != swiftOption && data.form3 != swiftOption) {
                int useTime = getUseDuration(stack) - timeLeft;
                if (useTime >= CHARGE_DURATION) {
                    if (player.level().isClientSide) {
                        SoundManager.playBasicFormChooseSound(player);
                    }
                    FormChangeHandler.getInstance().setFormOption(player, swiftOption);

                    if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == this) {
                        player.getItemInHand(InteractionHand.MAIN_HAND).setCount(0);
                    }
                }
            }
        }
    }
}