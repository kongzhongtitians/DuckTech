package gd.rf.kongzhongtitian.DuckTech.entities;

import gd.rf.kongzhongtitian.DuckTech.DuckTech;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RubberDuckRenderer extends MobRenderer<RubberDuckEntity, EntityModel<RubberDuckEntity>> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(DuckTech.MOD_ID, "textures/entity/rubber_duck.png");

    public RubberDuckRenderer(EntityRendererProvider.Context context) {
        super(context, new RubberDuckModel<>(context.bakeLayer(RubberDuckModel.LAYER_LOCATION)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(RubberDuckEntity entity) {
        return TEXTURE;
    }
}