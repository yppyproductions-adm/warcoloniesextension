package com.fenixlunar.warcolonies.client;

import com.fenixlunar.warcolonies.client.widgets.TexturedButton;
import com.fenixlunar.warcolonies.menu.ArchitectTableMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.server.packs.resources.Resource;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;

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

        // Use the canonical images from the `warcoloniesextension` assets (we store MineColonies images here)
        private static final String NS = "warcoloniesextension";

    // Fundo (layouthutpageactionsminwoinv.xml usa builder_paper.png)
    private static final ResourceLocation BG_PAPER =
            ResourceLocation.parse(NS + ":textures/gui/builderhut/builder_paper.png");

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
            ResourceLocation.parse("warcoloniesextension:textures/gui/red_wax_information.png");
    private static final ResourceLocation ICON_CHEST =
            ResourceLocation.parse("warcoloniesextension:textures/gui/chest.png");

    // Side tab textures (use images located in our `warcoloniesextension` assets)
        // Use padded variants for experiment A1 (1px transparent border) to check atlas bleeding
        private static final ResourceLocation TAB_SIDE_1 =
            ResourceLocation.parse("warcoloniesextension:textures/gui/modules/tab_side1.png");
        private static final ResourceLocation TAB_SIDE_2 =
            ResourceLocation.parse("warcoloniesextension:textures/gui/modules/tab_side2.png");
        private static final ResourceLocation TAB_SIDE_3 =
            ResourceLocation.parse("warcoloniesextension:textures/gui/modules/tab_side3.png");
        private static final ResourceLocation TAB_SIDE_4 =
            ResourceLocation.parse("warcoloniesextension:textures/gui/modules/tab_side4.png");

        // Runtime (non-atlas) resource locations we register to bypass atlas packing
        private static final ResourceLocation TAB_SIDE_1_RUNTIME =
            ResourceLocation.parse("warcoloniesextension:textures/gui/modules/tab_side1_runtime.png");
        private static final ResourceLocation TAB_SIDE_2_RUNTIME =
            ResourceLocation.parse("warcoloniesextension:textures/gui/modules/tab_side2_runtime.png");
        private static final ResourceLocation TAB_SIDE_3_RUNTIME =
            ResourceLocation.parse("warcoloniesextension:textures/gui/modules/tab_side3_runtime.png");
        private static final ResourceLocation TAB_SIDE_4_RUNTIME =
            ResourceLocation.parse("warcoloniesextension:textures/gui/modules/tab_side4_runtime.png");

        private boolean runtimeTabTexturesLoaded = false;

    private static final ResourceLocation MODULE_INFO =
            ResourceLocation.parse("warcoloniesextension:textures/gui/modules/info.png");
    private static final ResourceLocation MODULE_SETTINGS =
            ResourceLocation.parse("warcoloniesextension:textures/gui/modules/settings.png");
    private static final ResourceLocation MODULE_STOCK =
            ResourceLocation.parse("warcoloniesextension:textures/gui/modules/stock.png");
    private static final ResourceLocation MODULE_INVENTORY =
            ResourceLocation.parse("warcoloniesextension:textures/gui/modules/inventory.png");

    // Runtime-swappable resources — default to our packaged assets.
    private ResourceLocation resBgPaper = BG_PAPER;
    private ResourceLocation resSketchLeft = SKETCH_LEFT;
    private ResourceLocation resSketchCenter = SKETCH_CENTER;
    private ResourceLocation resSketchRight = SKETCH_RIGHT;
    private ResourceLocation resBtnEdit = BTN_EDIT;
    private ResourceLocation resBtnMediumLarge = BTN_MEDIUM_LARGE;
    private ResourceLocation resBtnMedium = BTN_MEDIUM;
    private ResourceLocation resBtnMini = BTN_MINI;
    private ResourceLocation resIconInfo = ICON_INFO;
    private ResourceLocation resIconChest = ICON_CHEST;

        // Tamanhos reais (MineColonies)
        private static final int BG_W = 190;
        private static final int BG_H = 244;

        // For the Builder-hut window (reference XML) we use the explicit size
        // and do NOT apply BlockUI lightbox/auto-size. Keep false to match
        // MineColonies' builder window behavior.
        private static final boolean USE_BLOCKUI_STYLE = false;

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

    // debug: only dump once per screen open
    private boolean dumpedMinecoloniesAtlas = false;
    // Debug render modes for isolating artifacts:
    // 0 = normal, 1 = skip tabs, 2 = draw tabs as solid boxes, 3 = skip paper, 4 = skip sketches
    private static final int DEBUG_RENDER_MODE = 0;

    @Override
    protected void init()
    {
        super.init();

        // Builder-hut reference uses explicit size 190x244 (layouthutpageactionsminwoinv.xml).
        // Use those exact values and keep default centering; do not auto-resize or apply lightbox.
        this.imageWidth = BG_W;
        this.imageHeight = BG_H;

        final int left = this.leftPos;
        final int top  = this.topPos;

        // =========================
        // layouthutpageactionsminwoinv.xml
        // =========================

        // editName pos="150 11" size="15 15"
        this.addRenderableWidget(new TexturedButton(
            left + 150, top + 11,
            EDIT_W, EDIT_H,
            resBtnEdit, EDIT_W, EDIT_H,
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
            resBtnMediumLarge, MEDIUM_LARGE_W, MEDIUM_LARGE_H,
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
            resIconInfo, ICON_17, ICON_17,
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
            resBtnMedium, MEDIUM_W, MEDIUM_H,
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
            resIconChest, ICON_17, ICON_17,
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
        
        // For the builder-hut reference we intentionally keep the full BG_H.
    }

    @Override
    protected void renderBg(final GuiGraphics g, final float partialTicks, final int mouseX, final int mouseY)
    {
        final int left = this.leftPos;
        final int top  = this.topPos;

        // Fundo (desenha o papel no tamanho canônico do Builder: 190x244)
        g.blit(resBgPaper, left, top, 0, 0, BG_W, BG_H, BG_W, BG_H);

        // Debug: try to dump the minecolonies atlas once to run/atlas-dump-minecolonies_gui.png
        if (!dumpedMinecoloniesAtlas) {
            dumpedMinecoloniesAtlas = true;
            try {
                final ResourceLocation atlas = ResourceLocation.parse("minecolonies:textures/atlas/minecolonies_gui.png");
                if (this.minecraft != null && this.minecraft.getTextureManager() != null) {
                    try {
                        Object tex = this.minecraft.getTextureManager().getTexture(atlas);
                        if (tex != null) {
                            int texId = -1;
                            for (Field f : tex.getClass().getDeclaredFields()) {
                                if (f.getType() == int.class) {
                                    f.setAccessible(true);
                                    int v = f.getInt(tex);
                                    if (v > 0) { texId = v; break; }
                                }
                            }
                            if (texId > 0) {
                                GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
                                int width = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
                                int height = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
                                if (width > 0 && height > 0) {
                                    ByteBuffer buf = BufferUtils.createByteBuffer(width * height * 4);
                                    GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, buf);
                                    BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                                    for (int y = 0; y < height; y++) {
                                        for (int x = 0; x < width; x++) {
                                            int i = (x + (height - 1 - y) * width) * 4;
                                            int b = buf.get(i) & 0xFF;
                                            int gcol = buf.get(i + 1) & 0xFF;
                                            int r = buf.get(i + 2) & 0xFF;
                                            int a = buf.get(i + 3) & 0xFF;
                                            int argb = (a << 24) | (r << 16) | (gcol << 8) | b;
                                            img.setRGB(x, y, argb);
                                        }
                                    }
                                    File out = new File("run/atlas-dump-minecolonies_gui.png");
                                    ImageIO.write(img, "PNG", out);
                                }
                            } else {
                                // Could not find texture GL id via reflection; skipping fallback bind.
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Left side tabs (draw using MineColonies assets and correct sizes).
        // Use exact texture sizes: tab side = 32x26, module icons = 20x20.
        final int tabX = left - 28; // overlap so tab sits over paper edge
        int tabY = top + 24;
        // Ensure runtime (non-atlas) consolidated tab texture is loaded (bypass atlas/mipmaps)
        ensureRuntimeTabTexturesLoaded();

        // Debug: allow skipping or drawing boxes to isolate artifact
        if (DEBUG_RENDER_MODE == 1) {
            // skip drawing tabs entirely
        } else if (DEBUG_RENDER_MODE == 2) {
            // draw solid boxes where tabs would be (semi-transparent red)
            g.fill(tabX, tabY, tabX + 32, tabY + 26, 0x80FF0000);
            g.fill(tabX + 0, tabY + 28, tabX + 32, tabY + 28 + 26, 0x8080FF00);
            g.fill(tabX + 0, tabY + 56, tabX + 32, tabY + 56 + 26, 0x800000FF);
            g.fill(tabX + 0, tabY + 84, tabX + 32, tabY + 84 + 26, 0x80FFFF00);
        } else {
            // UV inset: sample 1px inset from the runtime (non-atlas) texture to avoid any sampling bleed
            g.blit(TAB_SIDE_1_RUNTIME, tabX + 1, tabY + 1, 1, 1, 30, 24, 32, 26);
            g.blit(MODULE_INVENTORY, tabX + 7, tabY + 4, 0, 0, 20, 20, 20, 20);
        }
        tabY += 28;
        if (DEBUG_RENDER_MODE == 2) {
            // already drew all four boxes above for solid-box mode; advance tabY
        } else if (DEBUG_RENDER_MODE != 1) {
            g.blit(TAB_SIDE_2_RUNTIME, tabX + 1, tabY + 1, 1, 1, 30, 24, 32, 26);
            g.blit(MODULE_STOCK, tabX + 7, tabY + 4, 0, 0, 20, 20, 20, 20);
        }
        
        tabY += 28;
        if (DEBUG_RENDER_MODE != 1 && DEBUG_RENDER_MODE != 2) {
            g.blit(TAB_SIDE_3_RUNTIME, tabX + 1, tabY + 1, 1, 1, 30, 24, 32, 26);
            g.blit(MODULE_SETTINGS, tabX + 7, tabY + 4, 0, 0, 20, 20, 20, 20);
        }
        
        tabY += 28;
        if (DEBUG_RENDER_MODE != 1 && DEBUG_RENDER_MODE != 2) {
            g.blit(TAB_SIDE_4_RUNTIME, tabX + 1, tabY + 1, 1, 1, 30, 24, 32, 26);
            g.blit(MODULE_INFO, tabX + 7, tabY + 4, 0, 0, 20, 20, 20, 20);
        }
        

        // Header sketch (layouthutpageactionsminwoinv.xml)
        g.blit(resSketchLeft, left + 24, top + 12, 0, 0, SKETCH_L_W, SKETCH_L_H, SKETCH_L_W, SKETCH_L_H);
        g.blit(resSketchCenter, left + 30, top + 12, 0, 0, 130, 15, SKETCH_C_TEX_W, SKETCH_C_TEX_H);
        g.blit(resSketchRight, left + 160, top + 12, 0, 0, SKETCH_R_W, SKETCH_R_H, SKETCH_R_W, SKETCH_R_H);

        // name pos="30 14" size="128 11" color="red"
        // (no MineColonies isso é o nome do prédio; aqui fica placeholder visual)
        drawCenteredInBoxNoShadowColor(g, Component.literal("Mesa do Arquiteto"), left + 30, top + 14, 128, 0xFFAA0000);

        // workerassigned label (layouthutpageactions.xml) pos="13 32" size="164 11"
        drawCenteredInBoxNoShadowColor(g, LBL_WORKER_ASSIGNED, left + 13, top + 32, 164, 0xFF000000);

        // Note: button labels are intentionally omitted here to avoid drawing
        // text beneath the textured widgets. Text for these actions is
        // rendered by the widgets themselves (or will be added later).

        // prioValue pos="30 135" size="95 15"
        // MVP: desenhar o valor local de Delivery Priority. Não há sincronização ainda (PÓS-MVP).
        if (this.font != null) {
            final Component prioText = Component.literal("Delivery Priority: " + this.menu.getDeliveryPriority());
            g.drawString(this.font, prioText, left + 30, top + 135, 0xFF000000, false);
        }

        // Sawmill enabled indicator removed to avoid overlap artifacts behind buttons

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

    // Load the `_filled.png` tab images as runtime textures (bypass atlas) and register them
    private void ensureRuntimeTabTexturesLoaded() {
        if (runtimeTabTexturesLoaded) return;
        runtimeTabTexturesLoaded = true;
        try {
            if (this.minecraft == null || this.minecraft.getResourceManager() == null || this.minecraft.getTextureManager() == null) return;

            // Use packaged tab textures (no runtime dependency on MineColonies files)
            loadAndRegister(TAB_SIDE_1, TAB_SIDE_1_RUNTIME);
            loadAndRegister(TAB_SIDE_2, TAB_SIDE_2_RUNTIME);
            loadAndRegister(TAB_SIDE_3, TAB_SIDE_3_RUNTIME);
            loadAndRegister(TAB_SIDE_4, TAB_SIDE_4_RUNTIME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // No runtime dependency on MineColonies files: we use copied values from
    // the MineColonies XMLs that are present in the repo to emulate behaviour.

    // Return a ResourceLocation for a tab image, preferring MineColonies resource namespace.
    private ResourceLocation findTabSource(final int idx) {
        final String mcPath = "minecolonies:textures/gui/modules/tab_side" + idx + ".png";
        final ResourceLocation mc = ResourceLocation.parse(mcPath);
        try {
            if (this.minecraft != null && this.minecraft.getResourceManager() != null) {
                java.util.Optional<Resource> opt = this.minecraft.getResourceManager().getResource(mc);
                if (opt.isPresent()) return mc;
            }
        } catch (Exception ignored) {}
        // fallback to packaged resource
        switch (idx) {
            case 1: return TAB_SIDE_1;
            case 2: return TAB_SIDE_2;
            case 3: return TAB_SIDE_3;
            default: return TAB_SIDE_4;
        }
    }

    private void loadAndRegister(final ResourceLocation src, final ResourceLocation target) {
        try {
            java.util.Optional<Resource> opt = this.minecraft.getResourceManager().getResource(src);
            if (opt.isEmpty()) return;
            Resource res = opt.get();
            try (InputStream is = res.open()) {
                NativeImage img = NativeImage.read(is);
                if (img != null) {
                    DynamicTexture dt = new DynamicTexture(img);
                    try {
                        // Prefer the explicit API when available
                        dt.setFilter(false, false);
                    } catch (Throwable ignored) {}
                    this.minecraft.getTextureManager().register(target, dt);
                    // Try to force sampling/filtering flags on the registered texture (best-effort)
                    try {
                        Object tex = this.minecraft.getTextureManager().getTexture(target);
                        if (tex != null) {
                            // Try common API methods first
                            try {
                                java.lang.reflect.Method m = tex.getClass().getMethod("setFilter", boolean.class, boolean.class);
                                m.setAccessible(true);
                                m.invoke(tex, false, false);
                            } catch (Throwable ignored) {}
                            try {
                                java.lang.reflect.Method m2 = tex.getClass().getMethod("setBlurMipmap", boolean.class, boolean.class);
                                m2.setAccessible(true);
                                m2.invoke(tex, false, false);
                            } catch (Throwable ignored) {}
                            try {
                                java.lang.reflect.Method m3 = tex.getClass().getMethod("setClamp", boolean.class);
                                m3.setAccessible(true);
                                m3.invoke(tex, true);
                            } catch (Throwable ignored) {}

                            // Fallback: try to find integer GL id field and set gl params directly
                            try {
                                int texId = -1;
                                for (java.lang.reflect.Field f : tex.getClass().getDeclaredFields()) {
                                    if (f.getType() == int.class) {
                                        f.setAccessible(true);
                                        int v = f.getInt(tex);
                                        if (v > 0) { texId = v; break; }
                                    }
                                }
                                if (texId > 0) {
                                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
                                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
                                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
                                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
                                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
                                }
                            } catch (Throwable ignored) {}
                        }
                    } catch (Throwable ignored) {}
                }
            }
        } catch (Exception e) {
            // best-effort: log and continue
            e.printStackTrace();
        }
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
        // For the Builder reference we do not draw a BlockUI lightbox; use
        // the standard background rendering and draw the GUI on top.
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
