package gd.rf.kongzhongtitian.DuckTech.blocks.blockentity;

import gd.rf.kongzhongtitian.DuckTech.blocks.DTBlockEntity;
import gd.rf.kongzhongtitian.DuckTech.config.DTConfig;
import gd.rf.kongzhongtitian.DuckTech.items.DTItems;
import gd.rf.kongzhongtitian.DuckTech.sounds.DTSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class AirEssenceCollectorBlockEntity extends BlockEntity {
    private boolean isActive = false;
    private int currentTime = 0;

    public AirEssenceCollectorBlockEntity(BlockPos arg2, BlockState arg3) {
        super(DTBlockEntity.AIR_ESSENCE_COLLECTOR_BLOCK_ENTITY.get(), arg2, arg3);
    }

    public void tick(Level level, BlockPos pos, BlockState state, AirEssenceCollectorBlockEntity entity) {
        if (level.isClientSide) return;

        BlockPos[] sidePositions = {
                pos.north(), pos.south(), pos.east(), pos.west(), pos.below(), pos.above()
        };


        boolean allSidesBlocked = true;
        for (BlockPos sidePosition : sidePositions) {
            if (!level.isEmptyBlock(sidePosition)) {
                allSidesBlocked = false;
                break;
            }
        }

        if (allSidesBlocked) {
            if (isActive) {
                if (currentTime < 80) {
                    currentTime++;
                } else {
                    if (!level.isClientSide()&& DTConfig.switch_sound()) {
                        level.playSound(null, pos,
                                DTSounds.ZAOYIN.get(),
                                SoundSource.BLOCKS,
                                1.0F,
                                1.0F);
                    }
                    ItemEntity outputEntity = new ItemEntity(
                            level,
                            pos.getX() + 0.5,
                            pos.above().getY() + 0.5,
                            pos.getZ() + 0.5,
                            DTItems.AIR_ESSENCE.get().getDefaultInstance()
                    );
                    outputEntity.setDeltaMovement(0, 0.1, 0);
                    level.addFreshEntity(outputEntity);

                    isActive = false;
                    currentTime = 0;
                }
            } else {
                startWorking();
            }

            startWorking();
        }
    }


    private void startWorking() {
        if (!isActive) {
            isActive = true;
            currentTime = 0;
        }
    }

    @Override
    protected void saveAdditional(CompoundTag p_187471_) {
        super.saveAdditional(p_187471_);
        p_187471_.putBoolean("isActive", isActive);
        p_187471_.putInt("currentTime", currentTime);
    }

    @Override
    public void load(CompoundTag p_155245_) {
        super.load(p_155245_);
        isActive = p_155245_.getBoolean("isActive");
        currentTime = p_155245_.getInt("currentTime");
    }
}
