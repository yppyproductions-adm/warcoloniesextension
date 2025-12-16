package com.fenixlunar.warcolonies.client;

import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;

import java.nio.file.Path;

/**
 * One-shot dumper that writes all texture atlas sheets to disk for inspection.
 */
public class ClientAtlasDumper {
    private boolean dumped = false;

    @SubscribeEvent
    public void onClientTick(ClientTickEvent.Post event) {
        if (dumped) return;
        dumped = true;

        Minecraft mc = Minecraft.getInstance();
        // Schedule on render thread to ensure GL context is available
        mc.execute(() -> {
            try {
                Path out = Path.of("run/atlas-dumps");
                mc.getTextureManager().dumpAllSheets(out);
                System.out.println("ClientAtlasDumper: dumped atlases to " + out.toAbsolutePath());
            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
