package br.com.warcolonies.client.gui.huts;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

/**
 * Render helper VANILLA (sem BlockUI).
 * Só desenha o "papel" e textos por cima.
 */
public final class WindowArchitectHut
{
    private WindowArchitectHut() {}

    public static final ResourceLocation BG =
            ResourceLocation.parse("warcolonies:textures/gui/builderhut/builder_paper.png");

    public static final int BG_W = 190;
    public static final int BG_H = 244;

    public static void render(GuiGraphics g, Font font, int screenW, int screenH)
    {
        final int left = (screenW - BG_W) / 2;
        final int top  = (screenH - BG_H) / 2;

        // Fundo (190x244) igual MineColonies
        g.blit(BG, left, top, 0, 0, BG_W, BG_H, BG_W, BG_H);

        // Textos (preto)
        g.drawCenteredString(font, "Arquiteto — Nível 1", left + BG_W / 2, top + 14, 0x000000);
        g.drawCenteredString(font, "Aguardando pedidos de construção", left + BG_W / 2, top + 30, 0x000000);

        g.drawString(font, "- Itens pendentes aparecerão aqui -", left + 16, top + 60, 0x000000);
    }
}
