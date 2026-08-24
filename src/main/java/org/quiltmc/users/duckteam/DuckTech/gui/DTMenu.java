package org.quiltmc.users.duckteam.DuckTech.gui;

import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.quiltmc.users.duckteam.DuckTech.DuckTech;
import org.quiltmc.users.duckteam.DuckTech.blocks.blockentity.FE2ThermalEssenceMachineBlockEntity;
import org.quiltmc.users.duckteam.DuckTech.blocks.blockentity.FrozenEssenceMakerBlockEntity;
import org.quiltmc.users.duckteam.DuckTech.blocks.blockentity.ThermalEssenceMakerBlockEntity;
import org.quiltmc.users.duckteam.DuckTech.gui.advance_shredder.AdvanceShredderMenu;
import org.quiltmc.users.duckteam.DuckTech.gui.air_purifier.AirPurifierMenu;
import org.quiltmc.users.duckteam.DuckTech.gui.essence_blast_furnace.EssenceBlastFurnaceMenu;
import org.quiltmc.users.duckteam.DuckTech.gui.essence_conversion_machine.EssenceConversionMachineMenu;
import org.quiltmc.users.duckteam.DuckTech.gui.essence_furnace.EssenceFurnaceMenu;
import org.quiltmc.users.duckteam.DuckTech.gui.expulsion_machine.ExpulsionMachineMenu;
import org.quiltmc.users.duckteam.DuckTech.gui.fe2thermal_essence_machine.FE2ThermalEssenceMachineMenu;
import org.quiltmc.users.duckteam.DuckTech.gui.frozen_essence_maker.FrozenEssenceMakerMenu;
import org.quiltmc.users.duckteam.DuckTech.gui.injection_machine.InjectionMachineMenu;
import org.quiltmc.users.duckteam.DuckTech.gui.juice_extractor.JuiceExtractorMenu;
import org.quiltmc.users.duckteam.DuckTech.gui.levitation.LevitationMachineMenu;
import org.quiltmc.users.duckteam.DuckTech.gui.essence_earth_furnace.EssenceEarthFurnaceMenu;
import org.quiltmc.users.duckteam.DuckTech.gui.thermal_essence_maker.ThermalEssenceMakerMenu;

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
            MENUS.register("juice_extractor_menu", () -> IForgeMenuType.create(JuiceExtractorMenu::new
            ));

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
