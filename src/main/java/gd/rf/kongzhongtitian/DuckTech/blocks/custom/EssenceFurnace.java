package gd.rf.kongzhongtitian.DuckTech.blocks.custom;

import gd.rf.kongzhongtitian.DuckTech.api.block.DTBaseBlock;
import gd.rf.kongzhongtitian.DuckTech.blocks.blockentity.EssenceFurnaceBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class EssenceFurnace extends DTBaseBlock {
    public EssenceFurnace(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new EssenceFurnaceBlockEntity(p_153215_, p_153216_);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return p_153212_.isClientSide ? null :(level, pos, state, be) -> {
            if (be instanceof EssenceFurnaceBlockEntity essenceFurnace) {
                essenceFurnace.tick(level, pos, state);
            }
        };
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide){
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof EssenceFurnaceBlockEntity entity) {
                NetworkHooks.openScreen((ServerPlayer) player, entity , pos);

                return InteractionResult.SUCCESS;
            }else {
                throw  new IllegalStateException("our container provider is missing");
            }
        }

        return super.use( state, level, pos, player, hand, hitResult);
    }
}
