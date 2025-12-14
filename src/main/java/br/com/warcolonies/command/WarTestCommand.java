package br.com.warcolonies.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import br.com.warcolonies.building.BuildingSimulationManager;
import br.com.warcolonies.building.VirtualBuildingTask;
import br.com.warcolonies.colony.WarColony;
import br.com.warcolonies.core.WarColoniesMod;
import br.com.warcolonies.logistics.LogisticsManager;
import br.com.warcolonies.territory.ConquestClaim;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

/**
 * Comandos de TESTE (criam dados fake para você ver o sistema funcionando).
 *
 *   /war test colony     -> cria uma colônia simples ligada ao jogador
 *   /war test shipment   -> cria uma caravana abstrata fake
 *   /war test claim      -> cria um ConquestClaim fake de 60s
 *   /war test buildtask  -> cria uma obra virtual que some sozinha depois de alguns segundos
 */
public class WarTestCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("war")
                        .then(Commands.literal("test")
                                .then(Commands.literal("colony")
                                        .executes(WarTestCommand::createTestColony)
                                )
                                .then(Commands.literal("shipment")
                                        .executes(WarTestCommand::createTestShipment)
                                )
                                .then(Commands.literal("claim")
                                        .executes(WarTestCommand::createTestClaim)
                                )
                                .then(Commands.literal("buildtask")
                                        .executes(WarTestCommand::createTestBuildTask)
                                )
                        )
        );
    }

    // === /war test colony ===
    private static int createTestColony(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();

        if (!(source.getEntity() instanceof ServerPlayer player)) {
            source.sendFailure(net.minecraft.network.chat.Component.literal("Apenas jogadores podem criar colônias."));
            return 0;
        }

        UUID owner = player.getUUID();

        WarColony colony = new WarColony(owner, player.blockPosition(), 1);
        colony.setName("Colônia de " + player.getName().getString());
        colony.setProductionRate(3);
        colony.setMilitaryStrength(10);

        WarColoniesMod.getColonyManager().addColony(colony);

        source.sendSuccess(() ->
                net.minecraft.network.chat.Component.literal(
                        "Colônia criada! Execute /war debug colonies e /war debug stats para visualizar a produção."
                ), false);

        return 1;
    }

    // === /war test shipment ===
    private static int createTestShipment(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();

        // Usa SEMPRE o LogisticsManager global do WarColoniesMod
        LogisticsManager manager = WarColoniesMod.getLogisticsManager();

        // Cria uma caravana abstrata simples, só para ver no /war debug shipments
        manager.createShipment(
                "Colônia A (debug)",
                "Colônia B (debug)",
                java.util.Collections.emptyList(),
                null
        );

        int count = manager.getActiveShipments().size();

        source.sendSuccess(() ->
                net.minecraft.network.chat.Component.literal(
                        "Caravana de teste criada! Caravanas ativas agora: " + count +
                        ". Use /war debug shipments para acompanhar o progresso."
                ), false);

        return 1;
    }

    // === /war test claim ===
    private static int createTestClaim(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();

        if (!(source.getEntity() instanceof ServerPlayer player)) {
            source.sendFailure(net.minecraft.network.chat.Component.literal("Apenas jogadores podem iniciar claims de teste."));
            return 0;
        }

        UUID playerId = player.getUUID();

        // Claim fake: alvo = playerId, atacante = playerId, duração = 60s
        ConquestClaim claim = new ConquestClaim(
                playerId,
                playerId,
                60
        );

        WarColoniesMod.getConquestManager().addClaim(claim);

        source.sendSuccess(() ->
                net.minecraft.network.chat.Component.literal(
                        "Claim de conquista de teste criado! Use /war debug claims para ver o timer."
                ), false);

        return 1;
    }

    // === /war test buildtask ===
    private static int createTestBuildTask(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();

        BuildingSimulationManager manager = WarColoniesMod.getBuildingManager();

        // Tarefa de 30 "ticks estratégicos" (~30 segundos reais)
        VirtualBuildingTask task = new VirtualBuildingTask(
                "Obra virtual de teste",
                30
        );

        manager.addTask(task);

        source.sendSuccess(() ->
                net.minecraft.network.chat.Component.literal(
                        "Obra virtual de teste criada! Veja o contador em /war debug stats (Construção: ... tarefas)."
                ), false);

        return 1;
    }
}
