package gd.rf.kongzhongtitian.DuckTech.api.block;

import gd.rf.kongzhongtitian.DuckTech.utils.ProcessingData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class DTBaseProcessingBlockEntity extends DTBaseBlockEntity {
    protected final ContainerData data;
    public int progress = 0;
    public int maxProgress = 20;

    public DTBaseProcessingBlockEntity(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);

        data = new ProcessingData<>(this);
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("progress", progress);
        tag.putInt("maxProgress", maxProgress);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        progress = tag.getInt("progress");
        maxProgress = tag.getInt("maxProgress");
    }
}
