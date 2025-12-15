package com.fenixlunar.warcolonies.block;

import com.fenixlunar.warcolonies.menu.ArchitectTableMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class ArchitectTableBlock extends Block {

    public ArchitectTableBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            try {
                if (player instanceof net.minecraft.server.level.ServerPlayer) {
                    ((net.minecraft.server.level.ServerPlayer) player).sendSystemMessage(Component.literal("DEBUG: Architect table used - opening menu"));
                }
            } catch (final Throwable ignored) {}

            try {
                player.openMenu(new MenuProvider() {
                @Override
                public Component getDisplayName() {
                    return Component.translatable("container.warcoloniesextension.architect_table");
                }

                @Nullable
                @Override
                public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
                    try {
                        return new ArchitectTableMenu(containerId, playerInventory, ContainerLevelAccess.create(level, pos));
                    } catch (final Throwable t) {
                        if (player instanceof net.minecraft.server.level.ServerPlayer) {
                            ((net.minecraft.server.level.ServerPlayer) player).sendSystemMessage(Component.literal("ERROR: couldn't create ArchitectTableMenu: " + t.toString()));
                        }
                        return null;
                    }
                }
            });
            } catch (final Throwable t) {
                try {
                    if (player instanceof net.minecraft.server.level.ServerPlayer) {
                        ((net.minecraft.server.level.ServerPlayer) player).sendSystemMessage(Component.literal("ERROR: openMenu failed: " + t.toString()));
                    }
                } catch (final Throwable ignored) {}
                return InteractionResult.FAIL;
            }
            return InteractionResult.CONSUME;
        }
    }
}
