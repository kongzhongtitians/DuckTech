package gd.rf.kongzhongtitian.DuckTech.blocks.machines.meow_machine;

import gd.rf.kongzhongtitian.DuckTech.blocks.reg.DTBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Mod.EventBusSubscriber
public class MeowMachineMigration {

    /**
     * 待迁移的坐标队列。按维度分组，避免不同维度的坐标混在一起。
     * 只在 ChunkEvent.Load 中记录坐标，绝不在该事件中直接修改世界。
     */
    private static final Map<ResourceKey<Level>, Set<BlockPos>> PENDING = new HashMap<>();

    /**
     * 每个服务器 tick 最多处理多少个坐标，防止一次性替换过多方块造成瞬时卡顿。
     */
    private static final int MAX_PER_TICK = 10;

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        if (ModList.get().isLoaded("mcmoddoll")) {
            return;
        }
        if (!(event.getChunk() instanceof LevelChunk chunk)) {
            return;
        }
        LevelAccessor accessor = event.getLevel();
        if (accessor.isClientSide()) {
            return;
        }
        if (!(accessor instanceof Level level)) {
            return;
        }

        Block meowBlock = DTBlocks.MEOW_MACHINE.get();
        if (meowBlock == null) {
            return;
        }

        ResourceKey<Level> dimension = level.dimension();
        Set<BlockPos> positions = PENDING.computeIfAbsent(dimension, k -> new HashSet<>());

        LevelChunkSection[] sections = chunk.getSections();
        for (int sectionIndex = 0; sectionIndex < sections.length; sectionIndex++) {
            LevelChunkSection section = sections[sectionIndex];
            if (section == null || section.hasOnlyAir()) {
                continue;
            }

            int sectionBottomY = chunk.getMinBuildHeight() + (sectionIndex * 16);
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = 0; y < 16; y++) {
                        BlockState state = section.getBlockState(x, y, z);
                        if (state.is(meowBlock)) {
                            positions.add(new BlockPos(
                                    chunk.getPos().getMinBlockX() + x,
                                    sectionBottomY + y,
                                    chunk.getPos().getMinBlockZ() + z
                            ));
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        if (PENDING.isEmpty()) {
            return;
        }

        Block replacement = DTBlocks.ESSENCE_CONVERSION_MACHINE.get();
        if (replacement == null) {
            return;
        }

        int processed = 0;
        Iterator<Map.Entry<ResourceKey<Level>, Set<BlockPos>>> levelIterator = PENDING.entrySet().iterator();
        while (levelIterator.hasNext() && processed < MAX_PER_TICK) {
            Map.Entry<ResourceKey<Level>, Set<BlockPos>> entry = levelIterator.next();
            ServerLevel level = event.getServer().getLevel(entry.getKey());
            if (level == null) {
                levelIterator.remove();
                continue;
            }

            Set<BlockPos> positions = entry.getValue();
            Iterator<BlockPos> posIterator = positions.iterator();
            while (posIterator.hasNext() && processed < MAX_PER_TICK) {
                BlockPos pos = posIterator.next();
                if (!level.isLoaded(pos)) {
                    continue;
                }
                if (level.getBlockState(pos).is(DTBlocks.MEOW_MACHINE.get())) {
                    level.setBlock(pos, replacement.defaultBlockState(), Block.UPDATE_ALL);
                }
                posIterator.remove();
                processed++;
            }

            if (positions.isEmpty()) {
                levelIterator.remove();
            }
        }
    }
}
