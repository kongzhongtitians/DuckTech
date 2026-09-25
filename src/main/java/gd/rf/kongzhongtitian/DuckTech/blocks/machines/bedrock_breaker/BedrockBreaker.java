package gd.rf.kongzhongtitian.DuckTech.blocks.machines.bedrock_breaker;


import gd.rf.kongzhongtitian.DuckTech.blocks.reg.DTBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BedrockBreaker extends BaseEntityBlock {

    public BedrockBreaker(Properties props) {
        super(props);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;  // 使用普通方块模型渲染
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BedrockBreakerBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
                                                                  BlockEntityType<T> type) {
        // 只在服务端 tick
        if (level.isClientSide()) return null;
        return createTickerHelper(type, DTBlockEntity.BEDROCK_BREAKER_BE.get(),
                BedrockBreakerBlockEntity::serverTick);
    }
}