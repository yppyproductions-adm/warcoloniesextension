package br.com.warcolonies.territory;

import java.util.ArrayList;
import java.util.List;

public class ConquestManager {
    private List<ConquestClaim> activeClaims = new ArrayList<>();
    
    // TODO: criar ConquestClaim, contagem de tempo, cancelar, mudar dono
    
    public void addClaim(ConquestClaim claim) {
        activeClaims.add(claim);
    }
    
    public void updateClaims() {
        activeClaims.removeIf(claim -> claim.isExpired() || claim.getStatus() != ClaimStatus.ACTIVE);
    }
    
    public List<ConquestClaim> getActiveClaims() {
        return new ArrayList<>(activeClaims);
    }
}
