package gd.rf.kongzhongtitian.DuckTech.blocks.custom;

import gd.rf.kongzhongtitian.DuckTech.blocks.FacingBlock;
import gd.rf.kongzhongtitian.DuckTech.blocks.blockentity.LevitationMachineBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class LevitationMachine extends FacingBlock implements EntityBlock {
    public LevitationMachine(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new LevitationMachineBlockEntity(p_153215_, p_153216_);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if ( !state.is(newState.getBlock())){
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof LevitationMachineBlockEntity levitationMachineBlockEntity) {
                levitationMachineBlockEntity.drops();
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide){
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof LevitationMachineBlockEntity levitationMachineBlockEntity) {
                NetworkHooks.openScreen((ServerPlayer) player, levitationMachineBlockEntity , pos);

                return InteractionResult.SUCCESS;
            }else {
                throw  new IllegalStateException("our container provider is missing");
            }
        }

        return super.use( state, level, pos, player, hand, hitResult);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return level.isClientSide() ? null : (level1, pos, state, entity) -> ((LevitationMachineBlockEntity)entity).tick(level1, pos, state);
    }
}
