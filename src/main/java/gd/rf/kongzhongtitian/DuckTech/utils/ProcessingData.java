package gd.rf.kongzhongtitian.DuckTech.utils;

import gd.rf.kongzhongtitian.DuckTech.api.block.DTBaseProcessingBlockEntity;
import net.minecraft.world.inventory.ContainerData;

public class ProcessingData<T extends DTBaseProcessingBlockEntity> implements ContainerData {

    private final T blockEntity;

    public ProcessingData(T blockEntity) {
        this.blockEntity = blockEntity;
    }

    @Override
    public int get(int index) {
        return switch (index){
            case  0 -> blockEntity.progress;
            case  1 -> blockEntity.maxProgress;
            default -> 0;
        };
    }

    @Override
    public void set(int index, int value) {
        switch (index){
            case 0:
                blockEntity.progress = value;
                break;
            case 1:
                blockEntity.maxProgress = value;
                break;
        }
    }
    @Override
    public int getCount() {
        return 2;
    }

}
