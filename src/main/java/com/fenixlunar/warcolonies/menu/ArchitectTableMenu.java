package com.fenixlunar.warcolonies.menu;

import com.fenixlunar.warcolonies.WarColoniesExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import com.fenixlunar.warcolonies.block.ArchitectTableBlock;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.network.chat.Component;

/**
 * Menu da Mesa do Arquiteto - MVP 0.0
 * IMPORTANTE: sem slots (MineColonies style). Só serve para abrir a GUI.
 */
public class ArchitectTableMenu extends AbstractContainerMenu
{
    private final ContainerLevelAccess access;
    private int deliveryPriority = 1;

    public ArchitectTableMenu(final int containerId, final Inventory playerInventory)
    {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    // Server-side constructor (and general)
    public ArchitectTableMenu(final int containerId, final Inventory playerInventory, final ContainerLevelAccess access)
    {
        super(WarColoniesExtension.ARCHITECT_TABLE_MENU.get(), containerId);
        this.access = access;

        // Delivery priority sync (server -> client) via DataSlot
        this.addDataSlot(new DataSlot() {
            @Override
            public int get()
            {
                return deliveryPriority;
            }

            @Override
            public void set(final int value)
            {
                deliveryPriority = value;
            }
        });

        // MVP: sem slots mesmo (não desenha inventário nem hotbar)
    }

    // Client-side constructor used by container factory (reads pos if present)
    public ArchitectTableMenu(final int containerId, final Inventory playerInventory, final FriendlyByteBuf buf)
    {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
        try {
            // Some providers write pos into the buffer; read if present to keep compatibility.
            final BlockPos pos = buf.readBlockPos();
            // client side doesn't need to store pos/level — access will be NULL here
        } catch (final Exception ignored) {
        }
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

    public int getDeliveryPriority()
    {
        return deliveryPriority;
    }

    @Override
    public boolean clickMenuButton(final Player player, final int id)
    {
        // Server-only logic (AbstractContainerMenu handles routing). We still check instance.
        if (!(player instanceof ServerPlayer sp)) return false;

        if (id == 1) {
            // decrement
            deliveryPriority = Math.max(0, deliveryPriority - 1);
            this.broadcastChanges();
            sp.sendSystemMessage(Component.literal("MVP: Delivery priority set to " + deliveryPriority));
            return true;
        }

        if (id == 2) {
            // increment
            deliveryPriority = Math.min(10, deliveryPriority + 1);
            this.broadcastChanges();
            sp.sendSystemMessage(Component.literal("MVP: Delivery priority set to " + deliveryPriority));
            return true;
        }

        if (id == 0) {
            // Operate: spawn a few example items at the table position (server-side)
            this.access.evaluate((level, pos) -> {
                if (level instanceof ServerLevel serverLevel) {
                    final BlockPos spawnPos = pos.above();

                    // Example "pedido": 32 torches, 64 planks (oak), 16 stairs (oak)
                    final ItemStack torches = new ItemStack(Items.TORCH, 32);
                    final ItemStack planks = new ItemStack(Items.OAK_PLANKS, 64);
                    final ItemStack stairs = new ItemStack(Items.OAK_STAIRS, 16);

                    final ItemEntity e1 = new ItemEntity(serverLevel, spawnPos.getX() + 0.5, spawnPos.getY() + 1.0, spawnPos.getZ() + 0.5, torches);
                    final ItemEntity e2 = new ItemEntity(serverLevel, spawnPos.getX() + 0.6, spawnPos.getY() + 1.0, spawnPos.getZ() + 0.6, planks);
                    final ItemEntity e3 = new ItemEntity(serverLevel, spawnPos.getX() + 0.4, spawnPos.getY() + 1.0, spawnPos.getZ() + 0.4, stairs);

                    serverLevel.addFreshEntity(e1);
                    serverLevel.addFreshEntity(e2);
                    serverLevel.addFreshEntity(e3);
                }
                return null;
            }, null);

            sp.sendSystemMessage(Component.literal("MVP: Pedido criado (items spawned)"));
            return true;
        }

        return false;
    }
}
