package gd.rf.kongzhongtitian.DuckTech.blocks.machines.shredder;

import gd.rf.kongzhongtitian.DuckTech.blocks.FacingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class Shredder extends FacingBlock implements EntityBlock {
    public Shredder(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new ShredderBlockEntity(p_153215_, p_153216_);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return level.isClientSide() ? null : (level1, pos, state, entity) -> ((ShredderBlockEntity)entity).tick(level1, pos, state, (ShredderBlockEntity)entity);
    }
}
