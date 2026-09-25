package gd.rf.kongzhongtitian.DuckTech.blocks.gui;

import gd.rf.kongzhongtitian.DuckTech.DuckTech;
import gd.rf.kongzhongtitian.DuckTech.blocks.machines.advance_shredder.AdvanceShredderScreen;
import gd.rf.kongzhongtitian.DuckTech.blocks.machines.levitation_machine.LevitationMachineScreen;
import gd.rf.kongzhongtitian.DuckTech.blocks.machines.essence_furnace.EssenceFurnaceScreen;
import gd.rf.kongzhongtitian.DuckTech.blocks.machines.essence_conversion_machine.EssenceConversionMachineScreen;
import gd.rf.kongzhongtitian.DuckTech.blocks.machines.injection_machine.InjectionMachineScreen;
import gd.rf.kongzhongtitian.DuckTech.blocks.machines.frozen_essence_maker.FrozenEssenceMakerScreen;
import gd.rf.kongzhongtitian.DuckTech.blocks.machines.fe2thermal_essence_machine.FE2ThermalEssenceMachineScreen;
import gd.rf.kongzhongtitian.DuckTech.blocks.machines.thermal_essence_maker.ThermalEssenceMakerScreen;
import gd.rf.kongzhongtitian.DuckTech.blocks.machines.essence_earth_furnace.EssenceEarthFurnaceScreen;
import gd.rf.kongzhongtitian.DuckTech.blocks.machines.juice_extractor.JuiceExtractorScreen;
import gd.rf.kongzhongtitian.DuckTech.blocks.machines.essence_blast_furnace.EssenceBlastFurnaceScreen;
import gd.rf.kongzhongtitian.DuckTech.blocks.machines.expulsion_machine.ExpulsionMachineScreen;
import gd.rf.kongzhongtitian.DuckTech.blocks.machines.air_purifier.AirPurifierScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

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
