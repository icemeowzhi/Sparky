package dev.sora.sparky.common.block.entity;

import dev.sora.sparky.Sparky;
import dev.sora.sparky.common.block.BlockInitializer;
import dev.sora.sparky.common.inventory.ModChestMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * @author icemeowzhi
 * @date 20/8/2022
 * @apiNote
 */
public class ModChestBlockEntity extends RandomizableContainerBlockEntity implements LidBlockEntity {

    public RecordSlotInv recordSlotInv = new RecordSlotInv(this);
    private NonNullList<ItemStack> items;

    private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
        protected void onOpen(Level level, BlockPos pos, BlockState blockState) {
            ModChestBlockEntity.playSound(level, pos, blockState, SoundEvents.CHEST_OPEN);
        }

        protected void onClose(Level level, BlockPos pos, BlockState blockState) {
            ModChestBlockEntity.playSound(level, pos, blockState, SoundEvents.CHEST_CLOSE);
        }

        protected void openerCountChanged(Level level, BlockPos pos, BlockState blockState, int previousCount, int newCount) {
            ModChestBlockEntity.this.signalOpenCount(level, pos, blockState, previousCount, newCount);
        }

        protected boolean isOwnContainer(Player player) {
            if (!(player.containerMenu instanceof ModChestMenu)) {
                return false;
            } else {
                Container container = ((ModChestMenu) player.containerMenu).getContainer();
                return container instanceof ModChestBlockEntity || container instanceof CompoundContainer && ((CompoundContainer) container).contains(ModChestBlockEntity.this);
            }
        }
    };

    private final ChestLidController chestLidController = new ChestLidController();

    private final Supplier<Block> blockToUse;

    public ModChestBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityInitializer.MOD_CHEST.get(), blockPos, blockState);

        this.items = NonNullList.<ItemStack>withSize(55, ItemStack.EMPTY);
        this.blockToUse = BlockInitializer.MOD_CHEST_BLOCK::get;
    }

    @Override
    public int getContainerSize() {
        return this.getItems().size();
    }

    @Override
    protected Component getDefaultName() {
        return MutableComponent.create(new TranslatableContents(Sparky.MODID + ".container.mod_chest"));
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
        return ModChestMenu.createContainer(containerId, playerInventory, this,this);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);

        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);

        if (!this.tryLoadLootTable(compoundTag)) {
            ContainerHelper.loadAllItems(compoundTag, this.items);
        }

        CompoundTag recordSlotTag = (CompoundTag) compoundTag.get("recordSlot");
        recordSlotInv.setItem(0,ItemStack.of(recordSlotTag));
    }

    @Override
    public void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);

        if (!this.trySaveLootTable(compoundTag)) {
            ContainerHelper.saveAllItems(compoundTag, this.items);
        }

        CompoundTag recordSlotTag = new CompoundTag();
        recordSlotInv.getItem(0).save(recordSlotTag);
        compoundTag.put("recordSlot",recordSlotTag);
    }

    public static void lidAnimateTick(Level level, BlockPos blockPos, BlockState blockState, ModChestBlockEntity chestBlockEntity) {
        chestBlockEntity.chestLidController.tickLid();
    }

    static void playSound(Level level, BlockPos blockPos, BlockState blockState, SoundEvent soundEvent) {
        double d0 = (double) blockPos.getX() + 0.5D;
        double d1 = (double) blockPos.getY() + 0.5D;
        double d2 = (double) blockPos.getZ() + 0.5D;

        level.playSound(null, d0, d1, d2, soundEvent, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    public boolean triggerEvent(int id, int type) {
        if (id == 1) {
            this.chestLidController.shouldBeOpen(type > 0);
            return true;
        } else {
            return super.triggerEvent(id, type);
        }
    }

    @Override
    public void startOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.openersCounter.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    @Override
    public void stopOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.openersCounter.decrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    public void setItems(NonNullList<ItemStack> itemsIn) {
        this.items = NonNullList.<ItemStack>withSize(55, ItemStack.EMPTY);

        for (int i = 0; i < itemsIn.size(); i++) {
            if (i < this.items.size()) {
                this.getItems().set(i, itemsIn.get(i));
            }
        }
    }

    @Override
    public float getOpenNess(float partialTicks) {
        return this.chestLidController.getOpenness(partialTicks);
    }

    public static int getOpenCount(BlockGetter blockGetter, BlockPos blockPos) {
        BlockState blockstate = blockGetter.getBlockState(blockPos);

        if (blockstate.hasBlockEntity()) {
            BlockEntity blockentity = blockGetter.getBlockEntity(blockPos);

            if (blockentity instanceof ModChestBlockEntity) {
                return ((ModChestBlockEntity) blockentity).openersCounter.getOpenerCount();
            }
        }

        return 0;
    }

    public void recheckOpen() {
        if (!this.remove) {
            this.openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    protected void signalOpenCount(Level level, BlockPos blockPos, BlockState blockState, int previousCount, int newCount) {
        Block block = blockState.getBlock();
        level.blockEvent(blockPos, block, 1, newCount);
    }

    public void wasPlaced(@Nullable LivingEntity livingEntity, ItemStack stack) {}

    public Block getBlockToUse() {
        return this.blockToUse.get();
    }

    public static class RecordSlotInv implements Container{


        public NonNullList<ItemStack> inv = NonNullList.withSize(1,ItemStack.EMPTY);
        final ModChestBlockEntity blockEntity;

        public RecordSlotInv(ModChestBlockEntity blockEntity) {
            this.blockEntity = blockEntity;
        }

        /**
         * Returns the number of slots in the inventory.
         */
        @Override
        public int getContainerSize() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return inv.isEmpty();
        }

        /**
         * Returns the stack in the given slot.
         *
         * @param pSlot
         */
        @Override
        public ItemStack getItem(int pSlot) {
            return inv.get(0);
        }

        /**
         * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
         *
         * @param pSlot
         * @param pAmount
         */
        @Override
        public ItemStack removeItem(int pSlot, int pAmount) {
            ItemStack stack = getItem(0);
            if(!stack.isEmpty())
                if(stack.getCount() <= pAmount)
                    setItem(0, ItemStack.EMPTY);
                else
                {
                    stack = stack.split(pAmount);
                    if(stack.getCount()==0)
                        setItem(0, ItemStack.EMPTY);
                }
            return stack;
        }

        /**
         * Removes a stack from the given slot and returns it.
         *
         * @param pSlot
         */
        @Override
        public ItemStack removeItemNoUpdate(int pSlot) {
            ItemStack stack = getItem(0);
            if(!stack.isEmpty())
                setItem(0, ItemStack.EMPTY);
            return stack;
        }

        /**
         * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
         *
         * @param pSlot
         * @param pStack
         */
        @Override
        public void setItem(int pSlot, ItemStack pStack) {
            inv.set(0,pStack);
            if(!pStack.isEmpty()&&pStack.getCount() > getMaxStackSize())
                pStack.setCount(getMaxStackSize());
        }

        /**
         * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think it
         * hasn't changed and skip it.
         */
        @Override
        public void setChanged() {
            if (blockEntity!=null){
                this.blockEntity.setChanged();
            }
        }

        /**
         * Don't rename this method to canInteractWith due to conflicts with Container
         *
         * @param pPlayer
         */
        @Override
        public boolean stillValid(Player pPlayer) {
            return true;
        }

        @Override
        public void clearContent() {
            inv.set(0,ItemStack.EMPTY);
        }

        /**
         * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
         */
        @Override
        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public void startOpen(Player pPlayer) {
        }

        @Override
        public void stopOpen(Player pPlayer) {
        }

        /**
         * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
         * guis use Slot.isItemValid
         *
         * @param pIndex
         * @param pStack
         */
        @Override
        public boolean canPlaceItem(int pIndex, ItemStack pStack) {
            return true;
        }
    }
}
