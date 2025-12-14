package com.fenixlunar.warcolonies.client.widgets;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

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

        // DEBUG opcional de hitbox (deixa comentado)
        // if (this.isHoveredOrFocused()) g.renderOutline(this.getX(), this.getY(), this.width, this.height, 0xFF00FF00);
    }


    @Override
    protected void updateWidgetNarration(final net.minecraft.client.gui.narration.NarrationElementOutput narration)
    {
        // MVP: botão puramente visual. Narração mínima pra compilar no 1.21.1
        narration.add(net.minecraft.client.gui.narration.NarratedElementType.TITLE, this.getMessage());
    }
}
