package dev.sora.sparky.common.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 * @author icemeowzhi
 * @date 21/8/2022
 * @apiNote
 */
public class ItemHandlerGhost extends SlotItemHandler
{

    public ItemHandlerGhost(IItemHandler itemHandler, int index, int xPosition, int yPosition)
    {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPickup(Player playerIn)
    {
        return false;
    }

}
