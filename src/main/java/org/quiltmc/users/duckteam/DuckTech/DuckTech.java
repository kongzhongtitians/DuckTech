package org.quiltmc.users.duckteam.DuckTech;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.quiltmc.users.duckteam.DuckTech.blocks.*;
import org.quiltmc.users.duckteam.DuckTech.config.DTConfig;
import org.quiltmc.users.duckteam.DuckTech.gui.DTMenu;
import org.quiltmc.users.duckteam.DuckTech.items.*;
import org.quiltmc.users.duckteam.DuckTech.recipe.*;
import org.quiltmc.users.duckteam.DuckTech.sounds.DTSounds;

@Mod(DuckTech.MOD_ID)
public class DuckTech {
    public static final String MODID = "ducktech";
    public static final String MOD_ID = MODID;

    public static final Logger LOGGER = LogManager.getLogger();

    public DuckTech(FMLJavaModLoadingContext context)
    {
        DTConfig.register();
        IEventBus modEventBus = context.getModEventBus();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onConstructMod);

        LOGGER.info("|----l  |    |  /----  |  / --------- |----  /----  |");
        LOGGER.info("|     | |    | |     l | /      |     |     |     l |");
        LOGGER.info("|     | |    | |       |/       |     |     |       |");
        LOGGER.info("|     | |    | |       |        |     |---- |       |----、");
        LOGGER.info("|     | |    | |       |l       |     |     |       |    |");
        LOGGER.info("|     | |    | |     / | l      |     |     |     / |    |");
        LOGGER.info("|----/  l----|  l----  |  l     |     |----  l----  |    |");
        LOGGER.info("DuckTech Version:Internal Testing");

        DTCreativeTab.CREATIVE_TABS.register(modEventBus);
        DTSounds.SOUND_EVENTS.register(modEventBus);
        DTBlocks.BLOCKS.register(modEventBus);
        DTItems.ITEMS.register(modEventBus);
        DTBlockEntity.BLOCK_ENTITY_TYPES.register(modEventBus);
        DTRecipe.RECIPE_TYPES.register(modEventBus);
        DTRecipeSerializers.SERIALIZERS.register(modEventBus);
        DTMenu.MENUS.register(modEventBus);
        LOGGER.info("DuckTech Has Loaded");
    }
    private void onConstructMod(final FMLConstructModEvent event) {
        // 在这里处理 FMLConstructModEvent
    }
}
