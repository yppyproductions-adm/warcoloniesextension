package br.com.warcolonies.territory;

import java.util.UUID;

/**
 * Representa uma tentativa de conquista de uma colônia.
 * Usado pelo ConquestManager e pelos comandos de debug.
 */
public class ConquestClaim {

    // Colônia alvo (id abstrato da WarColony)
    private final UUID targetColonyId;

    // Quem está tentando conquistar (jogador ou facção)
    private final UUID attackerId;

    // Momento em que a conquista começou (em milissegundos)
    private final long startTimeMillis;

    // Duração total da conquista, em segundos (ex.: 300 = 5min)
    private final int durationSeconds;

    // Estado atual do claim (ACTIVE, CANCELLED, COMPLETED, etc.)
    private ClaimStatus status;

    public ConquestClaim(UUID targetColonyId,
                         UUID attackerId,
                         int durationSeconds) {
        this.targetColonyId = targetColonyId;
        this.attackerId = attackerId;
        this.durationSeconds = durationSeconds;
        this.startTimeMillis = System.currentTimeMillis();
        this.status = ClaimStatus.ACTIVE;
    }

    // === GETTERS USADOS PELO DEBUG COMMAND ===

    public UUID getTargetColonyId() {
        return targetColonyId;
    }

    public UUID getAttackerId() {
        return attackerId;
    }

    public ClaimStatus getStatus() {
        return status;
    }

    public void setStatus(ClaimStatus status) {
        this.status = status;
    }

    /**
     * Retorna true se o tempo acabou (usado pelo ConquestManager).
     */
    public boolean isExpired() {
        return getSecondsRemaining() <= 0;
    }

    /**
     * Quantos segundos ainda faltam pro claim terminar.
     * Se já passou, retorna 0.
     */
    public int getSecondsRemaining() {
        long elapsedMillis = System.currentTimeMillis() - startTimeMillis;
        long elapsedSeconds = elapsedMillis / 1000L;
        long remaining = durationSeconds - elapsedSeconds;
        return (int) Math.max(0, remaining);
    }
}
