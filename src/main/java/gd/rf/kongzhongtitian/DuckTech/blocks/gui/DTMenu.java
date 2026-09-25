package gd.rf.kongzhongtitian.DuckTech.blocks.gui;

import gd.rf.kongzhongtitian.DuckTech.DuckTech;
import gd.rf.kongzhongtitian.DuckTech.blocks.machines.advance_shredder.AdvanceShredderMenu;
import gd.rf.kongzhongtitian.DuckTech.blocks.machines.air_purifier.AirPurifierMenu;
import gd.rf.kongzhongtitian.DuckTech.blocks.machines.essence_blast_furnace.EssenceBlastFurnaceMenu;
import gd.rf.kongzhongtitian.DuckTech.blocks.machines.essence_conversion_machine.EssenceConversionMachineMenu;
import gd.rf.kongzhongtitian.DuckTech.blocks.machines.essence_earth_furnace.EssenceEarthFurnaceMenu;
import gd.rf.kongzhongtitian.DuckTech.blocks.machines.essence_furnace.EssenceFurnaceMenu;
import gd.rf.kongzhongtitian.DuckTech.blocks.machines.expulsion_machine.ExpulsionMachineMenu;
import gd.rf.kongzhongtitian.DuckTech.blocks.machines.fe2thermal_essence_machine.FE2ThermalEssenceMachineBlockEntity;
import gd.rf.kongzhongtitian.DuckTech.blocks.machines.fe2thermal_essence_machine.FE2ThermalEssenceMachineMenu;
import gd.rf.kongzhongtitian.DuckTech.blocks.machines.frozen_essence_maker.FrozenEssenceMakerBlockEntity;
import gd.rf.kongzhongtitian.DuckTech.blocks.machines.frozen_essence_maker.FrozenEssenceMakerMenu;
import gd.rf.kongzhongtitian.DuckTech.blocks.machines.injection_machine.InjectionMachineMenu;
import gd.rf.kongzhongtitian.DuckTech.blocks.machines.juice_extractor.JuiceExtractorBlockEntity;
import gd.rf.kongzhongtitian.DuckTech.blocks.machines.juice_extractor.JuiceExtractorMenu;
import gd.rf.kongzhongtitian.DuckTech.blocks.machines.levitation_machine.LevitationMachineMenu;
import gd.rf.kongzhongtitian.DuckTech.blocks.machines.thermal_essence_maker.ThermalEssenceMakerBlockEntity;
import gd.rf.kongzhongtitian.DuckTech.blocks.machines.thermal_essence_maker.ThermalEssenceMakerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static net.minecraftforge.registries.ForgeRegistries.MENU_TYPES;


public class DTMenu {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(MENU_TYPES, DuckTech.MODID);

    public static final RegistryObject<MenuType<AdvanceShredderMenu>> ADVANCE_SHREDDER_MENU =
            MENUS.register("advance_shredder_menu", () -> IForgeMenuType.create(AdvanceShredderMenu::new
        ));

    public static final RegistryObject<MenuType<LevitationMachineMenu>> LEVITATION_MACHINE_MENU =
            MENUS.register("levitation_machine_menu", () -> IForgeMenuType.create(LevitationMachineMenu::new));

    public static final RegistryObject<MenuType<EssenceFurnaceMenu>> ESSENCE_FURNACE_MENU =
            MENUS.register("essence_furnace_menu", () -> IForgeMenuType.create(EssenceFurnaceMenu::new));

    public static final RegistryObject<MenuType<EssenceConversionMachineMenu>> ESSENCE_CONVERSION_MACHINE_MENU =
            MENUS.register("essence_conversion_machine_menu", () -> IForgeMenuType.create(EssenceConversionMachineMenu::new));

    public static final RegistryObject<MenuType<InjectionMachineMenu>> INJECTION_MACHINE_MENU =
            MENUS.register("injection_machine_menu", () -> IForgeMenuType.create(InjectionMachineMenu::new));

    public static final RegistryObject<MenuType<FE2ThermalEssenceMachineMenu>> FE2THERMAL_ESSENCE_MACHINE_MENU =
            MENUS.register("fe2thermal_essence_machine_menu",
                    () -> IForgeMenuType.create((windowId, inv, data) -> {
                        BlockPos pos = data.readBlockPos();
                        BlockEntity be = inv.player.level().getBlockEntity(pos);
                        if (be instanceof FE2ThermalEssenceMachineBlockEntity generator) {
                            return new FE2ThermalEssenceMachineMenu(windowId, inv, generator);
                        }
                        return null;
                    }));

    public static final RegistryObject<MenuType<ThermalEssenceMakerMenu>> THERMAL_ESSENCE_MAKER =
            MENUS.register("thermal_essence_maker_menu",
                    () -> IForgeMenuType.create((windowId, inv, data) -> {
                        BlockPos pos = data.readBlockPos();
                        BlockEntity be = inv.player.level().getBlockEntity(pos);
                        if (be instanceof ThermalEssenceMakerBlockEntity generator) {
                            return new ThermalEssenceMakerMenu(windowId, inv, generator);
                        }
                        return null;
                    }));

    public static final RegistryObject<MenuType<FrozenEssenceMakerMenu>> FROZEN_ESSENCE_MAKER_MENU =
            MENUS.register("frozen_essence_maker_menu",
                    () -> IForgeMenuType.create((windowId, inv, data) -> {
                        BlockPos pos = data.readBlockPos();
                        BlockEntity be = inv.player.level().getBlockEntity(pos);
                        if (be instanceof FrozenEssenceMakerBlockEntity entity) {
                            return new FrozenEssenceMakerMenu(windowId, inv, entity);
                        } else {
                            // 客户端或未同步时，只传递位置
                            return new FrozenEssenceMakerMenu(windowId, inv, pos);
                        }
                    }));

    public static final RegistryObject<MenuType<EssenceEarthFurnaceMenu>> ESSENCE_EARTH_FURNACE =
            MENUS.register("essence_earth_furnace",
                    () -> IForgeMenuType.create(EssenceEarthFurnaceMenu::new));

    public static final RegistryObject<MenuType<JuiceExtractorMenu>> JUICE_EXTRACTOR_MENU =
            MENUS.register("juice_extractor", () -> IForgeMenuType.create((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                BlockEntity be = inv.player.level().getBlockEntity(pos);
                if (be instanceof JuiceExtractorBlockEntity extractor) {
                    return new JuiceExtractorMenu(windowId, inv, extractor);
                }
                return new JuiceExtractorMenu(windowId, inv); // fallback
            }));

    public static final RegistryObject<MenuType<EssenceBlastFurnaceMenu>> ESSENCE_BLAST_FURNACE_MENU =
            MENUS.register("essence_blast_furnace",
                    () -> IForgeMenuType.create(EssenceBlastFurnaceMenu::new));

    public static final RegistryObject<MenuType<ExpulsionMachineMenu>> EXPULSION_MACHINE =
            MENUS.register("expulsion_machine",
                    () -> IForgeMenuType.create(ExpulsionMachineMenu::new));

    public static final RegistryObject<MenuType<AirPurifierMenu>> AIR_PURIFIER_MENU =
            MENUS.register("air_purifier",
                    () -> IForgeMenuType.create((windowId, inv, data) ->
                            new AirPurifierMenu(windowId, inv, data.readBlockPos())));
}
