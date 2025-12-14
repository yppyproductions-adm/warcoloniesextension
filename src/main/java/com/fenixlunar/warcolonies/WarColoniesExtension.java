package com.fenixlunar.warcolonies;

import br.com.warcolonies.command.WarDebugCommand;
import br.com.warcolonies.command.WarTestCommand;
import br.com.warcolonies.core.WarColoniesMod;
import com.fenixlunar.warcolonies.block.ArchitectTableBlock;
import com.fenixlunar.warcolonies.client.ArchitectTableScreen;
import com.fenixlunar.warcolonies.menu.ArchitectTableMenu;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

@Mod(WarColoniesExtension.MODID)
public class WarColoniesExtension
{
    public static final String MODID = "warcoloniesextension";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // Itens/blocos de exemplo (mantendo o que você já tinha)
    public static final DeferredBlock<Block> EXAMPLE_BLOCK =
            BLOCKS.registerSimpleBlock("example_block",
                    BlockBehaviour.Properties.of().mapColor(MapColor.STONE));

    public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem("example_block", EXAMPLE_BLOCK);

    public static final DeferredItem<Item> EXAMPLE_ITEM =
            ITEMS.registerSimpleItem("example_item",
                    new Item.Properties().food(
                            new FoodProperties.Builder()
                                    .alwaysEdible()
                                    .nutrition(1)
                                    .saturationModifier(2f)
                                    .build()
                    ));

    // Mesa do Arquiteto
    public static final DeferredBlock<Block> ARCHITECT_TABLE_BLOCK =
            BLOCKS.register("architect_table",
                    () -> new ArchitectTableBlock(
                            BlockBehaviour.Properties.of()
                                    .mapColor(MapColor.WOOD)
                                    .strength(2.5F)
                                    .noOcclusion()
                    ));

    public static final DeferredItem<BlockItem> ARCHITECT_TABLE_ITEM =
            ITEMS.registerSimpleBlockItem("architect_table", ARCHITECT_TABLE_BLOCK);

    // Menu (container) da mesa do Arquiteto
    public static final DeferredHolder<MenuType<?>, MenuType<ArchitectTableMenu>> ARCHITECT_TABLE_MENU =
            MENUS.register("architect_table",
                    () -> IMenuTypeExtension.create((containerId, inv, data) ->
                            new ArchitectTableMenu(containerId, inv)));

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB =
            CREATIVE_MODE_TABS.register("example_tab",
                    () -> CreativeModeTab.builder()
                            .title(Component.translatable("itemGroup.warcoloniesextension"))
                            .withTabsBefore(CreativeModeTabs.COMBAT)
                            .icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
                            .displayItems((params, output) -> {
                                output.accept(EXAMPLE_ITEM.get());
                                output.accept(ARCHITECT_TABLE_ITEM.get());
                            })
                            .build()
            );

    public WarColoniesExtension(IEventBus modEventBus, ModContainer modContainer)
    {
        // Setup
        modEventBus.addListener(this::commonSetup);

        // Registries
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        MENUS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        // Eventos do jogo
        NeoForge.EVENT_BUS.register(this);

        // Creative tab vanilla extra
        modEventBus.addListener(this::addCreative);

        // ✅ Registrar a tela do menu (CLIENTE)
        modEventBus.addListener(this::onRegisterScreens);

        // Config
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        WarColoniesMod.init();

        NeoForge.EVENT_BUS.register(new br.com.warcolonies.core.StrategicTickHandler(
                WarColoniesMod.getEconomyManager(),
                WarColoniesMod.getLogisticsManager(),
                WarColoniesMod.getBuildingManager(),
                WarColoniesMod.getConquestManager()
        ));

        LOGGER.info("HELLO FROM COMMON SETUP");
        if (Config.LOG_DIRT_BLOCK.getAsBoolean())
        {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }
        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());
        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }

    private void addCreative(final BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
        {
            event.accept(EXAMPLE_BLOCK_ITEM);
        }
    }

    // ✅ Aqui é o lugar CERTO (CLIENT) pra dizer: "quando abrir o menu X, usa a tela Y"
    private void onRegisterScreens(final RegisterMenuScreensEvent event)
    {
        event.register(ARCHITECT_TABLE_MENU.get(), ArchitectTableScreen::new);
    }

    @SubscribeEvent
    public void onServerStarting(final ServerStartingEvent event)
    {
        LOGGER.info("HELLO from server starting");
    }

    @SubscribeEvent
    public void onRegisterCommands(final RegisterCommandsEvent event)
    {
        WarDebugCommand.register(event.getDispatcher());
        WarTestCommand.register(event.getDispatcher());
    }
}
