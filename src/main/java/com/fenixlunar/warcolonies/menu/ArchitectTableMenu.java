package com.fenixlunar.warcolonies.menu;

import com.fenixlunar.warcolonies.WarColoniesExtension;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import com.fenixlunar.warcolonies.block.ArchitectTableBlock;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;

/**
 * Menu da Mesa do Arquiteto - MVP 0.0
 * IMPORTANTE: sem slots (MineColonies style). Só serve para abrir a GUI.
 */
public class ArchitectTableMenu extends AbstractContainerMenu
{
    private final ContainerLevelAccess access;

    public ArchitectTableMenu(final int containerId, final Inventory playerInventory)
    {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public ArchitectTableMenu(final int containerId, final Inventory playerInventory, final ContainerLevelAccess access)
    {
        super(WarColoniesExtension.ARCHITECT_TABLE_MENU.get(), containerId);
        this.access = access;

        // MVP: sem slots mesmo (não desenha inventário nem hotbar)
    }

    @Override
    public ItemStack quickMoveStack(final Player player, final int index)
    {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(final Player player)
    {
        // MVP: implementamos uma verificação mínima de validade sem inventar sincronização.
        // Verifica se o bloco na posição ainda é uma ArchitectTableBlock e se o jogador
        // está dentro da distância (64 = 8 blocks) — aproximação padrão do vanilla.
        // TODO (PÓS-MVP): usar distâncias/checagens exatas de acordo com a API do jogo e
        // traduzir mensagens para localization keys.
        return this.access.evaluate((level, pos) -> {
            try {
                final Block block = level.getBlockState(pos).getBlock();
                if (block instanceof ArchitectTableBlock) {
                    final double dx = player.getX() - (pos.getX() + 0.5D);
                    final double dy = player.getY() - (pos.getY() + 0.5D);
                    final double dz = player.getZ() - (pos.getZ() + 0.5D);
                    return dx * dx + dy * dy + dz * dz <= 64.0D; // 8 blocks squared
                }
                return false;
            } catch (final Exception e) {
                // MVP: se algo falhar, consideramos inválido para prevenir abuses.
                return false;
            }
        }, false);
    }

    public ContainerLevelAccess getAccess()
    {
        return access;
    }
}
