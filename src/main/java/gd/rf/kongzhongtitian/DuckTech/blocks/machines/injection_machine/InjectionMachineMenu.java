package gd.rf.kongzhongtitian.DuckTech.blocks.machines.injection_machine;

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

public class InjectionMachineMenu extends DTBaseMenu {
    public final InjectionMachineBlockEntity blockEntity;
    private final ContainerData data;

    public InjectionMachineMenu(int containerId, Inventory inv, BlockEntity entity , ContainerData data) {
        super(DTMenu.INJECTION_MACHINE_MENU.get(),containerId, inv, entity);


        addDataSlots(data);
        addPlayerInventory(inv);
        //Player player = (Player) event.getEntity();
        //player.inventory.add
        addPlayerHotbar(inv);
        blockEntity = (InjectionMachineBlockEntity) entity;

        this.data = data;

        IItemHandler itemHandler = blockEntity.itemStackHandler;

        this.addSlot(new SlotItemHandler(itemHandler,0,49,40));
        this.addSlot(new SlotItemHandler(itemHandler,1,80,18));
        this.addSlot(new SlotItemHandler(itemHandler,2,111,40){
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

    public InjectionMachineMenu(int containerId, Inventory inventory, FriendlyByteBuf friendlyByteBuf ){
        this(containerId, inventory, ((InjectionMachineBlockEntity) inventory.player.level().getBlockEntity(friendlyByteBuf.readBlockPos())), new SimpleContainerData(3));
    }

    @Override
    public int setAdditionSlots() {
        return 3;
    }
}
