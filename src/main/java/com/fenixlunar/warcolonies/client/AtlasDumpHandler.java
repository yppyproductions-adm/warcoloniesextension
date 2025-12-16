package com.fenixlunar.warcolonies.client;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.TextureAtlasStitchedEvent;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
// TextureStitchEvent not available in this runtime; using reflective handler signature

import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AtlasDumpHandler {
    private static final Logger LOGGER = LogUtils.getLogger();

    // Not registered via event bus in this build (API mismatch). Keep a reflective handler method
    @SubscribeEvent
    public void onTextureStitchPost(TextureAtlasStitchedEvent event) {
        try {
            // Use the atlas provided by the event
            Object atlas = null;
            try {
                atlas = event.getAtlas();
            } catch (Throwable ignored) {}

            List<Object> sprites = new ArrayList<>();
            if (atlas != null) {
                // try to find a method that returns a Collection of sprites
                for (Method m : atlas.getClass().getMethods()) {
                    if (m.getParameterCount() == 0 && Collection.class.isAssignableFrom(m.getReturnType())) {
                        try {
                            Object res = m.invoke(atlas);
                            if (res instanceof Collection) { sprites.addAll((Collection<?>) res); break; }
                        } catch (Throwable ignored) {}
                    }
                }
                // fallback: inspect fields
                if (sprites.isEmpty()) {
                    for (Field f : atlas.getClass().getDeclaredFields()) {
                        try { f.setAccessible(true); Object v = f.get(atlas); if (v instanceof Collection) { sprites.addAll((Collection<?>) v); break; } } catch (Throwable ignored) {}
                    }
                }
            }

            File out = new File("run/atlas-dump-minecolonies_gui.txt");
            try (PrintWriter pw = new PrintWriter(out)) {
                pw.println("AtlasDumpHandler: event class=" + event.getClass().getName());
                pw.println("Found atlas object: " + (atlas != null ? atlas.getClass().getName() : "null"));
                pw.println("Sprites count (collected reflectively): " + sprites.size());
                for (Object sprite : sprites) {
                    String name = "<unknown>";
                    try {
                        for (Method m : sprite.getClass().getMethods()) {
                            if (m.getParameterCount() == 0) {
                                String mn = m.getName().toLowerCase();
                                if (mn.contains("location") || mn.contains("name") || mn.contains("id")) {
                                    Object res = m.invoke(sprite);
                                    if (res != null) { name = res.toString(); break; }
                                }
                            }
                        }
                    } catch (Throwable ignored) {}

                    Integer x = null, y = null, w = null, h = null;
                    try {
                        for (Method m : sprite.getClass().getMethods()) {
                            if (m.getParameterCount() == 0) {
                                String mn = m.getName().toLowerCase();
                                if (mn.equals("getx") || mn.equals("x")) { Object r = m.invoke(sprite); if (r instanceof Number) x = ((Number) r).intValue(); }
                                if (mn.equals("gety") || mn.equals("y")) { Object r = m.invoke(sprite); if (r instanceof Number) y = ((Number) r).intValue(); }
                                if (mn.contains("width")) { Object r = m.invoke(sprite); if (r instanceof Number) w = ((Number) r).intValue(); }
                                if (mn.contains("height")) { Object r = m.invoke(sprite); if (r instanceof Number) h = ((Number) r).intValue(); }
                            }
                        }
                    } catch (Throwable ignored) {}

                    pw.printf("sprite: %s  x=%s y=%s w=%s h=%s\n", name, x, y, w, h);
                }
            }

            LOGGER.info("AtlasDumpHandler: wrote {}", out.getAbsolutePath());
        } catch (Throwable t) {
            LOGGER.warn("AtlasDumpHandler: failed to dump atlas: {}", t.toString());
            t.printStackTrace();
        }
    }
}
