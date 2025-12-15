package com.fenixlunar.warcolonies.client;

import com.fenixlunar.warcolonies.client.widgets.TexturedButton;
import com.fenixlunar.warcolonies.menu.ArchitectTableMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * Layout 1:1 baseado nos XML do MineColonies:
 * - layouthuts/layouthutpageactionsminwoinv.xml
 * - layouthuts/layouthutpageactionsmin.xml
 * - layouthuts/layouthutpageactions.xml
 *
 * IMPORTANTE:
 * - Nada de lógica (MVP visual).
 * - Nada de inventário/hotbar/slots.
 * - Qualquer parte do layoutbuilderres.xml (resources list) foi mantida COMENTADA para uso futuro
 *   como “aba”/tela separada.
 */
public class ArchitectTableScreen extends AbstractContainerScreen<ArchitectTableMenu>
{
        // Delivery Priority é autoritativa no menu (servidor). Cliente apenas lê via DataSlot.

    // Você já está carregando texturas desse namespace hoje.
    private static final String NS = "warcoloniesextension";

        // Fundo (use MineColonies original builder paper)
        private static final ResourceLocation BG_PAPER =
            ResourceLocation.parse("minecolonies:textures/gui/builderhut/builder_paper.png");

    // Header sketch (layouthutpageactionsminwoinv.xml)
    private static final ResourceLocation SKETCH_LEFT =
            ResourceLocation.parse(NS + ":textures/gui/builderhut/builder_sketch_left.png");
    private static final ResourceLocation SKETCH_CENTER =
            ResourceLocation.parse(NS + ":textures/gui/builderhut/builder_sketch_center.png");
    private static final ResourceLocation SKETCH_RIGHT =
            ResourceLocation.parse(NS + ":textures/gui/builderhut/builder_sketch_right.png");

    // Botões (XML)
    private static final ResourceLocation BTN_EDIT =
            ResourceLocation.parse(NS + ":textures/gui/builderhut/edit.png");

    private static final ResourceLocation BTN_MEDIUM_LARGE =
            ResourceLocation.parse(NS + ":textures/gui/builderhut/builder_button_medium_large.png");
    private static final ResourceLocation BTN_MEDIUM =
            ResourceLocation.parse(NS + ":textures/gui/builderhut/builder_button_medium.png");
    private static final ResourceLocation BTN_MINI =
            ResourceLocation.parse(NS + ":textures/gui/builderhut/builder_button_mini.png");

    // Ícones (layouthutpageactionsminwoinv.xml / layouthutpageactionsmin.xml)
    private static final ResourceLocation ICON_INFO =
            ResourceLocation.parse("warcolonies:textures/gui/red_wax_information.png");
    private static final ResourceLocation ICON_CHEST =
            ResourceLocation.parse("warcolonies:textures/gui/chest.png");
        // Side tab textures (use images from the `warcolonies` namespace)
        private static final ResourceLocation TAB_SIDE_1 =
            ResourceLocation.parse("warcolonies:textures/gui/modules/tab_side1.png");
        private static final ResourceLocation TAB_SIDE_2 =
            ResourceLocation.parse("warcolonies:textures/gui/modules/tab_side2.png");
        private static final ResourceLocation TAB_SIDE_3 =
            ResourceLocation.parse("warcolonies:textures/gui/modules/tab_side3.png");
        private static final ResourceLocation TAB_SIDE_4 =
            ResourceLocation.parse("warcolonies:textures/gui/modules/tab_side4.png");

        private static final ResourceLocation MODULE_INFO =
            ResourceLocation.parse("warcolonies:textures/gui/modules/info.png");
        private static final ResourceLocation MODULE_SETTINGS =
            ResourceLocation.parse("warcolonies:textures/gui/modules/settings.png");
        private static final ResourceLocation MODULE_STOCK =
            ResourceLocation.parse("warcolonies:textures/gui/modules/stock.png");
        private static final ResourceLocation MODULE_INVENTORY =
            ResourceLocation.parse("warcolonies:textures/gui/modules/inventory.png");

    // Tamanhos reais (MineColonies)
    private static final int BG_W = 190;
    private static final int BG_H = 244;

