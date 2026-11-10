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

@Mod.EventBusSubscriber
public class MeowMachineMigration {

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        if (ModList.get().isLoaded("mcmoddoll")) {
            return;
        }
        if (!(event.getChunk() instanceof LevelChunk chunk)) {
            return;
        }
        if (event.getLevel().isClientSide()) {
            return;
        }

        Block meowBlock = DTBlocks.MEOW_MACHINE.get();
        Block replacement = DTBlocks.ESSENCE_CONVERSION_MACHINE.get();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        LevelChunkSection[] sections = chunk.getSections();
        for (int sectionIndex = 0; sectionIndex < sections.length; sectionIndex++) {
            LevelChunkSection section = sections[sectionIndex];
            if (section == null || section.hasOnlyAir()) {
                continue;
            }

            // getBottomBlockY 是静态方法，需要传入 section 索引
            int sectionBottomY = chunk.getMinBuildHeight() + (sectionIndex * 16);
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = 0; y < 16; y++) {
                        BlockState state = section.getBlockState(x, y, z);
                        if (state.is(meowBlock)) {
                            pos.set(
                                    chunk.getPos().getMinBlockX() + x,
                                    sectionBottomY + y,
                                    chunk.getPos().getMinBlockZ() + z
                            );
                            // 1.20.1 中 ChunkEvent.Load 提供 getLevel() 而非 getWorld()
                            event.getLevel().setBlock(pos, replacement.defaultBlockState(), Block.UPDATE_ALL);
                        }
                    }
                }
            }
        }
    }
}