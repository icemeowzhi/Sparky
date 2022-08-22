package dev.sora.sparky.common.inventory;

import dev.sora.sparky.common.block.BlockInitializer;
import dev.sora.sparky.common.block.entity.ModChestBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * @author icemeowzhi
 * @date 20/8/2022
 * @apiNote
 */
public class ModChestMenu extends AbstractContainerMenu {

    public static final int RECORD_SLOT_ID = 54;

    private final Container container;
    private final BlockEntity blockEntity;
    public final Slot recordSlot;

    protected ModChestMenu(@Nullable MenuType<?> menuType, int containerId, Inventory playerInventory, Container inventory, BlockEntity blockEntity) {
        super(menuType, containerId);

        checkContainerSize(inventory, 54);

        this.container = inventory;
        this.blockEntity = blockEntity;

        inventory.startOpen(playerInventory.player);

        for (int chestRow = 0; chestRow < 6; chestRow++) {
            for (int chestCol = 0; chestCol < 9; chestCol++) {
                this.addSlot(new Slot(inventory, chestCol + chestRow * 9, 8 + chestCol * 18, 18 + chestRow * 18){
                    /**
                     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
                     *
                     * @param pStack
                     */
                    @Override
                    public boolean mayPlace(ItemStack pStack) {
                        boolean isSameModid = Objects.equals(pStack.getItem().getCreatorModId(pStack), recordSlot.getItem().getItem().getCreatorModId(recordSlot.getItem()));
                        boolean isRecordEmpty = recordSlot.getItem().isEmpty();
                        if (!isRecordEmpty && !isSameModid && blockEntity.getLevel()!=null){
                            ModChestBlockEntity.playSound(blockEntity.getLevel(),blockEntity.getBlockPos(),blockEntity.getBlockState(), SoundEvents.EXPERIENCE_ORB_PICKUP);
                            return false;
                        }
                        return super.mayPlace(pStack);
                    }
                });
            }
        }

        for (int playerInvRow = 0; playerInvRow < 3; playerInvRow++) {
            for (int playerInvCol = 0; playerInvCol < 9; playerInvCol++) {
                this.addSlot(new Slot(playerInventory, playerInvCol + playerInvRow * 9 + 9, 8 + playerInvCol * 18, 140 + playerInvRow * 18));
            }

        }

        for (int hotHarSlot = 0; hotHarSlot < 9; hotHarSlot++) {
            this.addSlot(new Slot(playerInventory, hotHarSlot, 8 + hotHarSlot * 18, 198));
        }

        recordSlot = new ItemHandlerGhost(new InvWrapper(((ModChestBlockEntity)blockEntity).recordSlotInv),0,180,30);
        this.addSlot(recordSlot);
    }

    public static ModChestMenu createContainer(int containerId, Inventory playerInventory) {
        return new ModChestMenu(ContainerTypeInitializer.MOD_CHEST.get(),containerId,playerInventory,new SimpleContainer(55),new ModChestBlockEntity(BlockPos.ZERO, BlockInitializer.MOD_CHEST_BLOCK.get().defaultBlockState()));
    }

    public static ModChestMenu createContainer(int containerId, Inventory playerInventory, Container inventory, BlockEntity blockEntity) {
        return new ModChestMenu(ContainerTypeInitializer.MOD_CHEST.get(),containerId,playerInventory,inventory,blockEntity);
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     *
     * @param player
     * @param index
     */
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem() && !(slot instanceof ItemHandlerGhost)) {
            ItemStack stackInSlot = slot.getItem();
            itemstack = stackInSlot.copy();

            if (index < 54) {
                if (!this.moveItemStackTo(stackInSlot, 54, this.slots.size()-1, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.moveItemStackTo(stackInSlot, 0, 54, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stackInSlot.getCount()==0) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if(stackInSlot.getCount()==itemstack.getCount())
                return ItemStack.EMPTY;
            slot.onTake(player, stackInSlot);
        }

        return itemstack;
    }

    /**
     * Determines whether supplied player can use this container
     *
     * @param pPlayer
     */
    @Override
    public boolean stillValid(Player pPlayer) {
        return this.container.stillValid(pPlayer);
    }

    @Override
    public void removed(Player playerIn) {
        super.removed(playerIn);
        this.container.stopOpen(playerIn);
    }


    public Container getContainer() {
        return this.container;
    }

    // Basically an IE-assembler-copy solution.
    @Override
    public void clicked(int id, int dragType, ClickType clickType, Player player) {

        Slot slot = id < 0?null: this.slots.get(id);
        if(!(slot instanceof ItemHandlerGhost))
        {
            super.clicked(id, dragType, clickType, player);
            return;
        }


        ItemStack stackSlot = slot.getItem();

        if(dragType==2)
            slot.set(ItemStack.EMPTY);
        else if(dragType==0||dragType==1)
        {
            ItemStack stackHeld = getCarried();
            int amount = Math.min(slot.getMaxStackSize(), stackHeld.getCount());
            if(dragType==1)
                amount = 1;
            if(stackSlot.isEmpty())
            {
                if(!stackHeld.isEmpty()&&slot.mayPlace(stackHeld))
                    slot.set(ItemHandlerHelper.copyStackWithSize(stackHeld, amount));
            }
            else if(stackHeld.isEmpty())
            {
                slot.set(ItemStack.EMPTY);
            }
            else if(slot.mayPlace(stackHeld))
            {
                if(ItemStack.isSame(stackSlot, stackHeld))
                    stackSlot.grow(amount);
                else
                    slot.set(ItemHandlerHelper.copyStackWithSize(stackHeld, amount));
            }
            if(stackSlot.getCount() > slot.getMaxStackSize())
                stackSlot.setCount(slot.getMaxStackSize());
        }
        else if(dragType==5)
        {
            ItemStack stackHeld = getCarried();
            int amount = Math.min(slot.getMaxStackSize(), stackHeld.getCount());
            if(!slot.hasItem())
            {
                slot.set(ItemHandlerHelper.copyStackWithSize(stackHeld, amount));
            }
        }
        if (blockEntity!=null){
            slot.setChanged();
        }
    }
}