    private static final int EDIT_W = 15;
    private static final int EDIT_H = 15;

    private static final int SKETCH_L_W = 6;
    private static final int SKETCH_L_H = 15;

    private static final int SKETCH_C_TEX_W = 154;
    private static final int SKETCH_C_TEX_H = 15;

    private static final int SKETCH_R_W = 6;
    private static final int SKETCH_R_H = 15;

    private static final int MEDIUM_LARGE_W = 129;
    private static final int MEDIUM_LARGE_H = 17;

    private static final int MEDIUM_W = 86;
    private static final int MEDIUM_H = 17;

    private static final int MINI_W = 14;
    private static final int MINI_H = 15;

    private static final int ICON_17 = 17;

    // Labels COPIADAS do XML do MineColonies (placeholders até você criar as chaves no seu lang)
    private static final Component LBL_WORKER_ASSIGNED =
            Component.translatable("com.minecolonies.coremod.gui.workerhuts.workerassigned");
    private static final Component LBL_MANAGE =
            Component.translatable("com.minecolonies.coremod.gui.workerhuts.manage");
    private static final Component LBL_RECALL =
            Component.translatable("com.minecolonies.coremod.gui.workerhuts.recall");
    private static final Component LBL_BUILD_REPAIR =
            Component.translatable("com.minecolonies.coremod.gui.workerhuts.buildrepair");
    private static final Component LBL_FORCE_PICKUP =
            Component.translatable("com.minecolonies.coremod.gui.workerhuts.forcepickup");
    private static final Component LBL_INVENTORY =
            Component.translatable("container.inventory");

    public ArchitectTableScreen(final ArchitectTableMenu menu, final Inventory inv, final Component title)
    {
        super(menu, inv, title);
        this.imageWidth = BG_W;
        this.imageHeight = BG_H;
    }

