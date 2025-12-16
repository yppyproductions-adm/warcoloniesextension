package com.fenixlunar.warcolonies.client.widgets;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.Minecraft;

/**
 * Botão que desenha uma textura PNG direta (sem WidgetSprites/atlas).
 * Isso evita o "rosa/magenta" do ImageButton no 1.21.1 quando a sprite não está no atlas esperado.
 */
public class TexturedButton extends AbstractButton
{
    private final ResourceLocation texture;
    private final int texW;
    private final int texH;
    private final Runnable onPress;

    public TexturedButton(
            final int x, final int y,
            final int w, final int h,
            final ResourceLocation texture,
            final int texW, final int texH,
            final Runnable onPress,
            final Component narration)
    {
        super(x, y, w, h, narration == null ? Component.empty() : narration);
        this.texture = texture;
        this.texW = texW;
        this.texH = texH;
        this.onPress = onPress == null ? () -> {} : onPress;
    }

    @Override
    public void onPress()
    {
        onPress.run();
    }

    @Override
    protected void renderWidget(final GuiGraphics g, final int mouseX, final int mouseY, final float partialTicks)
    {
        // Textura (sem sombra, sem “state sprite”)
        // Se você quiser hover mais tarde, a gente cria outro RL ou muda UV.
        g.blit(texture, this.getX(), this.getY(), 0, 0, this.width, this.height, texW, texH);

        // MVP: destaque visual ao passar o mouse (overlay translúcido + contorno)
        // Isso facilita testar/criar a UI sem mudar texturas.
        if (this.isHoveredOrFocused()) {
            // overlay branco translúcido
            g.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 0x33FFFFFF);
            // contorno amarelo para visualização clara
            try {
                g.renderOutline(this.getX(), this.getY(), this.width, this.height, 0xFFEEAA00);
            } catch (final Throwable t) {
                // Caso a renderOutline não exista ou falhe em runtime, não quebra o cliente
            }
        }

        // Desenha o texto do botão (se houver mensagem passada). Centraliza horizontalmente.
        try {
            final Component msg = this.getMessage();
            if (msg != null && !msg.getString().isEmpty()) {
                final var font = Minecraft.getInstance().font;
                final int tw = font.width(msg);
                final int tx = this.getX() + (this.width - tw) / 2;
                final int ty = this.getY() + (this.height - 8) / 2;
                // Draw scaled down (85%) to avoid overlapping artifacts while keeping layout.
                try {
                    final float scale = 0.85f;
                    final var pose = g.pose();
                    pose.pushPose();
                    pose.scale(scale, scale, 1.0f);
                    // Compute centered position accounting for scale to ensure correct centering
                    final int twScaled = Math.round(tw * scale);
                    final int sx = Math.round((this.getX() + (this.width - twScaled) / 2f) / scale);
                    final int sy = Math.round((this.getY() + (this.height - 8) / 2f) / scale);
                    g.drawString(font, msg, sx, sy, 0xFF000000, false);
                    pose.popPose();
                } catch (final Throwable t2) {
                    // Fallback: draw normally if pose/scale unavailable
                    g.drawString(font, msg, tx, ty, 0xFF000000, false);
                }
            }
        } catch (final Throwable t) {
            // não bloquear a UI se algo falhar com a fonte (compatibilidade)
        }
    }


    @Override
    protected void updateWidgetNarration(final net.minecraft.client.gui.narration.NarrationElementOutput narration)
    {
        // MVP: botão puramente visual. Narração mínima pra compilar no 1.21.1
        narration.add(net.minecraft.client.gui.narration.NarratedElementType.TITLE, this.getMessage());
    }
}
