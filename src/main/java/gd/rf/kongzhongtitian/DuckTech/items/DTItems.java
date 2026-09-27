package gd.rf.kongzhongtitian.DuckTech.items;

import gd.rf.kongzhongtitian.DuckTech.DuckTech;
import gd.rf.kongzhongtitian.DuckTech.entities.DTEntities;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DTItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DuckTech.MODID);

    //粉
    public static final RegistryObject<Item> IRON_DUST = registerSimpleItem("iron_dust");
    public static final RegistryObject<Item> GOLDEN_DUST = registerSimpleItem("golden_dust");
    public static final RegistryObject<Item> COPPER_DUST = registerSimpleItem("copper_dust");
    public static final RegistryObject<Item> ALUMINUM_DUST = registerSimpleItem("aluminum_dust");
    public static final RegistryObject<Item> LEAD_DUST = registerSimpleItem("lead_dust");
    public static final RegistryObject<Item> TIN_DUST = registerSimpleItem("tin_dust");
    public static final RegistryObject<Item> SILVER_DUST = registerSimpleItem("silver_dust");
    public static final RegistryObject<Item> BRONZE_DUST = registerSimpleItem("bronze_dust");
    public static final RegistryObject<Item> DIAMOND_DUST = registerSimpleItem("diamond_dust");

    //精华
    public static final RegistryObject<Item> CRUSHED_ESSENCE = registerSimpleItem("crushed_essence");
    public static final RegistryObject<Item> ADVANCE_CRUSHED_ESSENCE = registerSimpleItem("advance_crushed_essence");
    public static final RegistryObject<Item> AIR_ESSENCE = registerSimpleItem("air_essence");
    public static final RegistryObject<Item> BASIC_ESSENCE = registerSimpleItem("basic_essence");
    public static final RegistryObject<Item> VOID_ESSENCE = registerSimpleItem("void_essence");
    public static final RegistryObject<Item> THERMAL_ESSENCE = registerSimpleItem("thermal_essence");
    public static final RegistryObject<Item> FROZEN_ESSENCE = registerSimpleItem("frozen_essence");
    public static final RegistryObject<Item> DUCK_ESSENCE = registerSimpleItem("duck_essence");

    //齿轮
    public static final RegistryObject<Item> IRON_GEAR = registerSimpleItem("iron_gear");
    public static final RegistryObject<Item> GOLDEN_GEAR = registerSimpleItem("golden_gear");
    public static final RegistryObject<Item> GOLD_PLATED_GEAR = registerSimpleItem("gold_plated_gear");
    public static final RegistryObject<Item> TIN_GEAR = registerSimpleItem("tin_gear");

    //锭
    public static final RegistryObject<Item> LEAD_INGOT = registerSimpleItem("lead_ingot");
    public static final RegistryObject<Item> ALUMINUM_INGOT = registerSimpleItem("aluminum_ingot");
    public static final RegistryObject<Item> TIN_INGOT = registerSimpleItem("tin_ingot");
    public static final RegistryObject<Item> SILVER_INGOT = registerSimpleItem("silver_ingot");
    public static final RegistryObject<Item> BRONZE_INGOT = registerSimpleItem("bronze_ingot");

    //nugget
    public static final RegistryObject<Item> LEAD_NUGGET = registerSimpleItem("lead_nugget");
    public static final RegistryObject<Item> ALUMINUM_NUGGET = registerSimpleItem("aluminum_nugget");
    public static final RegistryObject<Item> TIN_NUGGET = registerSimpleItem("tin_nugget");
    public static final RegistryObject<Item> SILVER_NUGGET = registerSimpleItem("silver_nugget");
    public static final RegistryObject<Item> BRONZE_NUGGET = registerSimpleItem("bronze_nugget");

    //粗
    public static final RegistryObject<Item> RAW_LEAD = registerSimpleItem("raw_lead");
    public static final RegistryObject<Item> RAW_ALUMINUM = registerSimpleItem("raw_aluminum");
    public static final RegistryObject<Item> RAW_TIN = registerSimpleItem("raw_tin");
    public static final RegistryObject<Item> RAW_SILVER = registerSimpleItem("raw_silver");

    //板
    public static final RegistryObject<Item> IRON_PLATE = registerSimpleItem("iron_plate");
    public static final RegistryObject<Item> ALUMINUM_PLATE = registerSimpleItem("aluminum_plate");
    public static final RegistryObject<Item> TIN_PLATE = registerSimpleItem("tin_plate");
    public static final RegistryObject<Item> BRONZE_PLATE = registerSimpleItem("bronze_plate");

    //外壳
    public static final RegistryObject<Item> IRON_HULL = registerSimpleItem("iron_hull");
    public static final RegistryObject<Item> ALUMINUM_HULL = registerSimpleItem("aluminum_hull");
    public static final RegistryObject<Item> TIN_HULL = registerSimpleItem("tin_hull");
    public static final RegistryObject<Item> BRONZE_HULL = registerSimpleItem("bronze_hull");

    //基础
    public static final RegistryObject<Item> BASIC_ESSENCE_DUST = registerSimpleItem("basic_essence_dust");
    public static final RegistryObject<Item> BASIC_ESSENCE_MESH = registerSimpleItem("basic_essence_mesh");
    public static final RegistryObject<Item> BASIC_ESSENCE_PLATE = registerSimpleItem("basic_essence_plate");

    //other
    public static final RegistryObject<ForgeSpawnEggItem> RUBBER_DUCK =
            ITEMS.register("rubber_duck",
                    () -> new ForgeSpawnEggItem(DTEntities.RUBBER_DUCK, 0xFFFFFF, 0xFFFFFF,
                            new Item.Properties()));
    public static final RegistryObject<Item> RUBBER = registerSimpleItem("rubber");
    public static final RegistryObject<Item> RUBBER_BUCKET = ITEMS.register("rubber_bucket",
            () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> YELLOW_RUBBER = registerSimpleItem("yellow_rubber");
    public static final RegistryObject<Item> DUCKTECH = registerSimpleItem("ducktech");
    public static final RegistryObject<Item> SPEED_UPGRADE = registerSimpleItem("speed_upgrade");
    public static final RegistryObject<Item> STACK_UPGRADE = registerSimpleItem("stack_upgrade");

    //SuLiao
    public static final RegistryObject<Item> PLASTIC_SHEET = registerSimpleItem("plastic_sheet");
    public static final RegistryObject<Item> PLASTIC_CLUMP = registerSimpleItem("plastic_clump");

    //耐久
    public static final RegistryObject<Item> FORGE_HAMMER = ITEMS.register("forge_hammer",
            () -> new Item(new Item.Properties().stacksTo(1).durability(64)));
    public static final RegistryObject<Item> SACRIFICIAL_KNIFE = ITEMS.register("sacrificial_knife",
            () -> new Item(new Item.Properties().stacksTo(1).durability(64)));

    //food
    public static final RegistryObject<Item> DUCK_JUICE = ITEMS.register("duck_juice",
            () -> new JuiceItem(new Item.Properties()
                    .stacksTo(16)
                    .food(new FoodProperties.Builder()
                            .nutrition(4)
                            .saturationMod(0.5F)
                            .alwaysEat()
                            .build())));
    public static final RegistryObject<Item> APPLE_JUICE = ITEMS.register("apple_juice",
            () -> new JuiceItem(new Item.Properties()
                    .stacksTo(16)
                    .food(new FoodProperties.Builder()
                            .nutrition(4)
                            .saturationMod(0.5F)
                            .alwaysEat()
                            .build())));
    public static final RegistryObject<Item> BAMBOO_JUICE = ITEMS.register("bamboo_juice",
            () -> new JuiceItem(new Item.Properties()
                    .stacksTo(16)
                    .food(new FoodProperties.Builder()
                            .nutrition(4)
                            .saturationMod(0.5F)
                            .alwaysEat()
                            .build())));
    public static final RegistryObject<Item> PUMPKIN_JUICE = ITEMS.register("pumpkin_juice",
            () -> new JuiceItem(new Item.Properties()
                    .stacksTo(16)
                    .food(new FoodProperties.Builder()
                            .nutrition(4)
                            .saturationMod(0.5F)
                            .alwaysEat()
                            .build())));
    public static final RegistryObject<Item> WATERMELON_JUICE = ITEMS.register("watermelon_juice",
            () -> new JuiceItem(new Item.Properties()
                    .stacksTo(16)
                    .food(new FoodProperties.Builder()
                            .nutrition(4)
                            .saturationMod(0.5F)
                            .alwaysEat()
                            .build())));
    public static final RegistryObject<Item> EGG_JUICE = ITEMS.register("egg_juice",
            () -> new JuiceItem(new Item.Properties()
                    .stacksTo(16)
                    .food(new FoodProperties.Builder()
                            .nutrition(4)
                            .saturationMod(0.5F)
                            .alwaysEat()
                            .build())));
    public static final RegistryObject<Item> KELP_JUICE = ITEMS.register("kelp_juice",
            () -> new JuiceItem(new Item.Properties()
                    .stacksTo(16)
                    .food(new FoodProperties.Builder()
                            .nutrition(4)
                            .saturationMod(0.5F)
                            .alwaysEat()
                            .build())));
    public static final RegistryObject<Item> COCOA_BEAN_JUICE = ITEMS.register("cocoa_bean_juice",
            () -> new JuiceItem(new Item.Properties()
                    .stacksTo(16)
                    .food(new FoodProperties.Builder()
                            .nutrition(4)
                            .saturationMod(0.5F)
                            .alwaysEat()
                            .build())));
    public static final RegistryObject<Item> SUGAR_CANE_JUICE = ITEMS.register("sugar_cane_juice",
            () -> new JuiceItem(new Item.Properties()
                    .stacksTo(16)
                    .food(new FoodProperties.Builder()
                            .nutrition(4)
                            .saturationMod(0.5F)
                            .alwaysEat()
                            .build())));
    public static final RegistryObject<Item> WHEAT_JUICE = ITEMS.register("wheat_juice",
            () -> new JuiceItem(new Item.Properties()
                    .stacksTo(16)
                    .food(new FoodProperties.Builder()
                            .nutrition(4)
                            .saturationMod(0.5F)
                            .alwaysEat()
                            .build())));
    public static final RegistryObject<Item> BEETROOT_JUICE = ITEMS.register("beetroot_juice",
            () -> new JuiceItem(new Item.Properties()
                    .stacksTo(16)
                    .food(new FoodProperties.Builder()
                            .nutrition(4)
                            .saturationMod(0.5F)
                            .alwaysEat()
                            .build())));
    public static final RegistryObject<Item> POTATO_JUICE = ITEMS.register("potato_juice",
            () -> new JuiceItem(new Item.Properties()
                    .stacksTo(16)
                    .food(new FoodProperties.Builder()
                            .nutrition(4)
                            .saturationMod(0.5F)
                            .alwaysEat()
                            .build())));

    //盔甲
    /*
    public static final RegistryObject<Item> BASIC_ESSENCE_HELMET_LEVEL_ONE = ITEMS.register("basic_essence_helmet_level_one",
            () -> new ArmorItem(DTArmorMaterial.BASIC_ESSENCE_LEVEL_ONE, ArmorItem.Type.HELMET, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> BASIC_ESSENCE_CHESTPLATE_LEVEL_ONE = ITEMS.register("basic_essence_chestplate_level_one",
            () -> new ArmorItem(DTArmorMaterial.BASIC_ESSENCE_LEVEL_ONE, ArmorItem.Type.CHESTPLATE, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> BASIC_ESSENCE_LEGGINGS_LEVEL_ONE = ITEMS.register("basic_essence_leggings_level_one",
            () -> new ArmorItem(DTArmorMaterial.BASIC_ESSENCE_LEVEL_ONE, ArmorItem.Type.LEGGINGS, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> BASIC_ESSENCE_BOOTS_LEVEL_ONE = ITEMS.register("basic_essence_boots_level_one",
            () -> new ArmorItem(DTArmorMaterial.BASIC_ESSENCE_LEVEL_ONE, ArmorItem.Type.BOOTS, new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> BASIC_ESSENCE_HELMET_LEVEL_TWO = ITEMS.register("basic_essence_helmet_level_two",
            () -> new ArmorItem(DTArmorMaterial.BASIC_ESSENCE_LEVEL_TWO, ArmorItem.Type.HELMET, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> BASIC_ESSENCE_CHESTPLATE_LEVEL_TWO = ITEMS.register("basic_essence_chestplate_level_two",
            () -> new ArmorItem(DTArmorMaterial.BASIC_ESSENCE_LEVEL_TWO, ArmorItem.Type.CHESTPLATE, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> BASIC_ESSENCE_LEGGINGS_LEVEL_TWO = ITEMS.register("basic_essence_leggings_level_two",
            () -> new ArmorItem(DTArmorMaterial.BASIC_ESSENCE_LEVEL_TWO, ArmorItem.Type.LEGGINGS, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> BASIC_ESSENCE_BOOTS_LEVEL_TWO = ITEMS.register("basic_essence_boots_level_two",
            () -> new ArmorItem(DTArmorMaterial.BASIC_ESSENCE_LEVEL_TWO, ArmorItem.Type.BOOTS, new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> BASIC_ESSENCE_HELMET_LEVEL_THREE = ITEMS.register("basic_essence_helmet_level_three",
            () -> new ArmorItem(DTArmorMaterial.BASIC_ESSENCE_LEVEL_THREE, ArmorItem.Type.HELMET, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> BASIC_ESSENCE_CHESTPLATE_LEVEL_THREE = ITEMS.register("basic_essence_chestplate_level_three",
            () -> new ArmorItem(DTArmorMaterial.BASIC_ESSENCE_LEVEL_THREE, ArmorItem.Type.CHESTPLATE, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> BASIC_ESSENCE_LEGGINGS_LEVEL_THREE = ITEMS.register("basic_essence_leggings_level_three",
            () -> new ArmorItem(DTArmorMaterial.BASIC_ESSENCE_LEVEL_THREE, ArmorItem.Type.LEGGINGS, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> BASIC_ESSENCE_BOOTS_LEVEL_THREE = ITEMS.register("basic_essence_boots_level_three",
            () -> new ArmorItem(DTArmorMaterial.BASIC_ESSENCE_LEVEL_THREE, ArmorItem.Type.BOOTS, new Item.Properties().stacksTo(1)));
*/

    public static RegistryObject<Item> registerSimpleItem(String itemName){
        return ITEMS.register(itemName , ()-> new Item(new Item.Properties()));
    }
}
