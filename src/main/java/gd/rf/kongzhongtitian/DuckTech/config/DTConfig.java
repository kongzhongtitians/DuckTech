package gd.rf.kongzhongtitian.DuckTech.config;

import gd.rf.kongzhongtitian.DuckTech.DuckTech;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class DTConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue SWITCH_SOUND;

    static {
        //BUILDER.push("DuckTech Configuration");

        SWITCH_SOUND = BUILDER
                .define("switchSound", true);

        //BUILDER.pop();

        SPEC = BUILDER.build();
    }

    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SPEC, "ducktech-common.toml");
        DuckTech.LOGGER.info("DuckTech 配置已注册");
    }

    public static boolean switch_sound() {
        return SWITCH_SOUND.get();
    }
}