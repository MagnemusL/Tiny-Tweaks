package net.smazeee.tinytweaks.init.blockentities;

import com.simibubi.create.AllItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.smazeee.tinytweaks.init.ModBlockEntities;
import net.smazeee.tinytweaks.screen.DuperMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DuperBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
                setChanged();
            }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return super.isItemValid(slot, stack);
        }
    };

    public static final int INPUT_ITEM = 0;
    public static final int OUTPUT_ITEM = 1;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int i = 0;

    public DuperBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.DUPER_BE.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> i;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                i = pValue;
            }

            @Override
            public int getCount() {
                    return 0;
                }
        };
    }
    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    public Component getDisplayName() {
            return Component.literal("Duper");
        }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new DuperMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
    }


    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
    }

    @Override
    public CompoundTag serializeNBT() {
        return super.serializeNBT();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, DuperBlockEntity entity) {
        entity.i++;
        Item item = entity.itemHandler.getStackInSlot(0).copy().getItem();
        RecipeWrapper inventory = new RecipeWrapper(entity.itemHandler);
        if(item != Items.AIR) {
            if (!level.isClientSide) {
                dupe(inventory, item, entity, level, pos, state);
            }
        }
    }

    public static void dupe(RecipeWrapper inventory, Item item, DuperBlockEntity entity, Level level, BlockPos pos, BlockState state) {
        setChanged(level, pos, state);
        int x = inventory.getItem(1).getCount();
        if (x <= 0) {
            entity.itemHandler.setStackInSlot(1, new ItemStack(item));
        } else if (entity.itemHandler.getStackInSlot(1).getMaxStackSize() > entity.itemHandler.getStackInSlot(1).getCount()) {
            if(entity.i > 50) {
                entity.i = 0;
                inventory.getItem(1).setCount(inventory.getItem(1).getCount() + 1);
            }
        }
    }
}