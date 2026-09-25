package gd.rf.kongzhongtitian.DuckTech.blocks.machines.air_essence_collector;

import gd.rf.kongzhongtitian.DuckTech.blocks.FacingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class AirEssenceCollector extends FacingBlock implements EntityBlock {
    public AirEssenceCollector(Properties arg) {
        super(arg);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos arg, BlockState arg2) {
        return new AirEssenceCollectorBlockEntity(arg, arg2);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level1, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return level1.isClientSide ? null :(level, pos, state, blockEntity) -> ((AirEssenceCollectorBlockEntity)blockEntity).tick(level, pos, state, (AirEssenceCollectorBlockEntity)blockEntity);
    }
}
