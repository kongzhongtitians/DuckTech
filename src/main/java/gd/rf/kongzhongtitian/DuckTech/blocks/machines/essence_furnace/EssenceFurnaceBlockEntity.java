package gd.rf.kongzhongtitian.DuckTech.blocks.machines.essence_furnace;

import gd.rf.kongzhongtitian.DuckTech.api.block.DTBaseProcessingBlockEntity;
import gd.rf.kongzhongtitian.DuckTech.blocks.reg.DTBlockEntity;
import gd.rf.kongzhongtitian.DuckTech.config.DTConfig;
import gd.rf.kongzhongtitian.DuckTech.items.DTItems;
import gd.rf.kongzhongtitian.DuckTech.sounds.DTSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.Nullable;

public class EssenceFurnaceBlockEntity extends DTBaseProcessingBlockEntity implements MenuProvider {

    public EssenceFurnaceBlockEntity( BlockPos p_155229_, BlockState p_155230_) {
        super(DTBlockEntity.ESSENCE_FURNACE_BLOCK_ENTITY.get(), p_155229_, p_155230_);

        setItemStackHandler(2);

        setMaxProgress(40);

    }



    @Override
    public Component getDisplayName() {
        return Component.translatable("block.ducktech.essence_furnace");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_) {
        return new EssenceFurnaceMenu(p_39954_, p_39955_, this,this.data);
    }

    public void tick(Level level, BlockPos pos, BlockState state ) {
        if (level.isClientSide()) return;

        if (!getItemStackHandler().getStackInSlot(0).isEmpty()) {
            if (getItemStackHandler().getStackInSlot(0).is(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("ducktech", "essence")))) {
                if (progress < maxProgress) {
                    progress++;
                } else {
                    if (!level.isClientSide()&& DTConfig.switch_sound()) {
                        level.playSound(null, pos,
                                DTSounds.ZAOYIN.get(),
                                SoundSource.BLOCKS,
                                1.0F,
                                1.0F);
                    }
                    ItemStack outputStack = getItemStackHandler().getStackInSlot(1);
                    ItemStack item = DTItems.BASIC_ESSENCE.get().getDefaultInstance();

                    if (outputStack.isEmpty()) {
                        getItemStackHandler().setStackInSlot(1, item);
                        getItemStackHandler().getStackInSlot(0).shrink(1);
                        progress = 0;
                    } else if (outputStack.getItem() == Items.GOLD_INGOT && outputStack.getCount() < outputStack.getMaxStackSize()) {
                        outputStack.grow(1);
                        getItemStackHandler().getStackInSlot(0).shrink(1);
                        progress = 0;
                    }
                }

                setChanged();
            } else {
                if (progress > 0) {
                    progress = 0;
                    setChanged();
                }
            }
        } else {
            if (progress > 0) {
                progress = 0;
                setChanged();
            }
        }
    }

    private IItemHandlerModifiable getItemStackHandler() {
        return  this.itemStackHandler;
    }
}
