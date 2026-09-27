package gd.rf.kongzhongtitian.DuckTech.entities;

import gd.rf.kongzhongtitian.DuckTech.DuckTech;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DTEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, DuckTech.MOD_ID);

    public static final RegistryObject<EntityType<RubberDuckEntity>> RUBBER_DUCK =
            ENTITY_TYPES.register("rubber_duck", () -> EntityType.Builder
                    .of(RubberDuckEntity::new, MobCategory.MISC) // 使用MISC分类，避免自然生成逻辑
                    .sized(0.6F, 0.8F) // 碰撞箱大小
                    .build(ResourceLocation.fromNamespaceAndPath(DuckTech.MOD_ID, "rubber_duck").toString()));
}