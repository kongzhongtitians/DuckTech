package gd.rf.kongzhongtitian.DuckTech.blocks.machines.essence_furnace;

import gd.rf.kongzhongtitian.DuckTech.api.gui.DTBaseMenu;
import gd.rf.kongzhongtitian.DuckTech.blocks.gui.DTMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class EssenceFurnaceMenu extends DTBaseMenu {
    public final EssenceFurnaceBlockEntity blockEntity;
    private final ContainerData data;

    public EssenceFurnaceMenu( int containerId, Inventory inv, BlockEntity entity , ContainerData data) {
        super(DTMenu.ESSENCE_FURNACE_MENU.get(),containerId, inv, entity);


        addDataSlots(data);
        addPlayerInventory(inv);
        //Player player = (Player) event.getEntity();
        //player.inventory.add
        addPlayerHotbar(inv);
        blockEntity = (EssenceFurnaceBlockEntity) entity;

        this.data = data;

        IItemHandler itemHandler = blockEntity.itemStackHandler;

        this.addSlot(new SlotItemHandler(itemHandler,0,54,34));
        this.addSlot(new SlotItemHandler(itemHandler,1,104,34){
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });

    }
    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public int getScaleArrowProgress(){
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);
        int arrowPixelSize = 24;

        return maxProgress != 0 &&  progress != 0 ? progress * arrowPixelSize / maxProgress : 0;
    }

    public EssenceFurnaceMenu(int containerId, Inventory inventory, FriendlyByteBuf friendlyByteBuf ){
        this(containerId, inventory, ((EssenceFurnaceBlockEntity) inventory.player.level().getBlockEntity(friendlyByteBuf.readBlockPos())), new SimpleContainerData(2));
    }

    @Override
    public int setAdditionSlots() {
        return 2;
    }
}
