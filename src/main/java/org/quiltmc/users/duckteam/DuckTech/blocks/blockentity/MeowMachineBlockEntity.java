package org.quiltmc.users.duckteam.DuckTech.blocks.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.quiltmc.users.duckteam.DuckTech.blocks.DTBlockEntity;
import org.quiltmc.users.duckteam.DuckTech.mixin.BlockEntityAccessor;

//Registered from DTBlocks::<clinit>
public class MeowMachineBlockEntity extends EssenceConversionMachineBlockEntity {
    public MeowMachineBlockEntity(BlockPos pos, BlockState state) {
        super(pos,state);
        if(DTBlockEntity.MEOW_MACHINE_BLOCK_ENTITY!=null){
            ((BlockEntityAccessor) this).setType(DTBlockEntity.MEOW_MACHINE_BLOCK_ENTITY.get());
        }
    }
}
