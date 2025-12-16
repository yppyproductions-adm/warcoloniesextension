package com.fenixlunar.warcolonies;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = WarColoniesExtension.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = WarColoniesExtension.MODID, value = Dist.CLIENT)
public class WarColoniesExtensionClient {
    public WarColoniesExtensionClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        WarColoniesExtension.LOGGER.info("HELLO FROM CLIENT SETUP");
        WarColoniesExtension.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        // Register atlas dump handler to inspect texture stitch atlases at runtime
        try {
            net.neoforged.neoforge.common.NeoForge.EVENT_BUS.register(new com.fenixlunar.warcolonies.client.AtlasDumpHandler());
        } catch (Exception e) {
            WarColoniesExtension.LOGGER.warn("Failed to register AtlasDumpHandler: {}", e.toString());
        }
        try {
            net.neoforged.neoforge.common.NeoForge.EVENT_BUS.register(new com.fenixlunar.warcolonies.client.ClientAtlasDumper());
        } catch (Exception e) {
            WarColoniesExtension.LOGGER.warn("Failed to register ClientAtlasDumper: {}", e.toString());
        }
    }
}
