package gd.rf.kongzhongtitian.DuckTech.items;

import com.mojang.blaze3d.platform.InputConstants;
import gd.rf.kongzhongtitian.DuckTech.DuckTech;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = DuckTech.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FrozenEssenceKeyMappings {
    public static final String CATEGORY = "DuckTech";
    public static final KeyMapping TOGGLE_FROST_WALKER = new KeyMapping(
            "Toggle Frost Walker",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_F,
            CATEGORY
    );

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(TOGGLE_FROST_WALKER);
    }
}