    @Override
    protected void init()
    {
        super.init();

        final int left = this.leftPos;
        final int top  = this.topPos;

        // =========================
        // layouthutpageactionsminwoinv.xml
        // =========================

        // editName pos="150 11" size="15 15"
        this.addRenderableWidget(new TexturedButton(
                left + 150, top + 11,
                EDIT_W, EDIT_H,
                BTN_EDIT, EDIT_W, EDIT_H,
                () -> {
                    // MVP: editar nome é placeholder (PÓS-MVP: abrir input e sync com servidor)
                    if (minecraft != null && minecraft.player != null) {
                        minecraft.player.displayClientMessage(Component.literal("PLACEHOLDER"), false);
                    }
                }, Component.empty() // icon-only: no label visible, tooltip can be added later
        ));

        // build pos="30 110" size="129 17"
        this.addRenderableWidget(new TexturedButton(
                left + 30, top + 110,
                MEDIUM_LARGE_W, MEDIUM_LARGE_H,
                BTN_MEDIUM_LARGE, MEDIUM_LARGE_W, MEDIUM_LARGE_H,
                () -> {
                    // MVP: build action placeholder
                    if (minecraft != null && minecraft.player != null) {
                        minecraft.player.displayClientMessage(Component.literal("PLACEHOLDER"), false);
                    }
                }, Component.literal("PLACEHOLDER")
        ));

        // info pos="14 214" size="17 17"
        this.addRenderableWidget(new TexturedButton(
                left + 14, top + 214,
                ICON_17, ICON_17,
                ICON_INFO, ICON_17, ICON_17,
                () -> {
                    // MVP: info/help placeholder
                    if (minecraft != null && minecraft.player != null) {
                        minecraft.player.displayClientMessage(Component.literal("PLACEHOLDER"), false);
                    }
                }, Component.empty() // icon only, tooltip on hover later
        ));

        // =========================
        // layouthutpageactionsmin.xml
        // =========================

        // inventory pos="52 214" size="86 17"
        this.addRenderableWidget(new TexturedButton(
                left + 52, top + 214,
                MEDIUM_W, MEDIUM_H,
                BTN_MEDIUM, MEDIUM_W, MEDIUM_H,
                () -> {
                    // MVP: inventory placeholder
                    if (minecraft != null && minecraft.player != null) {
                        minecraft.player.displayClientMessage(Component.literal("PLACEHOLDER"), false);
                    }
                }, Component.literal("PLACEHOLDER")
        ));

        // allinventory pos="159 214" size="17 17"
        this.addRenderableWidget(new TexturedButton(
                left + 159, top + 214,
                ICON_17, ICON_17,
                ICON_CHEST, ICON_17, ICON_17,
                () -> {
                    // MVP: all inventory placeholder
                    if (minecraft != null && minecraft.player != null) {
                        minecraft.player.displayClientMessage(Component.literal("PLACEHOLDER"), false);
                    }
                }, Component.empty() // icon-only
        ));

        // =========================
        // layouthutpageactions.xml
        // =========================

        // hire pos="30 74" size="129 17"
        this.addRenderableWidget(new TexturedButton(
                left + 30, top + 74,
                MEDIUM_LARGE_W, MEDIUM_LARGE_H,
                BTN_MEDIUM_LARGE, MEDIUM_LARGE_W, MEDIUM_LARGE_H,
                () -> {
                    // MVP: hire/manage placeholder
                    if (minecraft != null && minecraft.player != null) {
                        minecraft.player.displayClientMessage(Component.literal("PLACEHOLDER"), false);
                    }
                }, Component.literal("PLACEHOLDER")
        ));

        // recall pos="30 92" size="129 17"
        this.addRenderableWidget(new TexturedButton(
                left + 30, top + 92,
                MEDIUM_LARGE_W, MEDIUM_LARGE_H,
                BTN_MEDIUM_LARGE, MEDIUM_LARGE_W, MEDIUM_LARGE_H,
                () -> {
                    // MVP: recall placeholder
                    if (minecraft != null && minecraft.player != null) {
                        minecraft.player.displayClientMessage(Component.literal("PLACEHOLDER"), false);
                    }
                }, Component.literal("PLACEHOLDER")
        ));

        // deliveryPrioDown pos="127 135" size="14 15"
        this.addRenderableWidget(new TexturedButton(
                left + 127, top + 135,
                MINI_W, MINI_H,
                BTN_MINI, MINI_W, MINI_H,
                () -> {
                    // Send decrement request to server (ID 1)
                    if (minecraft != null && minecraft.gameMode != null) {
                        minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 1);
                    }
                }, Component.literal("-")
        ));

