package net.kenji.kenjiscombatforms.block.entity;

import net.kenji.kenjiscombatforms.recipe.EssenceInfusionRecipe;
import net.kenji.kenjiscombatforms.screen.EssenceInfusingMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class EssenceInfusionBlockEntity extends BlockEntity implements MenuProvider {
    public final ItemStackHandler handler = new ItemStackHandler(5);
    private final int INPUT_SLOT = 0;
    private final int INPUT_SLOT2 = 2;
    private final int INPUT_SLOT3 = 3;

    private final int FUEL_SLOT = 4;
    private final int OUTPUT_SLOT = 1;
    private Player interactPlayer;


    TagKey<Item> fuelItemsTag = ItemTags.create(new ResourceLocation("kenjiscombatforms", "fuel_items"));

    private LazyOptional<IItemHandler> LazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int progress_max = 120;
    int fuelCounter = 0;
    int fuelCounterMax = 8;

    public EssenceInfusionBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(ModBlockEntities.ESSENCE_CHANNELING_STATION_BE.get(), p_155229_, p_155230_);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex){
                    case 0 -> EssenceInfusionBlockEntity.this.progress;
                    case 1 -> EssenceInfusionBlockEntity.this.progress_max;
                    case 2 -> EssenceInfusionBlockEntity.this.fuelCounter;
                    case 3 -> EssenceInfusionBlockEntity.this.fuelCounterMax;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> EssenceInfusionBlockEntity.this.progress = pValue;
                    case 1 -> EssenceInfusionBlockEntity.this.progress_max = pValue;
                    case 2 -> EssenceInfusionBlockEntity.this.fuelCounter = pValue;
                    case 3 -> EssenceInfusionBlockEntity.this.fuelCounterMax = pValue;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };

    }



    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
      if (cap == ForgeCapabilities.ITEM_HANDLER) {
          return LazyItemHandler.cast();
      }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        LazyItemHandler = LazyOptional.of(() -> handler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        LazyItemHandler.invalidate();
    }

    public void drops() {
        SimpleContainer Inventory = new SimpleContainer(handler.getSlots());
        for (int i = 0; i < handler.getSlots(); i ++ ) {
            Inventory.setItem(i, handler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, Inventory);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.essence_infusion_station");
    }


    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainer, Inventory pInventory, Player player) {
        this.interactPlayer = player; // Set the player reference
        return new EssenceInfusingMenu(pContainer, pInventory, this, this.data);
    }

    public Player getInteractPlayer() {
        return interactPlayer;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", handler.serializeNBT());
        pTag.putInt("essence_infusion_station.progress", progress);
        super.saveAdditional(pTag);
    }


    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        handler.deserializeNBT(pTag.getCompound("inventory"));
        progress = pTag.getInt("essence_infusion_station.progress");
}


    public boolean hasPlayedSound = false;












boolean fuelBoolean = false;

ItemStack fuelSlotVar = this.handler.getStackInSlot(FUEL_SLOT);
boolean canContinue = false;

    public void tick(Level level1, BlockPos blockPos, BlockState blockState1) {
            serverSideLogic(level1, blockPos, blockState1);
    }


private void clientSideLogic(Level level){
    if (hasRecipe() && this.handler.getStackInSlot(FUEL_SLOT).getCount() != 0) {
            System.out.println("isTriggeringSoundMethod");
        }
    }

    private void serverSideLogic(Level level1, BlockPos blockPos, BlockState blockState1) {
        if (isCraftingValid()) {
            canContinue = true;
            fuelCounter++;
            if (fuelCounter != fuelCounterMax) {
                fuelBoolean = true;
            }
            if(fuelCounter >= fuelCounterMax) {
                fuelCounter = 0;
                this.handler.extractItem(FUEL_SLOT, 1, false);
            }
            increasaeCraftingProgress();
            hasPlayedSound = true;
            setChanged(level1, blockPos, blockState1);
            if (hasProgressFinished()) {
                craftItem();
                resetProgress();
                hasPlayedSound = false;
            }
        }else {
            resetProgress();
            hasPlayedSound = false;
        }
        // Send packet if the crafting state changed

    }

    private void resetProgress() {
        progress = 0;
    }

    public Boolean isCraftingValid(){
        return hasRecipe() && this.handler.getStackInSlot(FUEL_SLOT).getCount() != 0;
    }


    private void craftItem() {
        Optional<EssenceInfusionRecipe> recipe = getCurrentRecipe();

        ItemStack result = recipe.get().getResultItem(null);
        this.handler.extractItem(INPUT_SLOT, 1, false);
        this.handler.extractItem(INPUT_SLOT2, 1, false);
        this.handler.extractItem(INPUT_SLOT3, 1, false);

        this.handler.setStackInSlot(OUTPUT_SLOT, new ItemStack(result.getItem(),
            this.handler.getStackInSlot(OUTPUT_SLOT).getCount() + result.getCount()));
    }


    Boolean hasFuel(){
        return fuelSlotVar.is(fuelItemsTag);
    }

    Boolean hasFuelAmount(){
        return fuelSlotVar.getCount() == 14;
    }

    public boolean hasProgressFinished() {
            return progress >= progress_max;
    }

    private void increasaeCraftingProgress() {
        progress ++;
    }

    public boolean hasRecipe() {

        Optional<EssenceInfusionRecipe> recipe = getCurrentRecipe();

        if(recipe.isEmpty()) {
            return false;
        }
        ItemStack result = recipe.get().getResultItem(getLevel().registryAccess());

        return canInsertAmountInOutputSlot(result.getCount()) && canInsertItemInOutputSlot(result.getItem());
    }

    private Optional<EssenceInfusionRecipe> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(this.handler.getSlots());
        for(int i = 0; i < handler.getSlots(); i++) {
            inventory.setItem(i, this.handler.getStackInSlot(i));
        }
        return this.level.getRecipeManager().getRecipeFor(EssenceInfusionRecipe.Type.INSTANCE, inventory, level);
    }
    private boolean canInsertItemInOutputSlot(Item item) {
        return this.handler.getStackInSlot(OUTPUT_SLOT).isEmpty() || this.handler.getStackInSlot(OUTPUT_SLOT).is(item);
    }



    private boolean canInsertAmountInOutputSlot(int count) {
        return this.handler.getStackInSlot(OUTPUT_SLOT).getCount() + count <= this.handler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
    }
}
