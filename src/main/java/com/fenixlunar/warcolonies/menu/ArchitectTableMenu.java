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
import com.fenixlunar.warcolonies.data.ArchitectSavedData;
import net.minecraft.network.chat.Component;

/**
 * Menu da Mesa do Arquiteto - MVP 0.0
 * IMPORTANTE: sem slots (MineColonies style). Só serve para abrir a GUI.
 */
public class ArchitectTableMenu extends AbstractContainerMenu
{
    private final ContainerLevelAccess access;
    private int deliveryPriority = 1;
    private int sawmillEnabled = 0; // 0 = disabled, 1 = enabled
    // bit indexes for categories
    private static final int CAT_SAWMILL = 0;
    private static final int CAT_STONE = 1; // placeholder for future categories

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

        // Sawmill category enabled flag sync (0/1)
        this.addDataSlot(new DataSlot() {
            @Override
            public int get()
            {
                return sawmillEnabled;
            }

            @Override
            public void set(final int value)
            {
                sawmillEnabled = value;
            }
        });

        // MVP: sem slots mesmo (não desenha inventário nem hotbar)

        // If we have a real access (server), try to load persisted flags for this block
        this.access.evaluate((level, pos) -> {
            try {
                if (level instanceof ServerLevel serverLevel) {
                    final ArchitectSavedData saved = ArchitectSavedData.get(serverLevel);
                    this.sawmillEnabled = saved.getSawmillEnabled(pos) ? 1 : 0;
                }
            } catch (final Throwable ignored) {
            }
            return 0;
        }, 0);
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

    public boolean isSawmillEnabled()
    {
        return sawmillEnabled != 0;
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
            // Operate: coordinate constructions / teach enabled categories (no item spawning)
            this.access.evaluate((level, pos) -> {
                try {
                    if (level instanceof ServerLevel serverLevel) {
                        java.util.List<String> huts = com.fenixlunar.warcolonies.integration.ArchitectMineColoniesBridge.findNearbyHuts(serverLevel, pos);
                        if (huts.isEmpty()) {
                            sp.sendSystemMessage(Component.literal("Nenhuma colônia MineColonies próxima encontrada para ensinar receitas."));
                        } else {
                            sp.sendSystemMessage(Component.literal("Encontradas " + huts.size() + " construções/huts potenciais. Iniciando ensino para categorias habilitadas."));
                        }

                        boolean didAnything = false;
                        if (this.isSawmillEnabled()) {
                            didAnything = true;
                            com.fenixlunar.warcolonies.integration.ArchitectMineColoniesBridge.teachSawmillPlaceholder(serverLevel, pos, sp);
                        }

                        if (!didAnything) {
                            sp.sendSystemMessage(Component.literal("Nenhuma categoria selecionada para ensinar. Ative categorias no painel Materiais."));
                        }
                    }
                } catch (final Throwable t) {
                    sp.sendSystemMessage(Component.literal("Erro ao executar Operate: " + t.toString()));
                }
                return 0;
            }, 0);

            sp.sendSystemMessage(Component.literal("Operação iniciada: ensino disparado para categorias selecionadas."));
            return true;
        }

        if (id == 3) {
            // Toggle sawmill category
            this.sawmillEnabled = this.sawmillEnabled == 0 ? 1 : 0;
            this.broadcastChanges();
            // Persist change
            this.access.evaluate((level, pos) -> {
                try {
                    if (level instanceof ServerLevel serverLevel) {
                        final ArchitectSavedData saved = ArchitectSavedData.get(serverLevel);
                        saved.setSawmillEnabled(pos, this.sawmillEnabled != 0);
                    }
                } catch (final Throwable ignored) {
                }
                return 0;
            }, 0);
            sp.sendSystemMessage(Component.literal("Sawmill category toggled: " + (this.sawmillEnabled != 0 ? "ENABLED" : "DISABLED")));
            return true;
        }

        if (id == 4) {
            // Teach selected categories using bridge
            this.access.evaluate((level, pos) -> {
                try {
                    if (level instanceof ServerLevel serverLevel) {
                        java.util.List<String> huts = com.fenixlunar.warcolonies.integration.ArchitectMineColoniesBridge.findNearbyHuts(serverLevel, pos);
                        if (huts.isEmpty()) {
                            sp.sendSystemMessage(Component.literal("No nearby MineColonies colony found to teach recipes."));
                        } else {
                            sp.sendSystemMessage(Component.literal("Found " + huts.size() + " potential buildings/huts. Teaching placeholder."));
                        }
                        com.fenixlunar.warcolonies.integration.ArchitectMineColoniesBridge.teachSawmillPlaceholder(serverLevel, pos, sp);
                    }
                } catch (final Throwable t) {
                    sp.sendSystemMessage(Component.literal("Error while attempting to teach recipes: " + t.toString()));
                }
                return 0;
            }, 0);
            return true;
        }

        return false;
    }
}
