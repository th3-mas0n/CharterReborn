package net.arathain.charter.block.entity;

import net.arathain.charter.Charter;
import net.arathain.charter.item.SoulVesselItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Properties;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class SoulVesselBlockEntity extends BlockEntity implements AelpecyemIsCool {
    private final DefaultedList<ItemStack> ITEMS = DefaultedList.ofSize(1, ItemStack.EMPTY);

    public SoulVesselBlockEntity(BlockPos pos, BlockState state) {
        super(Charter.SOUL_VESSEL_ENTITY, pos, state);
    }

    public SoulVesselBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }


    public static void tick(World tickerWorld, BlockPos pos, BlockState tickerState, SoulVesselBlockEntity blockEntity) {
        if (tickerWorld != null) {
            BlockState state = tickerWorld.getBlockState(pos.offset(Direction.DOWN).offset(Direction.DOWN));
            if (tickerState.get(Properties.LIT) && blockEntity.isValid(0, blockEntity.getSoulVessel()) && state.isOf(Blocks.SOUL_FIRE) || state.isOf(Blocks.SOUL_LANTERN) || state.isOf(Blocks.SOUL_CAMPFIRE) || state.isOf(Blocks.SOUL_TORCH) || state.isOf(Blocks.SOUL_WALL_TORCH)) {
                if (!tickerWorld.isClient() && SoulVesselItem.getSoulUUID(blockEntity.getSoulVessel()) != null && tickerWorld.getPlayerByUuid(SoulVesselItem.getSoulUUID(blockEntity.getSoulVessel())) != null) {
                    PlayerEntity player = tickerWorld.getPlayerByUuid(SoulVesselItem.getSoulUUID(blockEntity.getSoulVessel()));
                    assert player != null;
                    player.addStatusEffect(new StatusEffectInstance(Charter.REPERCUSSION, 16000, 0, true, true));
                }
            }
        }
    }


    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(toClientTag(tag));
    }

    public void fromClientTag(NbtCompound tag) {
        ITEMS.clear();
        Inventories.readNbt(tag, ITEMS);
    }

    public NbtCompound toClientTag(NbtCompound tag) {
        Inventories.writeNbt(tag, ITEMS);
        return tag;
    }


    @Override
    public void readNbt(NbtCompound nbt) {
        fromClientTag(nbt);
        super.readNbt(nbt);
    }


    @Override
    public int getMaxCountPerStack() {
        return 1;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return ITEMS;
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        return SoulVesselItem.isViable(stack);
    }

    public ItemStack getSoulVessel() {
        return getStack(0);
    }


}