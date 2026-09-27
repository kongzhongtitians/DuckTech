package gd.rf.kongzhongtitian.DuckTech.blocks.rubber_sapling;

import gd.rf.kongzhongtitian.DuckTech.DuckTech;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

public class RubberSaplingBlock extends SaplingBlock {

    public RubberSaplingBlock() {
        super(new RubberTreeGrower(),
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.PLANT)
                        .noCollission()
                        .randomTicks()
                        .instabreak()
                        .sound(SoundType.GRASS));
    }

    public static class RubberTreeGrower extends AbstractTreeGrower {

        @Nullable
        @Override
        protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(
                RandomSource random, boolean largeHive) {
            return ResourceKey.create(Registries.CONFIGURED_FEATURE,
                    ResourceLocation.fromNamespaceAndPath(DuckTech.MODID, "rubber"));
        }
    }
}
