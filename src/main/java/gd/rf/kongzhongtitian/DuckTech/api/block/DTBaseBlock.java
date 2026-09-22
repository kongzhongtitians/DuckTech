package gd.rf.kongzhongtitian.DuckTech.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class DTBaseBlock<T extends DTBaseBlockEntity> extends Block implements EntityBlock {
    public DTBaseBlock(Properties p_49795_) {
        super(p_49795_);
    }


    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if ( !state.is(newState.getBlock())){
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof DTBaseBlockEntity atBaseBlockEntity) {
                atBaseBlockEntity.drops();
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }



    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return null;
    }
}
