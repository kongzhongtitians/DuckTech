package gd.rf.kongzhongtitian.DuckTech;

import gd.rf.kongzhongtitian.DuckTech.blocks.reg.DTBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

/**
 * 方块迁移：未安装 mcmoddoll 时，把世界中残留的 meow_machine 方块
 * 统一替换为 essence_conversion_machine。
 *
 * 触发时机：每个区块加载（ChunkEvent.Load）时检查一次；替换后该位置不再是 meow_machine，
 * 后续加载不会重复处理。空 section 直接跳过，性能开销可控。
 */
@Mod.EventBusSubscriber
public class MeowMachineMigration {

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        // 装了 mcmoddoll 就保留 meow_machine 原样，不动
        if (ModList.get().isLoaded("mcmoddoll")) {
            return;
        }
        if (!(event.getChunk() instanceof LevelChunk chunk)) {
            return;
        }
        if (event.getWorld().isClientSide()) {
            return;
        }

        Block meowBlock = DTBlocks.MEOW_MACHINE.get();
        Block replacement = DTBlocks.ESSENCE_CONVERSION_MACHINE.get();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (LevelChunkSection section : chunk.getSections()) {
            // 跳过全空 section
            if (section.hasOnlyAir()) {
                continue;
            }
            int sectionBottomY = section.bottomBlockY();
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = 0; y < 16; y++) {
                        BlockState state = section.getBlockState(x, y, z);
                        if (state.is(meowBlock)) {
                            pos.set(chunk.getPos().minBlockX() + x, sectionBottomY + y, chunk.getPos().minBlockZ() + z);
                            event.getWorld().setBlock(pos, replacement.defaultBlockState(), Block.UPDATE_ALL);
                        }
                    }
                }
            }
        }
    }
}
