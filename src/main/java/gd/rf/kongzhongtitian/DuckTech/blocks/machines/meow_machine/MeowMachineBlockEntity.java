package gd.rf.kongzhongtitian.DuckTech.blocks.machines.meow_machine;

import gd.rf.kongzhongtitian.DuckTech.blocks.machines.essence_conversion_machine.EssenceConversionMachineBlockEntity;
import gd.rf.kongzhongtitian.DuckTech.blocks.reg.DTBlockEntity;
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
