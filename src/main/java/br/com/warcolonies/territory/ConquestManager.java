package br.com.warcolonies.territory;

import java.util.ArrayList;
import java.util.List;

/**
 * Gerencia as tentativas de conquista (ConquestClaim).
 * Por enquanto:
 *  - mantém uma lista de claims ativos
 *  - conta quantos ticks estratégicos já rodaram
 *  - remove claims expirados ou que não estão mais ATIVOS
 */
public class ConquestManager {

    private final List<ConquestClaim> activeClaims = new ArrayList<>();
    private long ticksProcessed = 0L;

    /**
     * Chamado pelo WarColoniesMod (via StrategicTickHandler) a cada tick estratégico.
     */
    public void updateClaims() {
        ticksProcessed++;

        // Por enquanto, só limpamos claims expirados ou que não estão mais ATIVOS.
        activeClaims.removeIf(claim ->
                claim.isExpired() || claim.getStatus() != ClaimStatus.ACTIVE
        );
    }

    public void addClaim(ConquestClaim claim) {
        activeClaims.add(claim);
    }

    public List<ConquestClaim> getActiveClaims() {
        return new ArrayList<>(activeClaims);
    }

    /**
     * Usado pelo comando /war debug stats para mostrar quantos ticks já rodaram.
     */
    public long getTicksProcessed() {
        return ticksProcessed;
    }
}
