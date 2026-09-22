package gd.rf.kongzhongtitian.DuckTech.blocks.blockentity;

import gd.rf.kongzhongtitian.DuckTech.blocks.DTBlockEntity;
import gd.rf.kongzhongtitian.DuckTech.items.DTItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class BedrockBreakerBlockEntity extends BlockEntity {

    public BedrockBreakerBlockEntity(BlockPos pos, BlockState state) {
        super(DTBlockEntity.BEDROCK_BREAKER_BE.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, BedrockBreakerBlockEntity be) {
        be.tick();
    }

    private void tick() {
        if (level == null || level.isClientSide) return;

        AABB area = new AABB(worldPosition).move(0, 1, 0).expandTowards(0, 0.5, 0);
        List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, area);

        for (ItemEntity itemEntity : items) {
            if (itemEntity.getItem().getCount() == 64 &&
                    itemEntity.getItem().is(DTItems.VOID_ESSENCE.get())) {

                itemEntity.discard();

                BlockPos below = worldPosition.below();
                if (level.getBlockState(below).is(Blocks.BEDROCK)) {
                    level.setBlock(below, Blocks.AIR.defaultBlockState(), 3);
                }
                break;
            }
        }
    }
}