        // deliveryPrioUp pos="144 135" size="14 15"
        this.addRenderableWidget(new TexturedButton(
                left + 144, top + 135,
                MINI_W, MINI_H,
                BTN_MINI, MINI_W, MINI_H,
                () -> {
                    // Send increment request to server (ID 2)
                    if (minecraft != null && minecraft.gameMode != null) {
                        minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 2);
                    }
                }, Component.literal("+")
        ));

        // Operate (MVP) pos="30 154" size="129 17" -> ID 0
        this.addRenderableWidget(new TexturedButton(
                left + 30, top + 154,
                MEDIUM_LARGE_W, MEDIUM_LARGE_H,
                BTN_MEDIUM_LARGE, MEDIUM_LARGE_W, MEDIUM_LARGE_H,
                () -> {
                    if (minecraft != null && minecraft.gameMode != null) {
                        minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 0);
                    }
                }, Component.literal("PLACEHOLDER")
        ));

        // Sawmill toggle (ID 3)
        this.addRenderableWidget(new TexturedButton(
                left + 30, top + 174,
                MEDIUM_LARGE_W, MEDIUM_LARGE_H,
                BTN_MEDIUM_LARGE, MEDIUM_LARGE_W, MEDIUM_LARGE_H,
                () -> {
                    if (minecraft != null && minecraft.gameMode != null) {
                        minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 3);
                    }
                }, Component.literal("PLACEHOLDER")
        ));

        // Teach selected categories (ID 4)
        this.addRenderableWidget(new TexturedButton(
                left + 30, top + 194,
                MEDIUM_LARGE_W, MEDIUM_LARGE_H,
                BTN_MEDIUM_LARGE, MEDIUM_LARGE_W, MEDIUM_LARGE_H,
                () -> {
                    if (minecraft != null && minecraft.gameMode != null) {
                        minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 4);
                    }
                }, Component.literal("PLACEHOLDER")
        ));

        // =========================
        // COISAS DO layoutbuilderres.xml (resources list) — MANTIDAS, MAS COMENTADAS
        // =========================
        /*
        // Botões “resourceAdd” do layoutbuilderres.xml:
        // list pos="13 62" size="164 163"
        // box height 30
        // button resourceAdd pos="100 12" size="14 15"
        final int listX = left + 13;
        final int listY = top + 62;

        for (int i = 0; i < 5; i++)
        {
            final int rowY = listY + (i * 30);

            this.addRenderableWidget(new TexturedButton(
                    listX + 100, rowY + 12,
                    MINI_W, MINI_H,
                    BTN_MINI, MINI_W, MINI_H,
                    () -> {}, Component.empty()
            ));
        }
        */
    }

    @Override
    protected void renderBg(final GuiGraphics g, final float partialTicks, final int mouseX, final int mouseY)
    {
        final int left = this.leftPos;
        final int top  = this.topPos;

        // Fundo
        g.blit(BG_PAPER, left, top, 0, 0, BG_W, BG_H, BG_W, BG_H);

        // Left side tabs (draw using MineColonies assets and correct sizes).
        // Use exact texture sizes: tab side = 32x26, module icons = 20x20.
        final int tabX = left - 28; // overlap so tab sits over paper edge
        int tabY = top + 24;

        g.blit(TAB_SIDE_1, tabX, tabY, 0, 0, 32, 26, 32, 26);
        g.blit(MODULE_INVENTORY, tabX + 6, tabY + 3, 0, 0, 20, 20, 20, 20);
        tabY += 28;
        g.blit(TAB_SIDE_2, tabX, tabY, 0, 0, 32, 26, 32, 26);
        g.blit(MODULE_STOCK, tabX + 6, tabY + 3, 0, 0, 20, 20, 20, 20);
        tabY += 28;
        g.blit(TAB_SIDE_3, tabX, tabY, 0, 0, 32, 26, 32, 26);
        g.blit(MODULE_SETTINGS, tabX + 6, tabY + 3, 0, 0, 20, 20, 20, 20);
        tabY += 28;
        g.blit(TAB_SIDE_4, tabX, tabY, 0, 0, 32, 26, 32, 26);
        g.blit(MODULE_INFO, tabX + 6, tabY + 3, 0, 0, 20, 20, 20, 20);

        // Header sketch (layouthutpageactionsminwoinv.xml)
        g.blit(SKETCH_LEFT, left + 24, top + 12, 0, 0, SKETCH_L_W, SKETCH_L_H, SKETCH_L_W, SKETCH_L_H);
        g.blit(SKETCH_CENTER, left + 30, top + 12, 0, 0, 130, 15, SKETCH_C_TEX_W, SKETCH_C_TEX_H);
        g.blit(SKETCH_RIGHT, left + 160, top + 12, 0, 0, SKETCH_R_W, SKETCH_R_H, SKETCH_R_W, SKETCH_R_H);

        // name pos="30 14" size="128 11" color="red"
        // (no MineColonies isso é o nome do prédio; aqui fica placeholder visual)
        drawCenteredInBoxNoShadowColor(g, Component.literal("Mesa do Arquiteto"), left + 30, top + 14, 128, 0xFFAA0000);

        // workerassigned label (layouthutpageactions.xml) pos="13 32" size="164 11"
        drawCenteredInBoxNoShadowColor(g, LBL_WORKER_ASSIGNED, left + 13, top + 32, 164, 0xFF000000);

        // ===== Labels dos botões (porque TexturedButton é só textura; o XML tinha label no botão) =====
        // hire pos="30 74" size="129 17"
        drawCenteredInRectNoShadow(g, LBL_MANAGE, left + 30, top + 74, MEDIUM_LARGE_W, MEDIUM_LARGE_H, 0xFF000000);

        // recall pos="30 92" size="129 17"
        drawCenteredInRectNoShadow(g, LBL_RECALL, left + 30, top + 92, MEDIUM_LARGE_W, MEDIUM_LARGE_H, 0xFF000000);

        // build pos="30 110" size="129 17"
        drawCenteredInRectNoShadow(g, LBL_BUILD_REPAIR, left + 30, top + 110, MEDIUM_LARGE_W, MEDIUM_LARGE_H, 0xFF000000);

        // inventory pos="52 214" size="86 17"
        drawCenteredInRectNoShadow(g, LBL_INVENTORY, left + 52, top + 214, MEDIUM_W, MEDIUM_H, 0xFF000000);

        // prioValue pos="30 135" size="95 15"
        // MVP: desenhar o valor local de Delivery Priority. Não há sincronização ainda (PÓS-MVP).
        if (this.font != null) {
            final Component prioText = Component.literal("Delivery Priority: " + this.menu.getDeliveryPriority());
            g.drawString(this.font, prioText, left + 30, top + 135, 0xFF000000, false);
        }

        // Sawmill enabled indicator
        if (this.font != null) {
            final Component sawText = Component.literal("Sawmill: " + (this.menu.isSawmillEnabled() ? "ON" : "OFF"));
            g.drawString(this.font, sawText, left + 30, top + 172, 0xFF000000, false);
        }

        // deliveryPrioDown/up labels removed — buttons render their own text now

        // ===== workers list area (pos="13 43" size="164 30") =====
        // MineColonies: list com view/box linewidth=0 e texto centralizado.
        // MVP: placeholder visual vazio (sem inventar nomes/slots).
        // (se quiser, depois desenhamos 1-2 linhas de texto real quando houver lista de workers)

        // =========================
        // COISAS DO layoutbuilderres.xml (resources list) — MANTIDAS, MAS COMENTADAS
        // =========================
        /*
        // TEXTOS do layoutbuilderres.xml:
        // constructionName pos="13 29" size="164 11"
        // stepprogress pos="13 40" size="164 11"
        // progress pos="13 51" size="164 11"
        drawCenteredInBoxNoShadowColor(g, Component.literal("Aguardando pedidos de construção"), left + 13, top + 29, 164, 0xFF000000);

        // LISTA resources pos="13 62" size="164 163"
        final int listX = left + 13;
        final int listY = top + 62;
        final int listW = 164;
        final int listH = 163;
        drawBoxOutline(g, listX, listY, listW, listH, 0xFF000000);
        */
    }

    // Mata labels vanilla (title + inventory name)
    @Override
    protected void renderLabels(final GuiGraphics g, final int mouseX, final int mouseY)
    {
        // intencionalmente vazio
    }

    @Override
    public void render(final GuiGraphics g, final int mouseX, final int mouseY, final float partialTicks)
    {
        this.renderBackground(g, mouseX, mouseY, partialTicks);
        super.render(g, mouseX, mouseY, partialTicks);
    }

    // ===== Helpers =====

    private void drawCenteredInBoxNoShadowColor(final GuiGraphics g, final Component text, final int x, final int y, final int boxW, final int argb)
    {
        if (text == null) return;
        final int w = this.font.width(text);
        final int cx = x + (boxW - w) / 2;
        g.drawString(this.font, text, cx, y, argb, false);
    }

    private void drawCenteredInRectNoShadow(final GuiGraphics g, final Component text, final int x, final int y, final int w, final int h, final int argb)
    {
        if (text == null) return;
        final int tw = this.font.width(text);
        final int tx = x + (w - tw) / 2;
        final int ty = y + (h - 8) / 2; // alinhamento vanilla aproximado (sem sombra)
        g.drawString(this.font, text, tx, ty, argb, false);
    }

    @SuppressWarnings("unused")
    private void drawBoxOutline(final GuiGraphics g, final int x, final int y, final int w, final int h, final int argb)
    {
        g.hLine(x, x + w - 1, y, argb);
        g.hLine(x, x + w - 1, y + h - 1, argb);
        g.vLine(x, y, y + h - 1, argb);
        g.vLine(x + w - 1, y, y + h - 1, argb);
    }
}
