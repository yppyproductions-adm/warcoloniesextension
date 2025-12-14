package br.com.warcolonies.client.gui.huts;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ArchitectHutScreen extends Screen
{
    public ArchitectHutScreen()
    {
        super(Component.literal("Arquiteto — Nível 1"));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks)
    {
        // 1.21+ precisa desses 4 args
        this.renderBackground(graphics, mouseX, mouseY, partialTicks);

        WindowArchitectHut.render(graphics, this.font, this.width, this.height);

        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }
}
