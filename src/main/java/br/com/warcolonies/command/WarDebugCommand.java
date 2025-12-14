package br.com.warcolonies.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import br.com.warcolonies.building.BuildingSimulationManager;
import br.com.warcolonies.colony.WarColony;
import br.com.warcolonies.colony.WarColonyManager;
import br.com.warcolonies.core.WarColoniesMod;
import br.com.warcolonies.data.WarEconomyManager;
import br.com.warcolonies.logistics.LogisticsManager;
import br.com.warcolonies.logistics.Shipment;
import br.com.warcolonies.logistics.ShipmentState;
import br.com.warcolonies.territory.ConquestClaim;
import br.com.warcolonies.territory.ConquestManager;
import br.com.warcolonies.territory.ClaimStatus;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.util.List;

/**
 * Comandos de debug do War Colonies.
 *
 * Por enquanto:
 *   /war debug stats      -> mostra contadores de tick dos managers
 *   /war debug colonies   -> lista colônias registradas no WarColonyManager
 *   /war debug shipments  -> lista caravanas abstratas (Shipment)
 *   /war debug claims     -> lista ConquestClaim ativos
 */
public class WarDebugCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("war")
                .then(Commands.literal("debug")
                    .then(Commands.literal("stats")
                        .executes(WarDebugCommand::debugStats)
                    )
                    .then(Commands.literal("colonies")
                        .executes(WarDebugCommand::debugColonies)
                    )
                    .then(Commands.literal("shipments")
                        .executes(WarDebugCommand::debugShipments)
                    )
                    .then(Commands.literal("claims")
                        .executes(WarDebugCommand::debugClaims)
                    )
                )
        );
    }

    // ===== /war debug stats =====
    private static int debugStats(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();

        WarEconomyManager economy = WarColoniesMod.getEconomyManager();
        LogisticsManager logistics = WarColoniesMod.getLogisticsManager();
        BuildingSimulationManager building = WarColoniesMod.getBuildingManager();
        ConquestManager conquest = WarColoniesMod.getConquestManager();

        String header = "=== War Colonies – Tick estratégico ===";
        source.sendSuccess(() -> Component.literal(header), false);

        String econ = "Economia: " + economy.getTicksProcessed() + " ticks";
        source.sendSuccess(() -> Component.literal(econ), false);

        String log = "Logística: " + logistics.getTicksProcessed()
                + " ticks, " + logistics.getActiveShipments().size() + " caravanas ativas";
        source.sendSuccess(() -> Component.literal(log), false);

        String build = "Construção: " + building.getTicksProcessed()
                + " ticks, " + building.getActiveTasks().size() + " obras virtuais";
        source.sendSuccess(() -> Component.literal(build), false);

        String conq = "Conquistas: " + conquest.getTicksProcessed()
                + " ticks, " + conquest.getActiveClaims().size() + " claims ativos";
        source.sendSuccess(() -> Component.literal(conq), false);

        return 1;
    }

    // ===== /war debug colonies =====
    private static int debugColonies(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();
        WarColonyManager colonyManager = WarColoniesMod.getColonyManager();
        List<WarColony> colonies = colonyManager.getColonies();

        if (colonies.isEmpty()) {
            source.sendSuccess(() -> Component.literal("Nenhuma colônia registrada ainda."), false);
            return 1;
        }

        String header = "=== War Colonies – Colônias Registradas (" + colonies.size() + ") ===";
        source.sendSuccess(() -> Component.literal(header), false);

        for (int i = 0; i < colonies.size(); i++) {
            WarColony c = colonies.get(i);
            String line = "#" + (i + 1)
                    + " - dono=" + c.getOwner()
                    + ", nível=" + c.getLevel()
                    + ", força militar=" + c.getMilitaryStrength()
                    + ", produção=" + c.getProductionRate()
                    + ", food=" + c.getResource("food");
            String finalLine = line; // efetivamente final pro lambda
            source.sendSuccess(() -> Component.literal(finalLine), false);
        }

        return 1;
    }

    // ===== /war debug shipments =====
    private static int debugShipments(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();
        LogisticsManager logistics = WarColoniesMod.getLogisticsManager();
        List<Shipment> shipments = logistics.getActiveShipments();

        if (shipments.isEmpty()) {
            source.sendSuccess(() -> Component.literal("Nenhuma caravana ativa no momento."), false);
            return 1;
        }

        String header = "=== War Colonies – Caravanas Ativas (" + shipments.size() + ") ===";
        source.sendSuccess(() -> Component.literal(header), false);

        for (int i = 0; i < shipments.size(); i++) {
            Shipment s = shipments.get(i);

            Object origin = s.getOrigin();
            Object dest = s.getDestination();
            ShipmentState state = s.getState();
            int progress = s.getProgress();

            String line = "#" + (i + 1)
                    + " - " + origin + " -> " + dest
                    + " | estado=" + state
                    + " | progresso=" + progress + "%";
            String finalLine = line;
            source.sendSuccess(() -> Component.literal(finalLine), false);
        }

        return 1;
    }

    // ===== /war debug claims =====
    private static int debugClaims(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();
        ConquestManager conquest = WarColoniesMod.getConquestManager();
        List<ConquestClaim> claims = conquest.getActiveClaims();

        if (claims.isEmpty()) {
            source.sendSuccess(() -> Component.literal("Nenhum claim de conquista ativo."), false);
            return 1;
        }

        String header = "=== War Colonies – Claims de Conquista (" + claims.size() + ") ===";
        source.sendSuccess(() -> Component.literal(header), false);

        for (int i = 0; i < claims.size(); i++) {
            ConquestClaim claim = claims.get(i);

            String line = "#" + (i + 1)
                    + " - alvo=" + claim.getTargetColonyId()
                    + ", atacante=" + claim.getAttackerId()
                    + ", estado=" + claim.getStatus()
                    + ", tempo restante=" + claim.getSecondsRemaining() + "s";
            String finalLine = line;
            source.sendSuccess(() -> Component.literal(finalLine), false);
        }

        return 1;
    }
}
