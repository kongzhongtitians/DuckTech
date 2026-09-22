package gd.rf.kongzhongtitian.DuckTech.blocks.blockentity;

import gd.rf.kongzhongtitian.DuckTech.blocks.DTBlockEntity;
import gd.rf.kongzhongtitian.DuckTech.gui.levitation.LevitationMachineMenu;
import gd.rf.kongzhongtitian.DuckTech.items.DTItems;
import gd.rf.kongzhongtitian.DuckTech.sounds.DTSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LevitationMachineBlockEntity extends BlockEntity implements MenuProvider {
    public final ItemStackHandler itemStackHandler = new ItemStackHandler(1){
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
            super.onContentsChanged(slot);
        }
    };

        private int levitationTime = 0;
        private static final int MAX_LEVITATION_TIME = 600;
        private boolean isLevitating = false;

        public void tick(Level level, BlockPos pos, BlockState state) {
            if (level.isClientSide) return;

            if (!isLevitating && itemStackHandler.getStackInSlot(0).getItem().equals(DTItems.AIR_ESSENCE.get())) {
                if (!level.isClientSide()) {
                    level.playSound(null, pos,
                            DTSounds.ZAOYIN.get(),
                            SoundSource.BLOCKS,
                            1.0F,
                            1.0F);
                }
                itemStackHandler.getStackInSlot(0).shrink(1);
                startLevitationEffect();
            }

            if (isLevitating) {
                levitationTime++;
                data.set(0 ,this.levitationTime);
                setChanged();

                List<LivingEntity> entities = level.getEntitiesOfClass(
                        LivingEntity.class,
                        getBoundingBox(pos, 15),
                        entity -> !(entity instanceof Player) || !entity.isSpectator()
                );

                for (LivingEntity entity : entities) {
                    entity.addEffect(new MobEffectInstance(
                            MobEffects.LEVITATION,
                            40,
                            1,
                            true,
                            true
                    ));
                }

                if (levitationTime >= MAX_LEVITATION_TIME) {
                    isLevitating = false;
                    levitationTime = 0;
                }
            }
        }

        public void drops() {
            SimpleContainer inv = new SimpleContainer(itemStackHandler.getSlots());
            for (int i = 0; i < itemStackHandler.getSlots(); i++) {
                inv.setItem(i, itemStackHandler.getStackInSlot(i));
            }
            Containers.dropContents(this.level, this.worldPosition, inv);
        }

        @Override
        public CompoundTag getUpdateTag() {
            return saveWithoutMetadata();
        }
        @Override
        public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
            return ClientboundBlockEntityDataPacket.create(this);
        }

        private AABB getBoundingBox(BlockPos pos, int radius) {
            return new AABB(
                    pos.getX() - radius, pos.getY() - radius, pos.getZ() - radius,
                    pos.getX() + radius, pos.getY() + radius, pos.getZ() + radius
            );
        }

        private void startLevitationEffect() {
            isLevitating = true;
            levitationTime = 0;
        }

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            if (cap == ForgeCapabilities.ITEM_HANDLER) {

                LazyOptional<IItemHandler> inputHandler = LazyOptional.of(() -> new IItemHandler() {
                    @Override
                    public int getSlots() {
                        return itemStackHandler.getSlots();
                    }

                    @Override
                    public ItemStack getStackInSlot(int slot) {
                        return ItemStack.EMPTY;
                    }

                    @Override
                    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                        if (slot == 0 && stack.getItem().equals(DTItems.AIR_ESSENCE.get())) {
                            return itemStackHandler.insertItem(slot, stack, simulate);
                        }
                        return stack;
                    }

                    @Override
                    public ItemStack extractItem(int slot, int amount, boolean simulate) {
                        return ItemStack.EMPTY;
                    }

                    @Override
                    public int getSlotLimit(int slot) {
                        return itemStackHandler.getSlotLimit(slot);
                    }

                    @Override
                    public boolean isItemValid(int slot, ItemStack stack) {
                        return slot == 0 && stack.getItem().equals(DTItems.AIR_ESSENCE.get());
                    }
                });
                return inputHandler.cast();
            }
            return super.getCapability(cap, side);
        }

        protected final ContainerData data;
        public LevitationMachineBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
            super(DTBlockEntity.LEVITATION_MACHINE_BLOCK_ENTITY.get(), p_155229_, p_155230_);
            data = new SimpleContainerData(1);

        }

        @Override
        public Component getDisplayName() {
            return Component.translatable("block.ducktech.levitation_machine");
        }

        @Override
        public @Nullable AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_) {
            return new LevitationMachineMenu(p_39954_, p_39955_, this  ,data );
        }
    }