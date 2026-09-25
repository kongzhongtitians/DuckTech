package gd.rf.kongzhongtitian.DuckTech.blocks.machines.void_essence_collector;

import gd.rf.kongzhongtitian.DuckTech.blocks.reg.DTBlockEntity;
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

public class VoidEssenceCollectorBlockEntity extends BlockEntity {
    private boolean isActive = false;
    private int currentTime = 0;

    public VoidEssenceCollectorBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(DTBlockEntity.VOID_ESSENCE_COLLECTOR_BLOCK_ENTITY.get(), p_155229_, p_155230_);
    }

    public void tick(Level level, BlockPos pos, BlockState state, VoidEssenceCollectorBlockEntity entity){
        if (level.isClientSide) return;

        int y = pos.getY();

        if (isActive){
            if (y == -63){
                addEssence(80, level, pos);
            }else if (y == -64){
                addEssence(40, level, pos);
            }
        }

        startWorking();
    }

    private void addEssence(int ticks, Level level, BlockPos pos) {
        if (currentTime < ticks) {
            currentTime++;
        } else {
            if (!level.isClientSide()&& DTConfig.switch_sound()) {
                level.playSound(null, pos,
                        DTSounds.ZAOYIN.get(),
                        SoundSource.BLOCKS,
                        1.0F,
                        1.0F);
            }
            BlockPos spawnPosition = findSpawnPosition(level, pos);

            ItemEntity outputEntity = new ItemEntity(
                    level,
                    spawnPosition.getX() + 0.5,
                    spawnPosition.getY() + 1.5,
                    spawnPosition.getZ() + 0.5,
                    DTItems.VOID_ESSENCE.get().getDefaultInstance()
            );

            outputEntity.setDeltaMovement(0, 0.1, 0);
            level.addFreshEntity(outputEntity);

            // 重置状态
            isActive = false;
            currentTime = 0;
        }
    }

    private void startWorking() {
        if (!isActive) {  // 只有在非活动状态时才重置
            isActive = true;
            currentTime = 0;
        }
    }


    private BlockPos findSpawnPosition(Level level, BlockPos pos) {
        BlockPos belowPos = pos.below();
        if (level.isEmptyBlock(belowPos)) {
            return belowPos;
        }

        BlockPos[] sidePositions = {
                pos.north(), pos.south(), pos.east(), pos.west()
        };

        for (BlockPos sidePos : sidePositions) {
            if (level.isEmptyBlock(sidePos)) {
                return sidePos;
            }
        }

        return pos.above();
    }


    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putBoolean("isActive", isActive);
        tag.putInt("currentTime", currentTime);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        isActive = tag.getBoolean("isActive");
        currentTime = tag.getInt("currentTime");
    }
}
