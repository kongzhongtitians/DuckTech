package org.quiltmc.users.duckteam.DuckTech.gui;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.quiltmc.users.duckteam.DuckTech.DuckTech;
import org.quiltmc.users.duckteam.DuckTech.gui.advance_shredder.AdvanceShredderScreen;
import org.quiltmc.users.duckteam.DuckTech.gui.air_purifier.AirPurifierScreen;
import org.quiltmc.users.duckteam.DuckTech.gui.essence_blast_furnace.EssenceBlastFurnaceScreen;
import org.quiltmc.users.duckteam.DuckTech.gui.essence_conversion_machine.EssenceConversionMachineScreen;
import org.quiltmc.users.duckteam.DuckTech.gui.essence_earth_furnace.EssenceEarthFurnaceScreen;
import org.quiltmc.users.duckteam.DuckTech.gui.essence_furnace.EssenceFurnaceScreen;
import org.quiltmc.users.duckteam.DuckTech.gui.expulsion_machine.ExpulsionMachineScreen;
import org.quiltmc.users.duckteam.DuckTech.gui.fe2thermal_essence_machine.FE2ThermalEssenceMachineScreen;
import org.quiltmc.users.duckteam.DuckTech.gui.frozen_essence_maker.FrozenEssenceMakerScreen;
import org.quiltmc.users.duckteam.DuckTech.gui.juice_extractor.JuiceExtractorScreen;
import org.quiltmc.users.duckteam.DuckTech.gui.levitation.LevitationMachineScreen;
import org.quiltmc.users.duckteam.DuckTech.gui.injection_machine.InjectionMachineScreen;
import org.quiltmc.users.duckteam.DuckTech.gui.thermal_essence_maker.ThermalEssenceMakerScreen;
import net.minecraft.client.gui.screens.inventory.FurnaceScreen;

@Mod.EventBusSubscriber(modid = DuckTech.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ScreenRegister {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(DTMenu.ADVANCE_SHREDDER_MENU.get(), AdvanceShredderScreen::new);
            MenuScreens.register(DTMenu.LEVITATION_MACHINE_MENU.get(), LevitationMachineScreen::new);
            MenuScreens.register(DTMenu.ESSENCE_FURNACE_MENU.get(), EssenceFurnaceScreen::new);
            MenuScreens.register(DTMenu.ESSENCE_CONVERSION_MACHINE_MENU.get(), EssenceConversionMachineScreen::new);
            MenuScreens.register(DTMenu.INJECTION_MACHINE_MENU.get(), InjectionMachineScreen::new);
            MenuScreens.register(DTMenu.FE2THERMAL_ESSENCE_MACHINE_MENU.get(), FE2ThermalEssenceMachineScreen::new);
            MenuScreens.register(DTMenu.THERMAL_ESSENCE_MAKER.get(), ThermalEssenceMakerScreen::new);
            MenuScreens.register(DTMenu.FROZEN_ESSENCE_MAKER_MENU.get(), FrozenEssenceMakerScreen::new);
            MenuScreens.register(DTMenu.ESSENCE_EARTH_FURNACE.get(), EssenceEarthFurnaceScreen::new);
            MenuScreens.register(DTMenu.JUICE_EXTRACTOR_MENU.get(), JuiceExtractorScreen::new);
            MenuScreens.register(DTMenu.ESSENCE_BLAST_FURNACE_MENU.get(), EssenceBlastFurnaceScreen::new);
            MenuScreens.register(DTMenu.EXPULSION_MACHINE.get(), ExpulsionMachineScreen::new);
            MenuScreens.register(DTMenu.AIR_PURIFIER_MENU.get(), AirPurifierScreen::new);
        });
    }
}
