package net.kenji.kenjiscombatforms.api.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public class ExtraContainerCapability {
    // Capability token
    public static final Capability<IExtraContainer> EXTRA_CONTAINER_CAP = CapabilityManager.get(new CapabilityToken<>() {});
    public static String storedItem = "storedItem";
    public static String orignalSlot = "extraSlot";


    // Interface for the capability
    public interface IExtraContainer {
        ItemStack getStoredItem();
        int getOriginalSlot();
        void setStoredItem(ItemStack stack);
        void setOriginalSlot(int value);
    }

    // Implementation with NBT serialization
    public static class ExtraContainerImpl implements IExtraContainer, INBTSerializable<CompoundTag> {
        private ItemStack storedItem = ItemStack.EMPTY;
        private int originalSlot = -1;

        @Override
        public ItemStack getStoredItem() {
            return storedItem;
        }

        @Override
        public int getOriginalSlot() {
            return originalSlot;
        }

        @Override
        public void setStoredItem(ItemStack stack) {
            this.storedItem = stack;
        }

        @Override
        public void setOriginalSlot(int value) {
            this.originalSlot = value;
        }

        // Serialization to NBT
        @Override
        public CompoundTag serializeNBT() {
            CompoundTag nbt = new CompoundTag();
            nbt.put(ExtraContainerCapability.storedItem, storedItem.save(new CompoundTag()));
            nbt.putInt(ExtraContainerCapability.orignalSlot, originalSlot);
            return nbt;
        }

        // Deserialization from NBT
        @Override
        public void deserializeNBT(CompoundTag nbt) {
            storedItem = ItemStack.of(nbt.getCompound(ExtraContainerCapability.storedItem));
            originalSlot = nbt.getInt(ExtraContainerCapability.orignalSlot);
        }
    }

    // Provider to attach the capability
    public static class Provider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
        private final ExtraContainerImpl instance = new ExtraContainerImpl();
        private final LazyOptional<IExtraContainer> handler = LazyOptional.of(() -> instance);

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            return ExtraContainerCapability.EXTRA_CONTAINER_CAP.orEmpty(cap, handler.cast());
        }

        @Override
        public CompoundTag serializeNBT() {
            return instance.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            instance.deserializeNBT(nbt);
        }
    }
}