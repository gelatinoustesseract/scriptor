package com.ssblur.scriptor.blockentity;

import com.ssblur.scriptor.helpers.DictionarySavedData;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.Spell;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CastingLecternBlockEntity extends BlockEntity {
  public static final int SPELLBOOK_SLOT = 0;
  public static final int CASTING_FOCUS_SLOT = 1;

  NonNullList<ItemStack> items;

  public CastingLecternBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(ScriptorBlockEntities.CASTING_LECTERN.get(), blockPos, blockState);
    items = NonNullList.withSize(2, ItemStack.EMPTY);
  }

  public ItemStack getSpellbook() {
    return items.get(SPELLBOOK_SLOT);
  }

  @Nullable
  @Override
  public Packet<ClientGamePacketListener> getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  @Override
  public CompoundTag getUpdateTag() {
    var tag = super.getUpdateTag();

    saveAdditional(tag);

    return tag;
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);

    ContainerHelper.loadAllItems(tag, items);

    setChanged();
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);

    ContainerHelper.saveAllItems(tag, items);
  }

  public void tick() {}

  public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T entity) {
    if(level.isClientSide) return;
    if(entity instanceof CastingLecternBlockEntity tile) tile.tick();
  }
}
