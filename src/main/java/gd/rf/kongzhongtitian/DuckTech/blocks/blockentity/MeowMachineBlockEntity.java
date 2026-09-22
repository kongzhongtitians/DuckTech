package gd.rf.kongzhongtitian.DuckTech.blocks.blockentity;

import gd.rf.kongzhongtitian.DuckTech.blocks.DTBlockEntity;
import gd.rf.kongzhongtitian.DuckTech.mixin.BlockEntityAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

//Registered from DTBlocks::<clinit>
public class MeowMachineBlockEntity extends EssenceConversionMachineBlockEntity {
    public MeowMachineBlockEntity(BlockPos pos, BlockState state) {
        super(pos,state);
        if(DTBlockEntity.MEOW_MACHINE_BLOCK_ENTITY!=null){
            ((BlockEntityAccessor) this).setType(DTBlockEntity.MEOW_MACHINE_BLOCK_ENTITY.get());
        }
    }
}
