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

    // Interface for the capability
    public interface IExtraContainer {
        ItemStack getStoredItem();
        void setStoredItem(ItemStack stack);
    }

    // Implementation with NBT serialization
    public static class ExtraContainerImpl implements IExtraContainer, INBTSerializable<CompoundTag> {
        private ItemStack storedItem = ItemStack.EMPTY;

        @Override
        public ItemStack getStoredItem() {
            return storedItem;
        }

        @Override
        public void setStoredItem(ItemStack stack) {
            this.storedItem = stack;
        }

        // Serialization to NBT
        @Override
        public CompoundTag serializeNBT() {
            CompoundTag nbt = new CompoundTag();
            nbt.put("StoredItem", storedItem.save(new CompoundTag()));
            return nbt;
        }

        // Deserialization from NBT
        @Override
        public void deserializeNBT(CompoundTag nbt) {
            storedItem = ItemStack.of(nbt.getCompound("StoredItem"));
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